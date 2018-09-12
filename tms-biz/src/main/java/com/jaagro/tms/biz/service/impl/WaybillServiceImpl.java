package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.OrderStatus;
import com.jaagro.tms.api.constant.WaybillStatus;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.order.GetOrderDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.DriverClientService;
import com.jaagro.tms.biz.service.TruckClientService;
import com.jaagro.tms.biz.service.TruckTypeClientService;
import com.jaagro.utils.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;


/**
 * @author tony
 */
@Service
public class WaybillServiceImpl implements WaybillService {
    private static final Logger log = LoggerFactory.getLogger(WaybillServiceImpl.class);

    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private TruckTypeClientService truckTypeClientService;
    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private WaybillMapper waybillMapper;
    @Autowired
    private WaybillItemsMapper waybillItemsMapper;
    @Autowired
    private WaybillGoodsMapper waybillGoodsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderGoodsMapper orderGoodsMapper;
    @Autowired
    private WaybillTrackingImagesMapper waybillTrackingImagesMapper;
    @Autowired
    private WaybillTrackingMapper waybillTrackingMapper;
    @Autowired
    private OrderGoodsMarginMapper orderGoodsMarginMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DriverClientService driverClientService;
    @Autowired
    private TruckClientService truckClientService;
    @Autowired
    private AppMessageMapperExt appMessageMapperExt;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> createWaybill(List<CreateWaybillDto> waybillDtoList) {
        //更新orders表的状态OrderStatus.STOWAGE
        for (CreateWaybillDto createWaybillDto : waybillDtoList) {
            Integer orderId = createWaybillDto.getOrderId();
            Orders orders = new Orders();
            orders.setId(orderId);
            orders.setOrderStatus(OrderStatus.STOWAGE);
            orders.setModifyTime(new Date());
            orders.setModifyUserId(99999999);
            ordersMapper.updateByPrimaryKeySelective(orders);
            break;
        }
        for (CreateWaybillDto createWaybillDto : waybillDtoList) {
            Integer orderId = createWaybillDto.getOrderId();
            Waybill waybill = new Waybill();
            waybill.setOrderId(orderId);
            waybill.setLoadSiteId(createWaybillDto.getLoadSiteId());
            waybill.setNeedTruckType(createWaybillDto.getNeedTruckTypeId());
            waybill.setTruckTeamContractId(createWaybillDto.getTruckTeamContractId());
            waybill.setWaybillStatus(WaybillStatus.RECEIVE);
            waybill.setCreateTime(new Date());
            waybill.setCreatedUserId(99999999);
            //waybill.setCreatedUserId(currentUserService.getCurrentUser().getId());
            waybillMapper.insertSelective(waybill);
            int waybillId = waybill.getId();
            List<CreateWaybillItemsDto> waybillItemsList = createWaybillDto.getWaybillItems();
            for (CreateWaybillItemsDto waybillItemsDto : waybillItemsList) {
                WaybillItems waybillItem = new WaybillItems();
                waybillItem.setWaybillId(waybillId);
                waybillItem.setUnloadSiteId(waybillItemsDto.getUnloadSiteId());
                waybillItem.setRequiredTime(waybillItemsDto.getRequiredTime());
                waybillItem.setModifyUserId(99999999);
                waybillItemsMapper.insertSelective(waybillItem);
                int waybillItemsId = waybillItem.getId();

                List<CreateWaybillGoodsDto> createWaybillGoodsDtoList = waybillItemsDto.getGoods();
                for (CreateWaybillGoodsDto createWaybillGoodsDto : createWaybillGoodsDtoList) {
                    WaybillGoods waybillGoods = new WaybillGoods();
                    waybillGoods.setWaybillItemId(waybillItemsId);
                    waybillGoods.setOrderGoodsId(createWaybillGoodsDto.getOrderGoodsId());
                    waybillGoods.setGoodsName(createWaybillGoodsDto.getGoodsName());
                    waybillGoods.setGoodsUnit(createWaybillGoodsDto.getGoodsUnit());
                    if (createWaybillGoodsDto.getGoodsUnit() == 3) {
                        waybillGoods.setGoodsWeight(createWaybillGoodsDto.getGoodsWeight());
                    } else {
                        waybillGoods.setGoodsQuantity(createWaybillGoodsDto.getGoodsQuantity());
                    }
                    waybillGoods.setJoinDrug(createWaybillGoodsDto.getJoinDrug());
                    waybillGoods.setModifyUserId(99999999);
                    waybillGoodsMapper.insertSelective(waybillGoods);
                    //插入order_goods_margin
                    OrderGoodsMargin orderGoodsMargin = new OrderGoodsMargin();
                    orderGoodsMargin.setOrderId(orderId);
                    orderGoodsMargin.setOrderItemId(waybillItemsDto.getOrderItemId());
                    orderGoodsMargin.setOrderGoodsId(createWaybillGoodsDto.getOrderGoodsId());
                    orderGoodsMargin.setMargin(BigDecimal.ZERO);
                    orderGoodsMarginMapper.insertSelective(orderGoodsMargin);
                }
            }

        }
        return ServiceResult.toResult("运单创建成功");
    }

