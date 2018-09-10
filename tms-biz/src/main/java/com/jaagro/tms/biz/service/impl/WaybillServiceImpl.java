package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.truck.TruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.OrderGoods;
import com.jaagro.tms.biz.entity.OrderItems;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.mapper.OrderGoodsMapper;
import com.jaagro.tms.biz.mapper.OrderItemsMapper;
import com.jaagro.tms.biz.mapper.OrdersMapper;
import com.jaagro.tms.biz.mapper.WaybillMapper;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import lombok.Data;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gavin
 */
@Service
public class WaybillServiceImpl implements WaybillService {
    private static final Logger log = LoggerFactory.getLogger(WaybillServiceImpl.class);
    @Autowired
    private WaybillMapper waybillMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<ListWaybillPlanDto> createWaybillPlan(CreateWaybillPlanDto waybillDto){
        Integer orderId = waybillDto.getOrderId();
        List<CreateWaybillItemsPlanDto>  waybillItemsDtos = waybillDto.getWaybillItems();
        List<TruckDto> truckDtos = waybillDto.getTrucks();
        List<MiddleObject> middleObjects = new ArrayList<>();
        for(CreateWaybillItemsPlanDto waybillItemsDto:waybillItemsDtos) {
            Integer orderItemId = waybillItemsDto.getOrderItemId();
            List<CreateWaybillGoodsPlanDto> goods =  waybillItemsDto.getGoods();
            for(CreateWaybillGoodsPlanDto waybillGoodsDto:goods){
                MiddleObject  middleObject = new MiddleObject();
                middleObject.setOrderId(orderId);
                middleObject.setOrderItemId(orderItemId);
                middleObject.setOrderGoodsId(waybillGoodsDto.getGoodsId());
                middleObject.setProportioning(waybillGoodsDto.getProportioning());
                middleObjects.add(middleObject);
            }
        }
        List<ListWaybillPlanDto> waybillDtos = null;
        if(!CollectionUtils.isEmpty(middleObjects)){
            try {
                   waybillDtos=wayBillAlgorithm(middleObjects,truckDtos);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("计算配运单失败，货物内容={}", middleObjects);
                throw new RuntimeException("计算配运单失败");
            }
        }
        return waybillDtos;
    }
    /**
     * 货物配运单核心算法
     * @param middleObjects
     * @param truckDtos
     */
    private List<ListWaybillPlanDto> wayBillAlgorithm(List<MiddleObject> middleObjects,List<TruckDto> truckDtos){
        List<MiddleObject> middleObjects_assigned = new ArrayList<>();
        List<ListWaybillPlanDto> waybillDtos = new ArrayList<>();
        //按配送地排序
        List<MiddleObject> newMiddleObjectsList = middleObjects.stream().sorted((e1,e2)->Integer.compare(e1.getOrderItemId(),e2.getOrderItemId())).collect(Collectors.toList());
            Iterator<TruckDto> truckIterator = truckDtos.iterator();
            while(truckIterator.hasNext()) {
                TruckDto truckDto = truckIterator.next();
                Integer capacity = truckDto.getCapacity();
                //使用按照送货地排序后货物分配到车辆生成临时送货单
                List<MiddleObject> assignList = new ArrayList<>();
                for (int k = 0;k<truckDto.getNumber(); k++){
                    Iterator<MiddleObject> goodsIterator = newMiddleObjectsList.iterator();
                    Integer proposioningCount = 0;
                    while (goodsIterator.hasNext()) {
                        MiddleObject middleObject = goodsIterator.next();
                        proposioningCount += middleObject.getProportioning();
                        Integer planAmount  = (middleObject.getProportioning() + capacity - proposioningCount);
                        MiddleObject assignedObj = new MiddleObject();
                        BeanUtils.copyProperties(middleObject,assignedObj);
                        assignList.add(assignedObj);
                        if (proposioningCount > capacity) {
                            List<MiddleObject> assignedDtos = assignList.stream().filter(c -> c.orderGoodsId.equals(middleObject.getOrderGoodsId())).collect(Collectors.toList());
                            assignedDtos.get(0).setTruckId(truckDto.getTruckId());
                            assignedDtos.get(0).setProportioning(planAmount);
                            assignedDtos.get(0).setPlanAmount(planAmount);
                            assignedDtos.get(0).setUnPlanAmount(0);
                            middleObject.setProportioning(proposioningCount - capacity);
                        } else {
                            List<MiddleObject> assignedDtos = assignList.stream().filter(c -> c.orderGoodsId.equals(middleObject.getOrderGoodsId())).collect(Collectors.toList());
                            assignedDtos.get(0).setTruckId(truckDto.getTruckId());
                            assignedDtos.get(0).setPlanAmount(middleObject.getProportioning());
                            assignedDtos.get(0).setUnPlanAmount(0);
                            goodsIterator.remove();
                        }

                        Integer assignTotal = assignList.stream().mapToInt(c -> c.getPlanAmount()).sum();
                        boolean flag = (assignTotal.equals(capacity) || (assignTotal<capacity && newMiddleObjectsList.size()==0));
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
        System.out.println("margin==="+newMiddleObjectsList.toString());
        System.out.println("assigned==="+middleObjects_assigned.toString());
        log.error("货物配运时剩余货物，margin={}", newMiddleObjectsList);
        log.error("货物配运时已经分配的货物，assigned={}", middleObjects_assigned);

        return waybillDtos;
}

    /**
     * 生成一个运单计划
     * @param assignList
     * @return
     */
    private  ListWaybillPlanDto makeOneWaybillPlan(List<MiddleObject> assignList){
        ListWaybillPlanDto waybillPlanDto = new ListWaybillPlanDto();
        Orders ordersData = null;
        ListTruckTypeDto truckType = null;
        Integer orderId = 0;
        for (MiddleObject obj:assignList) {
            orderId = obj.getOrderId();
            ordersData = ordersMapper.selectByPrimaryKey(orderId);
            if(ordersData == null){
                throw new NullPointerException("订单号为：" + orderId + " 不存在");
            }
            Integer truckTypeId= obj.getTruckId();
            truckType = truckTypeClientService.getTruckTypeById(truckTypeId);
            break;
        }
        waybillPlanDto.setOrderId(orderId);
        waybillPlanDto.setNeedTruckType(truckType);
        waybillPlanDto.setLoadSiteId(ordersData.getLoadSiteId());
        waybillPlanDto.setTruckTeamContractId(ordersData.getCustomerContractId());

        List<ListWaybillItemsPlanDto> itemsList = new ArrayList<>();
        for (MiddleObject obj:assignList) {
             OrderGoods orderGoods = orderGoodsMapper.selectByPrimaryKey(obj.getOrderGoodsId());
            Integer goodsUnit = orderGoods.getGoodsUnit();
            if(orderGoods == null){
                throw new NullPointerException("goodsId为：" + obj.getOrderGoodsId()+ " 的货物不存在");
            }
            OrderItems orderItems = orderItemsMapper.selectByPrimaryKey(obj.getOrderItemId());
            ListWaybillItemsPlanDto createWaybillItemsDto = new ListWaybillItemsPlanDto();
            createWaybillItemsDto
                    .setUnloadSiteId(orderItems.getUnloadId())
                    .setRequiredTime(orderItems.getUnloadTime());
            List<ListWaybillGoodsPlanDto> goodsList = new LinkedList<>();
            ListWaybillGoodsPlanDto createWaybillGoodsDto = new ListWaybillGoodsPlanDto();
            createWaybillGoodsDto
                    .setGoodsId(orderGoods.getId())
                    .setGoodsName(orderGoods.getGoodsName())
                    .setGoodsQuantity(orderGoods.getGoodsQuantity())
                    .setGoodsUnit(orderGoods.getGoodsUnit())
                    .setJoinDrug(orderGoods.getJoinDrug());
            if(goodsUnit==3){
                createWaybillGoodsDto.setGoodsWeight(new BigDecimal(obj.getPlanAmount()));
            }else{
                createWaybillGoodsDto.setGoodsQuantity(obj.getPlanAmount());
            }
            goodsList.add(createWaybillGoodsDto);
            createWaybillItemsDto.setGoods(goodsList);
            itemsList.add(createWaybillItemsDto);
        }
        waybillPlanDto.setWaybillItems(itemsList);
    return waybillPlanDto;
 }

    @Override
    public Map<String,Object> createWaybill(List<CreateWaybillDto> waybillDtos) {

        return null;
    }
    @Data
    @Accessors(chain = true)
    public class MiddleObject{
        private Integer orderId;
        private Integer truckId;
        private Integer orderItemId;
        private Integer orderGoodsId;
        /**
         * 可配量
         */
        private Integer proportioning;
        /**
         * 已配量
         */
        private Integer planAmount;
        /**
         * 剩余量
         */
        private Integer unPlanAmount;
    }
}
