package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.constant.GoodsType;
import com.jaagro.tms.api.constant.WaybillStatus;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.truck.TruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillPlanService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import com.jaagro.tms.biz.bo.MiddleObjectBo;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tony
 */
@Service
public class WaybillPlanServiceImpl implements WaybillPlanService {

    private static final Logger log = LoggerFactory.getLogger(WaybillPlanServiceImpl.class);

    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private OrderItemsMapperExt orderItemsMapper;
    @Autowired
    private OrderGoodsMapperExt orderGoodsMapper;
    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private OrderGoodsMarginMapperExt orderGoodsMarginMapper;
    @Autowired
    private WaybillGoodsMapperExt waybillGoodsMapper;
    @Autowired
    private CatchChickenTimeMapperExt catchChickenTimeMapperExt;

    @Override
    public List<ListWaybillPlanDto> createWaybillPlan(CreateWaybillPlanDto waybillDto) {
        Integer orderId = waybillDto.getOrderId();
        List<CreateWaybillItemsPlanDto> waybillItemsDtos = waybillDto.getWaybillItems();
        List<TruckDto> truckDtos = waybillDto.getTrucks();
        List<MiddleObjectBo> middleObjects = new ArrayList<>();
        for (CreateWaybillItemsPlanDto waybillItemsDto : waybillItemsDtos) {
            Integer orderItemId = waybillItemsDto.getOrderItemId();
            List<CreateWaybillGoodsPlanDto> goods = waybillItemsDto.getGoods();
            for (CreateWaybillGoodsPlanDto waybillGoodsDto : goods) {
                MiddleObjectBo middleObject = new MiddleObjectBo();
                middleObject.setOrderId(orderId);
                middleObject.setOrderItemId(waybillGoodsDto.getOrderItemId());
                middleObject.setOrderGoodsId(waybillGoodsDto.getGoodsId());
                middleObject.setProportioning(waybillGoodsDto.getProportioning());
                middleObject.setUnPlanAmount(0);
                middleObjects.add(middleObject);
            }
        }
        List<ListWaybillPlanDto> waybillDtos = null;
        if (!CollectionUtils.isEmpty(middleObjects)) {
            try {
                waybillDtos = wayBillAlgorithm(middleObjects, truckDtos);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("计算配运单失败，货物内容={}", middleObjects);
                throw new RuntimeException("计算配运单失败");
            }
        }
        try {
            //更新运单货物是生鸡时的装货时间和卸货时间
            int goodType = waybillDtos.get(0).getGoodType();
            if (GoodsType.CHICKEN.equals(goodType)) {
                waybillDtos = getloadTimeAndUnloadTime(waybillDtos, truckDtos);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("计算毛鸡配运单的装货时间和卸货时间失败:{}", e.getMessage());
            throw new RuntimeException("计算毛鸡配运单的装货时间和卸货时间失败");
        }
        return waybillDtos;
    }

    /**
     * 从配载计划中移除运单【逻辑删除】
     *
     * @param waybillId
     * @return 结果集
     * @author tony
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> removeWaybillFromPlan(Integer waybillId) {
        //判断运单状态是否满足条件
        Waybill waybillData = waybillMapper.selectByPrimaryKey(waybillId);

        List<OrderGoodsMargin> orderGoodsMarginList = new LinkedList<>();
        if (null == waybillData) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), waybillId + " :id无效");
        }
        boolean flag = waybillData.getWaybillStatus().equals(WaybillStatus.RECEIVE) ||
                waybillData.getWaybillStatus().equals(WaybillStatus.SEND_TRUCK) ||
                waybillData.getWaybillStatus().equals(WaybillStatus.REJECT);
        if (!flag) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "只有【待派单/待接单/已拒绝】的运单才可以撤销");
        }
        if (!waybillData.getEnabled()) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), waybillId + " :id已是注销状态");
        }

        List<GetWaybillGoodsDto> waybillGoodsDtoList = waybillGoodsMapper.listGoodsByWaybillId(waybillId);
        for (GetWaybillGoodsDto wg : waybillGoodsDtoList) {
            OrderGoodsMargin orderGoodsMargin = new OrderGoodsMargin();
            OrderGoodsMargin orderGoodsMarginData = orderGoodsMarginMapper.getMarginByGoodsId(wg.getOrderGoodsId());
            if (orderGoodsMarginData == null) {
                return ServiceResult.error("货物余量表记录为空");
            }
            orderGoodsMargin
                    .setId(orderGoodsMarginData.getId())
                    .setOrderGoodsId(wg.getOrderGoodsId());
            Orders orders = ordersMapper.selectByPrimaryKey(waybillData.getOrderId());
            //饲料
            if (orders.getGoodsType() == 2) {
                BigDecimal m = orderGoodsMarginData.getMargin().add(wg.getGoodsWeight());
                orderGoodsMargin.setMargin(m);
            } else {
                BigDecimal m = orderGoodsMarginData.getMargin().add(new BigDecimal(wg.getGoodsQuantity()));
                orderGoodsMargin.setMargin(m);
            }
            int count = orderGoodsMarginMapper.updateByPrimaryKeySelective(orderGoodsMargin);
            if (count == 0) {
                log.debug(orderGoodsMargin + " :余量未被更新");
                continue;
            }
            orderGoodsMarginList.add(orderGoodsMargin);
        }
        waybillMapper.removeWaybillById(waybillId);
        System.out.println(orderGoodsMarginList);
        return ServiceResult.toResult(orderGoodsMarginList);
    }

    /**
     * 货物配运单核心算法
     *
     * @param middleObjects
     * @param truckDtos
     * @return
     */
    private List<ListWaybillPlanDto> wayBillAlgorithm(List<MiddleObjectBo> middleObjects, List<TruckDto> truckDtos) throws RuntimeException{
        List<MiddleObjectBo> middleObjects_assigned = new ArrayList<>();
        List<ListWaybillPlanDto> waybillDtos = new ArrayList<>();
        //按配送地排序
        List<MiddleObjectBo> newMiddleObjectsList = middleObjects.stream().sorted((e1, e2) -> Integer.compare(e1.getOrderItemId(), e2.getOrderItemId())).collect(Collectors.toList());
        Iterator<TruckDto> truckIterator = truckDtos.iterator();
        while (truckIterator.hasNext()) {
            TruckDto truckDto = truckIterator.next();
            Integer capacity = truckDto.getCapacity();
            //使用按照送货地排序后货物分配到车辆生成临时送货单
            List<MiddleObjectBo> assignList = new ArrayList<>();
            for (int k = 0; k < truckDto.getNumber(); k++) {
                Iterator<MiddleObjectBo> goodsIterator = newMiddleObjectsList.iterator();
                Integer proposioningCount = 0;
                while (goodsIterator.hasNext()) {
                    MiddleObjectBo middleObject = goodsIterator.next();
                    proposioningCount += middleObject.getProportioning();
                    Integer planAmount = (middleObject.getProportioning() + capacity - proposioningCount);
                    MiddleObjectBo assignedObj = new MiddleObjectBo();
                    BeanUtils.copyProperties(middleObject, assignedObj);
                    assignList.add(assignedObj);
                    if (proposioningCount > capacity) {
                        List<MiddleObjectBo> assignedDtos = assignList.stream().filter(c -> c.getOrderGoodsId().equals(middleObject.getOrderGoodsId())).collect(Collectors.toList());
                        assignedDtos.get(0).setTruckId(truckDto.getTruckId());
                        assignedDtos.get(0).setProportioning(planAmount);
                        assignedDtos.get(0).setPlanAmount(planAmount);
                        assignedDtos.get(0).setUnPlanAmount(0);
                        middleObject.setProportioning(proposioningCount - capacity);
                    } else {
                        List<MiddleObjectBo> assignedDtos = assignList.stream().filter(c -> c.getOrderGoodsId().equals(middleObject.getOrderGoodsId())).collect(Collectors.toList());
                        assignedDtos.get(0).setTruckId(truckDto.getTruckId());
                        assignedDtos.get(0).setPlanAmount(middleObject.getProportioning());
                        assignedDtos.get(0).setUnPlanAmount(0);
                        goodsIterator.remove();
                    }

                    Integer assignTotal = assignList.stream().mapToInt(c -> c.getPlanAmount()).sum();
                    boolean flag = (assignTotal.equals(capacity) || (assignTotal < capacity && newMiddleObjectsList.size() == 0));
                    if (flag) {
                        middleObjects_assigned.addAll(assignList);
                        ListWaybillPlanDto waybillDto = this.makeOneWaybillPlan(assignList);
                        waybillDtos.add(waybillDto);
                        assignList.clear();
                        break;
                    }
                }
            }
        }
        System.out.println("margin===" + newMiddleObjectsList.toString());
        System.out.println("assigned===" + middleObjects_assigned.toString());
        log.error("货物配运时剩余货物，margin={}", newMiddleObjectsList);
        log.error("货物配运时已经分配的货物，assigned={}", middleObjects_assigned);

        return waybillDtos;
    }

    /**
     * 生成一个运单计划
     *
     * @param assignList
     * @return
     */
    private ListWaybillPlanDto makeOneWaybillPlan(List<MiddleObjectBo> assignList) {
        ListWaybillPlanDto waybillPlanDto = new ListWaybillPlanDto();
        Orders ordersData = null;
        ListTruckTypeDto truckType = null;
        Integer orderId = 0;
        Integer truckTypeId = 0;
        for (MiddleObjectBo obj : assignList) {
            orderId = obj.getOrderId();
            ordersData = ordersMapper.selectByPrimaryKey(orderId);
            if (ordersData == null) {
                throw new NullPointerException("订单号为：" + orderId + " 不存在");
            }
            truckTypeId = obj.getTruckId();
            truckType = truckTypeClientService.getTruckTypeById(truckTypeId);
            break;
        }
        waybillPlanDto.setOrderId(orderId);
        waybillPlanDto.setNeedTruckTypeId(truckTypeId);
        waybillPlanDto.setNeedTruckType(truckType);
        waybillPlanDto.setLoadSiteId(ordersData.getLoadSiteId());
        waybillPlanDto.setTruckTeamContractId(ordersData.getCustomerContractId());
        waybillPlanDto.setLoadTime(ordersData.getLoadTime());
        waybillPlanDto.setWaybillPlanTime(new Date());
        waybillPlanDto.setGoodType(ordersData.getGoodsType());
        List<ListWaybillItemsPlanDto> itemsList = new ArrayList<>();
        int totalAmount =0;
        for (MiddleObjectBo obj : assignList) {
            OrderGoods orderGoods = orderGoodsMapper.selectByPrimaryKey(obj.getOrderGoodsId());
            Integer goodsUnit = orderGoods.getGoodsUnit();
            if (orderGoods == null) {
                throw new NullPointerException("goodsId为：" + obj.getOrderGoodsId() + " 的货物不存在");
            }
            OrderItems orderItems = orderItemsMapper.selectByPrimaryKey(obj.getOrderItemId());
            ListWaybillItemsPlanDto waybillItemsPlanDto = new ListWaybillItemsPlanDto();
            ShowSiteDto showSiteDto = customerClientService.getShowSiteById(orderItems.getUnloadId());
            waybillItemsPlanDto.setUnloadSiteId(orderItems.getUnloadId());
            waybillItemsPlanDto.setOrderItemId(orderItems.getId());
            waybillItemsPlanDto.setShowSiteDto(showSiteDto);
            waybillItemsPlanDto.setRequiredTime(orderItems.getUnloadTime());
            List<ListWaybillGoodsPlanDto> goodsList = new LinkedList<>();
            ListWaybillGoodsPlanDto waybillGoodsDto = new ListWaybillGoodsPlanDto();
            waybillGoodsDto
                    .setOrderGoodsId(orderGoods.getId())
                    .setGoodsName(orderGoods.getGoodsName())
                    .setGoodsQuantity(orderGoods.getGoodsQuantity())
                    .setGoodsUnit(orderGoods.getGoodsUnit())
                    .setGoodsType(ordersData.getGoodsType())
                    .setJoinDrug(orderGoods.getJoinDrug());
            if (goodsUnit == 3) {
                waybillGoodsDto.setGoodsWeight(new BigDecimal(obj.getPlanAmount()));
            } else {
                waybillGoodsDto.setGoodsQuantity(obj.getPlanAmount());
            }
            totalAmount = totalAmount + obj.getPlanAmount();
            goodsList.add(waybillGoodsDto);
            waybillItemsPlanDto.setGoods(goodsList);
            itemsList.add(waybillItemsPlanDto);
        }
        waybillPlanDto.setTotalQuantity(totalAmount);
        waybillPlanDto.setTotalWeight(new BigDecimal(totalAmount));
        waybillPlanDto.setWaybillItems(itemsList);
        return waybillPlanDto;
    }

    /**
     * 订单是毛鸡时，重新计算装货时间和卸货时间
     *
     * @param waybillDtos
     * @param truckDtos
     * @return
     */
    private List<ListWaybillPlanDto> getloadTimeAndUnloadTime(List<ListWaybillPlanDto> waybillDtos, List<TruckDto> truckDtos) throws RuntimeException{
        ShowSiteDto loadSite = customerClientService.getShowSiteById(waybillDtos.get(0).getLoadSiteId());
        if (GoodsType.CHICKEN.equals(loadSite.getProductType())) {
            ListWaybillPlanDto dtoPrevious = new ListWaybillPlanDto();
            Date unloadTimePrevious = new Date();
            Date loadTime = new Date();
            Date unloadTime = new Date();
            for (int i = 0; i < waybillDtos.size(); i++) {
                ListWaybillPlanDto dto = waybillDtos.get(i);
                ShowSiteDto unloadSite = dto.getWaybillItems().get(0).getShowSiteDto();
                TruckDto truck = truckDtos.stream().filter(c -> c.getTruckId().equals(dto.getNeedTruckTypeId())).findAny().get();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Integer capacity = 0;
                Integer needTruckTypeId;
                if (i != 0) {
                    needTruckTypeId = dtoPrevious.getNeedTruckTypeId();
                    capacity = truckDtos.stream().filter(c -> c.getTruckId().equals(needTruckTypeId)).findAny().get().getCapacity();
                    int marginTime = capacity * 480 / unloadSite.getKillChain();
                    unloadTime = DateUtils.addMinutes(unloadTimePrevious, marginTime);
                    //计算并重设装货时间
                    CatchChickenTime catchChickenTime = new CatchChickenTime();
                    catchChickenTime.setFarmsType(loadSite.getFarmsType());
                    catchChickenTime.setTruckTypeId(truck.getTruckId());
                    List<CatchChickenTime> catchChickenTimes = catchChickenTimeMapperExt.listCatchChickenTimeByCriteria(catchChickenTime);
                    int catchTime = catchChickenTimes.get(0).getCatchTime();
                    loadTime = DateUtils.addMinutes(DateUtils.addMinutes(unloadTime, -truck.getTravelTime()), -loadSite.getOperationTime());
                    loadTime = DateUtils.addMinutes(DateUtils.addMinutes(loadTime, -catchTime), -20);
                    String strDate = DateFormatUtils.format(dto.getLoadTime(), "yyyy-MM-dd");
                    String strTime = DateFormatUtils.format(loadTime, "HH:mm:ss");
                    String dateTime = strDate + " " + strTime;
                    try {
                        loadTime = sdf.parse(dateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Date killTime = unloadSite.getKillTime();
                    int times = (capacity * 480 / unloadSite.getKillChain()) * i;
                    Date paramDate = DateUtils.addMinutes(killTime, times);
                    //计算并重设卸货时间
                    int waitKillTime = 0;
                    if (truck.getKilometres() > 80) {
                        waitKillTime = 30;
                    } else if (truck.getKilometres() > 30) {
                        waitKillTime = 25;
                    } else {
                        waitKillTime = 20;
                    }
                    unloadTime = DateUtils.addMinutes(paramDate, -waitKillTime);
                    Date requiredTime = dto.getWaybillItems().get(0).getRequiredTime();
                    String strDate = DateFormatUtils.format(requiredTime, "yyyy-MM-dd");
                    String strTime = DateFormatUtils.format(unloadTime, "HH:mm:ss");
                    String dateTime = strDate + " " + strTime;
                    try {
                        unloadTime = sdf.parse(dateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //计算并重设装货时间
                    CatchChickenTime catchChickenTime = new CatchChickenTime();
                    catchChickenTime.setFarmsType(loadSite.getFarmsType());
                    catchChickenTime.setTruckTypeId(truck.getTruckId());
                    List<CatchChickenTime> catchChickenTimes = catchChickenTimeMapperExt.listCatchChickenTimeByCriteria(catchChickenTime);
                    int catchTime = catchChickenTimes.get(0).getCatchTime();
                    loadTime = DateUtils.addMinutes(DateUtils.addMinutes(unloadTime, -truck.getTravelTime()), -loadSite.getOperationTime());
                    loadTime = DateUtils.addMinutes(DateUtils.addMinutes(loadTime, -catchTime), -20);
                    strDate = DateFormatUtils.format(dto.getLoadTime(), "yyyy-MM-dd");
                    strTime = DateFormatUtils.format(loadTime, "HH:mm:ss");
                    dateTime = strDate + " " + strTime;
                    try {
                        loadTime = sdf.parse(dateTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                dto.setLoadTime(loadTime);
                dto.getWaybillItems().get(0).setRequiredTime(unloadTime);
                dtoPrevious = dto;
                unloadTimePrevious = unloadTime;
            }
        }
        return waybillDtos;
    }
}