    /**
     * 根据订单号获取运单列表
     *
     * @param orderId
     * @return
     */
    @Override
    public List<GetWaybillDto> listWaybillByOrderId(Integer orderId) {
        List<Waybill> waybillList = waybillMapper.selectByOrderId(orderId);
        if (waybillList == null) {
            throw new NullPointerException("当前订单无有效运单");
        }
        List<GetWaybillDto> getWaybills = new ArrayList<>();
        for (Waybill waybill : waybillList) {
            GetWaybillDto getWaybillDto = new GetWaybillDto();
            BeanUtils.copyProperties(waybill, getWaybillDto);
            getWaybills.add(getWaybillDto);
        }

        return null;
    }

    /**
     * 根据id获取waybill对象
     *
     * @param id
     * @return
     */
    @Override
    public GetWaybillDto getWaybillById(Integer id) {
        //拿到waybill对象
        Waybill waybill = waybillMapper.selectByPrimaryKey(id);
        if (null == waybill) {
            throw new NullPointerException(id + ": 无效");
        }
        //拿到装货地对象
        ShowSiteDto loadSiteDto = customerClientService.getShowSiteById(waybill.getLoadSiteId());

        //拿到车型对象
        ListTruckTypeDto truckTypeDto = null;
        if (!StringUtils.isEmpty(waybill.getNeedTruckType())) {
            truckTypeDto = truckTypeClientService.getTruckTypeById(waybill.getNeedTruckType());
        }

        ShowDriverDto showDriverDto = null;
        if (!StringUtils.isEmpty(waybill.getDriverId())) {
            showDriverDto = driverClientService.getDriverReturnObject(waybill.getDriverId());
        }
        //车辆对象
        ShowTruckDto truckDto = null;
        if (!StringUtils.isEmpty(waybill.getTruckId())) {
            truckDto = truckClientService.getTruckByIdReturnObject(waybill.getTruckId());
        }
        //获取waybillItem列表
        List<GetWaybillItemsDto> getWaybillItemsDtoList = new ArrayList<>();
        List<WaybillItems> waybillItemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybill.getId());
        for (WaybillItems items : waybillItemsList) {
            GetWaybillItemsDto getWaybillItemsDto = new GetWaybillItemsDto();
            BeanUtils.copyProperties(items, getWaybillItemsDto);
            List<ShowGoodsDto> showGoodsDtoList = new LinkedList<>();
            List<WaybillGoods> waybillGoodsList = waybillGoodsMapper.listWaybillGoodsByItemId(items.getId());
            for (WaybillGoods wg : waybillGoodsList) {
                ShowGoodsDto showGoodsDto = new ShowGoodsDto();
                BeanUtils.copyProperties(wg, showGoodsDto);
                showGoodsDtoList.add(showGoodsDto);
            }
            //拿到卸货信息
            ShowSiteDto unloadSite = customerClientService.getShowSiteById(items.getUnloadSiteId());
            getWaybillItemsDto
                    .setUnloadSite(unloadSite)
                    .setGoods(showGoodsDtoList);
            getWaybillItemsDtoList.add(getWaybillItemsDto);
        }
        GetWaybillDto getWaybillDto = new GetWaybillDto();
        BeanUtils.copyProperties(waybill, getWaybillDto);
        getWaybillDto
                .setLoadSite(loadSiteDto)
                .setNeedTruckType(truckTypeDto)
                .setTruckId(truckDto)
                .setDriverId(showDriverDto)
                .setWaybillItems(getWaybillItemsDtoList);

        return getWaybillDto;
    }

    /**
     * 根据orderId获取order和waybill信息
     * 主要用于既需要order也需要waybill的场景
     * <p>
     * 由于采用循环的方式获取值，项目紧任务中，后期时间宽裕需要重构
     *
     * @param orderId
     * @return
     */
    @Override
    public GetWaybillPlanDto getOrderAndWaybill(Integer orderId) {
        GetOrderDto getOrderDto = orderService.getOrderById(orderId);
        if (null == getOrderDto) {
            throw new NullPointerException(orderId + " :无效");
        }
        List<Integer> waybillIds = waybillMapper.listWaybillIdByOrderId(orderId);
        if (null == waybillIds) {
            throw new NullPointerException(orderId + " :当前订单无有效运单");
        }
        List<GetWaybillDto> getWaybillDtoList = new ArrayList<>(12);
        for (Integer waybillId : waybillIds) {
            GetWaybillDto getWaybillDto = this.getWaybillById(waybillId);
            getWaybillDtoList.add(getWaybillDto);
        }
        GetWaybillPlanDto getWaybillPlanDto = new GetWaybillPlanDto();
        getWaybillPlanDto
                .setOrderDto(getOrderDto)
                .setWaybillDtoList(getWaybillDtoList);
        return getWaybillPlanDto;
    }

    /**
     * 根据状态查询我的运单信息
     *
     * @param dto
     * @return
     * @author @Gao.
     */
    @Override
    public Map<String, Object> listWaybillByStatus(GetWaybillParamDto dto) {

        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser.getId();
        Waybill waybill = new Waybill();
        waybill.setDriverId(currentUserId);
        List<ListWaybillAppDto> listWaybillAppDtos;
        //承运中订单
        if (WaybillStatus.CARRIER.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByCarrierStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return ServiceResult.toResult(new PageInfo<>(listWaybillAppDtos));
        }
        //已完成运单
        if (WaybillStatus.ACCOMPLISH.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            waybill.setWaybillStatus(WaybillStatus.ACCOMPLISH);
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return ServiceResult.toResult(new PageInfo<>(listWaybillAppDtos));
        }
        //取消运单
        if (WaybillStatus.CANCEL.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            waybill.setWaybillStatus(WaybillStatus.CANCEL);
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return ServiceResult.toResult(new PageInfo<>(listWaybillAppDtos));
        }
        return ServiceResult.error("没有相关运单");
    }

    /**
     * 运单详情页
     *
     * @param waybillId
     * @return
     * @author @Gao.
     */
    @Override
    public Map<String, Object> listWayBillDetails(Integer waybillId) {
        GetWaybillDetailsAppDto waybillDetailsAppDto = new GetWaybillDetailsAppDto();
        WaybillTrackingImages waybillTrackingImages = new WaybillTrackingImages();
        Waybill waybillParam = new Waybill();
        waybillParam.setId(waybillId);
        List<GetWaybillAppDto> waybillAppDtos = waybillMapper.selectWaybillByStatus(waybillParam);
        //运单状态
        waybillDetailsAppDto.setWaybillStatus(waybillAppDtos.get(0).getWaybillStatus());
        //运单号
        waybillDetailsAppDto.setWaybillId(waybillId);
        //客户信息
        Orders orders = ordersMapper.selectByPrimaryKey(waybillAppDtos.get(0).getOrderId());
        if (null != orders) {
            ShowCustomerDto showCustomerDto = customerClientService.getShowCustomerById(orders.getCustomerId());
            waybillDetailsAppDto.setCustomer(showCustomerDto);
        }
        //装货信息
        if (null != orders) {
            ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
            waybillDetailsAppDto.setLoadSite(loadSite);
            //提货单
            waybillTrackingImages.setWaybillId(waybillId);
            waybillTrackingImages.setSiteId(orders.getLoadSiteId());
            List<GetWaybillTrackingImagesDto> loadSiteWaybillTrackingImages = waybillTrackingImagesMapper.listWaybillTrackingImage(waybillTrackingImages);
            loadSite.setWaybillTrackingImagesDtos(loadSiteWaybillTrackingImages);
        }
        //卸货信息
        List<GetWaybillItemsDto> waybillItems = waybillAppDtos.get(0).getWaybillItems();
        List<ShowSiteDto> unloadSiteList = new ArrayList<>();
        for (GetWaybillItemsDto waybillItem : waybillItems) {
            List<ShowGoodsDto> goods = waybillItem.getGoods();
            ShowSiteDto unloadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
            unloadSite.setGoods(goods);
            unloadSite.setRequiredTime(waybillItem.getRequiredTime());
            //卸货单
            waybillTrackingImages.setWaybillId(waybillId);
            waybillTrackingImages.setSiteId(waybillItem.getUnloadSiteId());
            List<GetWaybillTrackingImagesDto> waybillTrackingImagesDtosList = waybillTrackingImagesMapper.listWaybillTrackingImage(waybillTrackingImages);
            unloadSite.setWaybillTrackingImagesDtos(waybillTrackingImagesDtosList);
            unloadSiteList.add(unloadSite);

        }
        waybillDetailsAppDto.setUnloadSite(unloadSiteList);
        return ServiceResult.toResult(waybillDetailsAppDto);
    }

    /**
     * 运单轨迹
     *
     * @param waybillId
     * @return
     * @author @Gao.
     */
    @Override
    public Map<String, Object> showWaybill(Integer waybillId) {
        WaybillTrackingImages waybillTrackingImages = new WaybillTrackingImages();
        waybillTrackingImages.setWaybillId(waybillId);
        return ServiceResult.toResult(waybillTrackingImagesMapper.listWaybillTrackingImage(waybillTrackingImages));
    }

    /**
     * 接单详情列表
     *
     * @return
     * @author @Gao.
     */
    @Override
    public Map<String, Object> receiptList(GetReceiptParamDto dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Waybill waybill = new Waybill();
        List<GetReceiptListAppDto> receiptList = new ArrayList<>();
        waybill
                .setWaybillStatus(WaybillStatus.RECEIVE)
                .setTruckId(1);
        List<GetWaybillAppDto> waybillAppDtos = waybillMapper.selectWaybillByStatus(waybill);
        for (GetWaybillAppDto waybillAppDto : waybillAppDtos) {
            GetReceiptListAppDto receiptListAppDto = new GetReceiptListAppDto();
            receiptListAppDto.setWaybillId(waybillAppDto.getId());
            List<ShowGoodsDto> goodsList = new ArrayList<>();
            List<ShowSiteDto> unloadSite = new ArrayList<>();
            Orders orders = ordersMapper.selectByPrimaryKey(waybillAppDto.getOrderId());
            if (null != orders) {
                //要求提货时间
                receiptListAppDto.setLoadTime(orders.getLoadTime());
                //装货地
                ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
                receiptListAppDto.setLoadSite(loadSite);
            }

            List<GetWaybillItemsDto> waybillItems = waybillAppDto.getWaybillItems();
            for (GetWaybillItemsDto waybillItem : waybillItems) {
                //卸货地
                ShowSiteDto unLoadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
                unloadSite.add(unLoadSite);
                //货物信息
                List<ShowGoodsDto> goods = waybillItem.getGoods();
                for (ShowGoodsDto good : goods) {
                    goodsList.add(good);
                }
            }
            receiptListAppDto.setUnloadSite(unloadSite);
            receiptListAppDto.setGoods(goodsList);
            receiptList.add(receiptListAppDto);
        }
        return ServiceResult.toResult(new PageInfo<>(receiptList));
    }

    /**
     * 接单状态控制
     *
     * @param dto
     * @return
     * @author @Gao.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> upDateReceiptStatus(GetReceiptParamDto dto) {

        boolean isReceipt = false;
        Integer waybillId = dto.getWaybillId();
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        UserInfo currentUser = currentUserService.getCurrentUser();
        if (null != waybill.getDriverId()) {
            return ServiceResult.toResult("该运单已接单");
        }
        WaybillTracking waybillTracking = new WaybillTracking();
        waybillTracking
                .setWaybillId(waybillId)
                .setCreateTime(new Date())
                .setDriverId(currentUser.getId())
                .setDevice(dto.getDevice())
                .setTrackingInfo(dto.getTrackingInfo())
                .setLatitude(dto.getLatitude())
                .setLatitude(dto.getLongitude());
        //拒单
        if (WaybillStatus.REJECT.equals(dto.getReceiptStatus())) {
            waybill.setWaybillStatus(WaybillStatus.REJECT);
            waybillMapper.updateByPrimaryKey(waybill);
            waybillTracking
                    .setOldStatus(WaybillStatus.RECEIVE)
                    .setNewStatus(WaybillStatus.REJECT);
            waybillTrackingMapper.insertSelective(waybillTracking);
            return ServiceResult.toResult(isReceipt);
            //接单
        } else if (WaybillStatus.RECEIPT.equals(dto.getReceiptStatus())) {
            isReceipt = true;
            waybill.setId(waybillId);
            waybill.setDriverId(currentUser.getId());
            waybill.setWaybillStatus(WaybillStatus.DEPART);
            waybillMapper.updateByPrimaryKey(waybill);
            waybillTracking.setOldStatus(WaybillStatus.RECEIVE);
            waybillTracking.setNewStatus(WaybillStatus.DEPART);
            waybillTrackingMapper.insertSelective(waybillTracking);
            return ServiceResult.toResult(isReceipt);
        }
        return ServiceResult.toResult("接单操作失败");
    }


    /**
     * 接单消息列表显示
     *
     * @return
     * @author @Gao.
     */
    @Override
    public Map<String, Object> receiptMessage(GetReceiptMessageParamDto dto) {
        AppMessage appMessage = new AppMessage();
        appMessage.setMsgType(dto.getMsgType());
        appMessage.setTruckId(dto.getTruckId());
        appMessage.setWaybillId(dto.getWaybillId());
        return ServiceResult.toResult(appMessageMapperExt.listAppMessageByCondtion(appMessage));
    }

    /**
     * 运单列表公共方法
     *
     *
     * @return
     * @author @Gao.
     */
    private List<ListWaybillAppDto> listWaybill(List<GetWaybillAppDto> waybillDtos, Integer currentUserId) {
        List<ListWaybillAppDto> listWaybillAppDtos = new ArrayList<>();
        if (null != waybillDtos && waybillDtos.size() > 0) {
            for (GetWaybillAppDto waybillDto : waybillDtos) {
                ListWaybillAppDto listWaybillAppDto = new ListWaybillAppDto();
                List<ShowGoodsDto> showGoodsDtos = new ArrayList<>();
                List<ShowSiteDto> unloadSiteList = new ArrayList<>();
                //运单号
                listWaybillAppDto.setWaybillId(waybillDto.getId());
                //运单状态
                listWaybillAppDto.setWaybillStatus(waybillDto.getWaybillStatus());
                //接单时间
                WaybillTracking waybillTrackingCondition = new WaybillTracking();
                waybillTrackingCondition
                        .setWaybillId(waybillDto.getId())
                        .setNewStatus(WaybillStatus.DEPART)
                        .setDriverId(currentUserId);
                WaybillTracking waybillTracking = waybillTrackingMapper.selectSingleTime(waybillTrackingCondition);
                if (null != waybillTracking) {
                    listWaybillAppDto.setSingleTime(waybillTracking.getCreateTime());
                }
                //客户信息
                Orders orders = ordersMapper.selectByPrimaryKey(waybillDto.getOrderId());
                if (null != orders) {
                    ShowCustomerDto showCustomerDto = customerClientService.getShowCustomerById(orders.getCustomerId());
                    listWaybillAppDto.setCustomer(showCustomerDto);
                }
                //货物信息
                List<GetWaybillItemsDto> waybillItems = waybillDto.getWaybillItems();
                if (null != waybillItems && waybillItems.size() > 0) {
                    for (GetWaybillItemsDto waybillItem : waybillItems) {
                        if (null != waybillItem) {
                            ShowSiteDto unloadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
                            unloadSiteList.add(unloadSite);
                        }
                        List<ShowGoodsDto> goods = waybillItem.getGoods();
                        for (ShowGoodsDto good : goods) {
                            showGoodsDtos.add(good);
                        }
                    }
                }
                listWaybillAppDto.setGoods(showGoodsDtos);
                //装货地
                if (null != orders) {
                    ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
                    listWaybillAppDto.setLoadSite(loadSite);
                }
                //卸货地
                listWaybillAppDto.setUnloadSite(unloadSiteList);
                listWaybillAppDtos.add(listWaybillAppDto);
            }
        }
        return listWaybillAppDtos;
    }
}