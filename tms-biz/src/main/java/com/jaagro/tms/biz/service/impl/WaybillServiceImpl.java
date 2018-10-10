package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.driverapp.*;
import com.jaagro.tms.api.dto.order.GetOrderDto;
import com.jaagro.tms.api.dto.truck.DriverReturnDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.jpush.JpushClientUtil;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.*;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;


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
    private WaybillMapperExt waybillMapper;
    @Autowired
    private WaybillItemsMapperExt waybillItemsMapper;
    @Autowired
    private WaybillGoodsMapperExt waybillGoodsMapper;
    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private WaybillTrackingImagesMapperExt waybillTrackingImagesMapper;
    @Autowired
    private WaybillTrackingMapperExt waybillTrackingMapper;
    @Autowired
    private OrderGoodsMarginMapperExt orderGoodsMarginMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private DriverClientService driverClientService;
    @Autowired
    private TruckClientService truckClientService;
    @Autowired
    private MessageMapperExt messageMapper;
    @Autowired
    private AuthClientService authClientService;
    @Autowired
    private OssSignUrlClientService ossSignUrlClientService;
    @Autowired
    private SmsClientService smsClientService;
    @Autowired
    private UserClientService userClientService;

    /**
     * @param waybillDtoList
     * @return
     * @Author gavin
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createWaybill(List<CreateWaybillDto> waybillDtoList) {
        String departmentId = currentUserService.getCurrentUser().getDepartmentId().toString();
        if (StringUtils.isEmpty(departmentId)) {
            throw new NullPointerException("当前用户的部门为空，没有权限做运单");
        }
        Integer userId = getUserId();
        //更新orders表的状态OrderStatus.STOWAGE
        for (CreateWaybillDto createWaybillDto : waybillDtoList) {
            Integer orderId = createWaybillDto.getOrderId();
            Orders orders = new Orders();
            orders.setId(orderId);
            orders.setOrderStatus(OrderStatus.STOWAGE);
            orders.setModifyTime(new Date());
            orders.setModifyUserId(userId);
            ordersMapper.updateByPrimaryKeySelective(orders);
            break;
        }
        for (CreateWaybillDto createWaybillDto : waybillDtoList) {
            if (StringUtils.isEmpty(createWaybillDto.getLoadSiteId())) {
                throw new NullPointerException("装货地id为空");
            }
            Integer orderId = createWaybillDto.getOrderId();
            Waybill waybill = new Waybill();
            waybill.setOrderId(orderId);
            waybill.setLoadSiteId(createWaybillDto.getLoadSiteId());
            waybill.setLoadTime(createWaybillDto.getLoadTime());
            waybill.setNeedTruckType(createWaybillDto.getNeedTruckTypeId());
            waybill.setTruckTeamContractId(createWaybillDto.getTruckTeamContractId());
            waybill.setWaybillStatus(WaybillStatus.SEND_TRUCK);
            waybill.setCreateTime(new Date());
            waybill.setCreatedUserId(userId);
            waybill.setDepartmentId(currentUserService.getCurrentUser().getDepartmentId());
            waybillMapper.insertSelective(waybill);
            int waybillId = waybill.getId();
            List<CreateWaybillItemsDto> waybillItemsList = createWaybillDto.getWaybillItems();
            for (CreateWaybillItemsDto waybillItemsDto : waybillItemsList) {
                if (StringUtils.isEmpty(waybillItemsDto.getUnloadSiteId())) {
                    throw new NullPointerException("卸货地id为空");
                }
                WaybillItems waybillItem = new WaybillItems();
                waybillItem.setWaybillId(waybillId);
                waybillItem.setUnloadSiteId(waybillItemsDto.getUnloadSiteId());
                waybillItem.setRequiredTime(waybillItemsDto.getRequiredTime());
                waybillItem.setModifyUserId(userId);
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
                    waybillGoods.setModifyUserId(userId);
                    waybillGoods.setWaybillId(waybillId);
                    waybillGoodsMapper.insertSelective(waybillGoods);
                    //插入order_goods_margin
                    OrderGoodsMargin orderGoodsMargin;
                    orderGoodsMargin = orderGoodsMarginMapper.getMarginByGoodsId(createWaybillGoodsDto.getOrderGoodsId());
                    if (orderGoodsMargin == null) {
                        orderGoodsMargin = new OrderGoodsMargin();
                        orderGoodsMargin.setOrderId(orderId);
                        orderGoodsMargin.setOrderItemId(waybillItemsDto.getOrderItemId());
                        orderGoodsMargin.setOrderGoodsId(createWaybillGoodsDto.getOrderGoodsId());
                        orderGoodsMargin.setMargin(BigDecimal.ZERO);
                        orderGoodsMarginMapper.insertSelective(orderGoodsMargin);
                    } else {
                        orderGoodsMargin.setMargin(BigDecimal.ZERO);
                        orderGoodsMarginMapper.updateByPrimaryKeySelective(orderGoodsMargin);
                    }
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
        List<Waybill> waybillList = waybillMapper.listWaybillByOrderId(orderId);
        if (waybillList == null) {
            throw new NullPointerException("当前订单无有效运单");
        }
        List<GetWaybillDto> getWaybills = new ArrayList<>();
        for (Waybill waybill : waybillList) {
            GetWaybillDto getWaybillDto = new GetWaybillDto();
            BeanUtils.copyProperties(waybill, getWaybillDto);
            getWaybills.add(getWaybillDto);
        }
        return getWaybills;
    }

    /**
     * 根据id获取waybill对象
     *
     * @param id
     * @return
     * @author tony
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
        List<GetWaybillItemDto> getWaybillItemsDtoList = new ArrayList<>();
        List<WaybillItems> waybillItemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybill.getId());
        for (WaybillItems items : waybillItemsList) {
            GetWaybillItemDto getWaybillItemsDto = new GetWaybillItemDto();
            BeanUtils.copyProperties(items, getWaybillItemsDto);
            List<GetWaybillGoodsDto> getWaybillGoodsDtoList = new LinkedList<>();
            List<WaybillGoods> waybillGoodsList = waybillGoodsMapper.listWaybillGoodsByItemId(items.getId());
            for (WaybillGoods wg : waybillGoodsList) {
                GetWaybillGoodsDto getWaybillGoodsDto = new GetWaybillGoodsDto();
                BeanUtils.copyProperties(wg, getWaybillGoodsDto);
                getWaybillGoodsDtoList.add(getWaybillGoodsDto);
            }
            //拿到卸货信息
            ShowSiteDto unloadSite = customerClientService.getShowSiteById(items.getUnloadSiteId());
            getWaybillItemsDto
                    .setShowSiteDto(unloadSite)
                    .setGoods(getWaybillGoodsDtoList);
            getWaybillItemsDtoList.add(getWaybillItemsDto);
        }
        //根据waybillId获取WaybillTracking 和 WaybillTrackingImages
        List<GetTrackingDto> getTrackingDtos = new ArrayList<>();
        List<ShowTrackingDto> showTrackingDtos = waybillTrackingMapper.listWaybillTrackingByWaybillId(waybill.getId());
        for (ShowTrackingDto showTrackingDto : showTrackingDtos) {
            GetTrackingDto getTrackingDto = new GetTrackingDto();
            BeanUtils.copyProperties(showTrackingDto, getTrackingDto);
            getTrackingDtos.add(getTrackingDto);
        }
        WaybillTrackingImages record = new WaybillTrackingImages();
        record.setWaybillId(waybill.getId());
        List<GetWaybillTrackingImagesDto> getWaybillTrackingImagesDtos = waybillTrackingImagesMapper.listWaybillTrackingImage(record);

        List<GetTrackingImagesDto> getTrackingImagesDtos = new ArrayList<>();
        for (GetWaybillTrackingImagesDto getWaybillTrackingImagesDto : getWaybillTrackingImagesDtos) {
            GetTrackingImagesDto getTrackingImagesDto = new GetTrackingImagesDto();
            BeanUtils.copyProperties(getWaybillTrackingImagesDto, getTrackingImagesDto);
            getTrackingImagesDtos.add(getTrackingImagesDto);
        }

        //这个语句查询不出图片
        for (GetTrackingDto getTrackingDto : getTrackingDtos) {
            List<GetTrackingImagesDto> imageList = getTrackingImagesDtos.stream().filter(c -> c.getWaybillTrackingId().equals(getTrackingDto.getId())).collect(Collectors.toList());
            System.out.println(imageList);
            getTrackingDto.setImageList(imageList);
        }

        GetWaybillDto getWaybillDto = new GetWaybillDto();
        getWaybillDto.setTracking(getTrackingDtos);
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
        if (WaybillConstant.CARRIER.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByCarrierStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return ServiceResult.toResult(new PageInfo<>(listWaybillAppDtos));
        }
        //已完成运单
        if (WaybillConstant.ACCOMPLISH.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            waybill.setWaybillStatus(WaybillStatus.ACCOMPLISH);
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return ServiceResult.toResult(new PageInfo<>(listWaybillAppDtos));
        }
        //取消运单
        if (WaybillConstant.CANCEL.equals(dto.getWaybillStatus())) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            waybill.setWaybillStatus(WaybillStatus.CANCEL);
            List<GetWaybillAppDto> waybillDtos = waybillMapper.selectWaybillByStatus(waybill);
            listWaybillAppDtos = listWaybill(waybillDtos, currentUserId);
            return ServiceResult.toResult(new PageInfo<>(listWaybillAppDtos));
        }
        return ServiceResult.error(ResponseStatusCode.QUERY_DATA_EMPTY.getCode(), "没有相关运单");
    }

    /**
     * 运单详情页
     *
     * @param waybillId
     * @return
     * @Author @Gao.
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
        //是否需要纸质回单
        if (orders.getPaperReceipt() != null) {
            waybillDetailsAppDto.setPaperReceipt(orders.getPaperReceipt());
        }
        //装货信息
        if (null != orders) {
            ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
            ShowSiteAppDto loadSiteAppDto = new ShowSiteAppDto();
            BeanUtils.copyProperties(loadSite, loadSiteAppDto);
            //提货时间
//            Date loadTime = orders.getLoadTime();
            Date loadTime = waybillAppDtos.get(0).getLoadTime();
            loadSiteAppDto.setLoadTime(loadTime);
            //货物信息
            List<ShowGoodsDto> showGoodsDtos = new ArrayList<>();
            for (GetWaybillAppDto waybillAppDto : waybillAppDtos) {
                List<GetWaybillItemsAppDto> waybillItems = waybillAppDto.getWaybillItems();
                if (null != waybillItems && waybillItems.size() > 0) {
                    for (GetWaybillItemsAppDto waybillItem : waybillItems) {
                        List<ShowGoodsDto> goods = waybillItem.getGoods();
                        for (ShowGoodsDto good : goods) {
                            showGoodsDtos.add(good);
                        }
                    }
                }
            }
            loadSiteAppDto.setGoods(showGoodsDtos);
            //提货单
            waybillTrackingImages.setWaybillId(waybillId);
            waybillTrackingImages.setSiteId(orders.getLoadSiteId());
            List<GetWaybillTrackingImagesDto> loadSiteWaybillTrackingImages = waybillTrackingImagesMapper.listWaybillTrackingImage(waybillTrackingImages);
            for (GetWaybillTrackingImagesDto loadSiteWaybillTrackingImage : loadSiteWaybillTrackingImages) {
                //替换单据url地址
                String[] strArray = {loadSiteWaybillTrackingImage.getImageUrl()};
                List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray);
                loadSiteWaybillTrackingImage.setImageUrl(urls.get(0).toString());
            }
            loadSiteAppDto.setWaybillTrackingImagesDtos(loadSiteWaybillTrackingImages);
            waybillDetailsAppDto.setLoadSite(loadSiteAppDto);

        }
        //卸货信息
        List<GetWaybillItemsAppDto> waybillItems = waybillAppDtos.get(0).getWaybillItems();
        List<ShowSiteAppDto> unloadSiteList = new ArrayList<>();
        for (GetWaybillItemsAppDto waybillItem : waybillItems) {
            List<ShowGoodsDto> goods = waybillItem.getGoods();
            ShowSiteDto unloadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
            ShowSiteAppDto unloadSiteApp = new ShowSiteAppDto();
            BeanUtils.copyProperties(unloadSite, unloadSiteApp);
            unloadSiteApp.setGoods(goods);
            unloadSiteApp.setRequiredTime(waybillItem.getRequiredTime());
            //卸货单
            waybillTrackingImages.setWaybillId(waybillId);
            waybillTrackingImages.setSiteId(waybillItem.getUnloadSiteId());
            List<GetWaybillTrackingImagesDto> waybillTrackingImagesDtosList = waybillTrackingImagesMapper.listWaybillTrackingImage(waybillTrackingImages);
            for (GetWaybillTrackingImagesDto getWaybillTrackingImagesDto : waybillTrackingImagesDtosList) {
                //替换单据地址
                String[] strArray1 = {getWaybillTrackingImagesDto.getImageUrl()};
                List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
                getWaybillTrackingImagesDto.setImageUrl(urls.get(0).toString());
            }
            unloadSiteApp.setWaybillTrackingImagesDtos(waybillTrackingImagesDtosList);
            unloadSiteList.add(unloadSiteApp);

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
    public Map<String, Object> showWaybillTrucking(Integer waybillId) {
        ShowWaybillTrackingDto showWaybillTrackingDto = new ShowWaybillTrackingDto();
        List<ShowTrackingDto> showTrackingDtos = waybillTrackingMapper.listWaybillTrackingByWaybillId(waybillId);
        showWaybillTrackingDto.setShowTrackingDtos(showTrackingDtos);
        return ServiceResult.toResult(showWaybillTrackingDto);
    }

    /**
     * 更新运单轨迹
     *
     * @param dto
     * @return
     * @author @Gao.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> upDateWaybillTrucking(GetWaybillTruckingParamDto dto) {

        Integer waybillId = dto.getWaybillId();
        UserInfo currentUser = currentUserService.getCurrentUser();
        ShowTruckDto truckByToken = truckClientService.getTruckByToken();
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        WaybillTracking waybillTracking = new WaybillTracking();
        waybillTracking
                .setWaybillId(waybillId)
                .setDriverId(currentUser.getId())
                .setDevice(dto.getDevice())
                .setTrackingInfo(dto.getTrackingInfo())
                .setLatitude(dto.getLatitude())
                .setLatitude(dto.getLongitude())
                .setCreateTime(new Date());
        //司机出发
        if (WaybillStatus.DEPART.equals(dto.getWaybillStatus())) {
            waybillTracking
                    .setNewStatus(WaybillStatus.ARRIVE_LOAD_SITE)
                    .setOldStatus(waybill.getWaybillStatus());
            waybillTrackingMapper.insert(waybillTracking);
            waybill.setWaybillStatus(WaybillStatus.ARRIVE_LOAD_SITE);
            waybillMapper.updateByPrimaryKey(waybill);
            return ServiceResult.toResult("操作成功");
        }
        //到达货厂
        if (WaybillStatus.ARRIVE_LOAD_SITE.equals(dto.getWaybillStatus())) {
            waybillTracking
                    .setNewStatus(WaybillStatus.LOAD_PRODUCT)
                    .setOldStatus(waybill.getWaybillStatus());
            waybillTrackingMapper.insert(waybillTracking);
            waybill.setWaybillStatus(WaybillStatus.LOAD_PRODUCT);
            waybillMapper.updateByPrimaryKey(waybill);
            return ServiceResult.toResult("操作成功");
        }
        //完成提货
        if (WaybillStatus.LOAD_PRODUCT.equals(dto.getWaybillStatus())) {

            List<ConfirmProductDto> confirmProductDtosList = dto.getConfirmProductDtos();
            waybillTracking
                    .setNewStatus(WaybillStatus.DELIVERY)
                    .setOldStatus(waybill.getWaybillStatus());
            waybillTrackingMapper.insert(waybillTracking);
            //更新货物信息
            for (ConfirmProductDto confirmProductDto : confirmProductDtosList) {
                WaybillGoods waybillGoods = new WaybillGoods();
                waybillGoods.setId(confirmProductDto.getWaybillGoodId());
                //更新数量
                if (confirmProductDto.getGoodsUnit() == 2) {
                    waybillGoods.setLoadQuantity(confirmProductDto.getLoadQuantity());
                    // 吨 更新重量
                } else {
                    waybillGoods.setLoadWeight(confirmProductDto.getLoadWeight());
                }
                waybillGoodsMapper.updateByPrimaryKeySelective(waybillGoods);
            }
            //批量插入提货单
            List<String> imagesUrls = dto.getImagesUrl();
            ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
            if (!CollectionUtils.isEmpty(imagesUrls)) {
                for (int i = 0; i < imagesUrls.size(); i++) {
                    WaybillTrackingImages waybillTrackingImages = new WaybillTrackingImages();
                    waybillTrackingImages
                            .setWaybillId(waybillId)
                            .setSiteId(loadSite.getId())
                            .setCreateTime(new Date())
                            .setCreateUserId(currentUser.getId())
                            .setImageUrl(imagesUrls.get(i))
                            .setWaybillTrackingId(truckByToken.getId());
                    //出库单
                    if (i == 0) {
                        waybillTrackingImages.setImageType(ImagesTypeConstant.OUTBOUND_BILL);
                    } else if (i == 1) {
                        //磅单
                        waybillTrackingImages.setImageType(ImagesTypeConstant.POUND_BILL);
                    }
                    waybillTrackingImagesMapper.insert(waybillTrackingImages);
                }
            }
            waybill.setWaybillStatus(WaybillStatus.DELIVERY);
            waybillMapper.updateByPrimaryKey(waybill);
            return ServiceResult.toResult("操作成功");
        }
        //司机送达
        if (WaybillStatus.DELIVERY.equals(dto.getWaybillStatus())) {
            waybillTracking
                    .setNewStatus(WaybillStatus.SIGN)
                    .setOldStatus(WaybillStatus.DELIVERY);
            waybillTrackingMapper.insert(waybillTracking);
            waybill.setWaybillStatus(WaybillStatus.SIGN);
            waybillMapper.updateByPrimaryKey(waybill);
        }
        //客户签收
        if (WaybillStatus.SIGN.equals(dto.getWaybillStatus())) {
            //查询出卸货地未签收的
            WaybillItems waybillItemsCondtion = new WaybillItems();
            waybillItemsCondtion
                    .setWaybillId(waybillId)
                    .setSignStatus(SignStatusConstant.UNSIGN);
            List<Map<String, Long>> unSignUnloadSite = waybillItemsMapper.listWaybillIdIdAndSignStatus(waybillItemsCondtion);
            if (!CollectionUtils.isEmpty(unSignUnloadSite)) {
                waybillTracking.setOldStatus(WaybillStatus.SIGN);
                if (unSignUnloadSite.size() == 1) {
                    waybillTracking.setNewStatus(WaybillStatus.ACCOMPLISH);

                } else {
                    waybillTracking.setNewStatus(WaybillStatus.DELIVERY);
                }
                waybillTrackingMapper.insert(waybillTracking);
                //更新卸货物信息
                List<ConfirmProductDto> unLoadSiteConfirmProductDtos = dto.getConfirmProductDtos();
                for (ConfirmProductDto unLoadSiteconfirmProductDto : unLoadSiteConfirmProductDtos) {
                    WaybillGoods waybillGoods = new WaybillGoods();
                    waybillGoods.setId(unLoadSiteconfirmProductDto.getWaybillGoodId());
                    //单位 头 更新数量
                    if (unLoadSiteconfirmProductDto.getGoodsUnit() == 2) {
                        waybillGoods.setUnloadQuantity(unLoadSiteconfirmProductDto.getUnloadQuantity());
                    } else {
                        waybillGoods.setUnloadWeight(unLoadSiteconfirmProductDto.getUnloadWeight());
                    }
                    waybillGoodsMapper.updateByPrimaryKeySelective(waybillGoods);
                }
                //批量插入卸货单
                List<String> imagesUrls = dto.getImagesUrl();
                if (!CollectionUtils.isEmpty(imagesUrls)) {
                    for (int i = 0; i < imagesUrls.size(); i++) {
                        WaybillTrackingImages waybillTrackingImages = new WaybillTrackingImages();
                        waybillTrackingImages
                                .setWaybillId(waybillId)
                                .setSiteId(unLoadSiteConfirmProductDtos.get(0).getUnLoadSiteId())
                                .setCreateTime(new Date())
                                .setCreateUserId(currentUser.getId())
                                .setImageUrl(imagesUrls.get(i))
                                .setWaybillTrackingId(truckByToken.getId());
                        //签收单
                        if (i == 0) {
                            waybillTrackingImages.setImageType(ImagesTypeConstant.SIGN_BILL);
                        } else if (i == 1) {
                            //磅单
                            waybillTrackingImages.setImageType(ImagesTypeConstant.POUND_BILL);
                        }
                        waybillTrackingImagesMapper.insert(waybillTrackingImages);
                    }
                }
                //更新该运单签收
                WaybillItems waybillItems = new WaybillItems();
                waybillItems
                        .setSignStatus(SignStatusConstant.SIGN)
                        .setId(unLoadSiteConfirmProductDtos.get(0).getWaybillItemId());
                waybillItemsMapper.updateByPrimaryKeySelective(waybillItems);
            }
            //如果运单全部签收 更改订单状态 运单状态
            if (unSignUnloadSite.size() == 1) {

                //更改运单状态
                waybill.setWaybillStatus(WaybillStatus.ACCOMPLISH);
                waybillMapper.updateByPrimaryKeySelective(waybill);
                Orders orderUpdate = new Orders();
                //更改订单状态
                orderUpdate
                        .setId(orders.getId())
                        .setOrderStatus(OrderStatus.ACCOMPLISH);
                ordersMapper.updateByPrimaryKeySelective(orderUpdate);
                return ServiceResult.toResult(SignStatusConstant.SIGN_ALL);
            }
            return ServiceResult.toResult("操作成功");
        }
        return ServiceResult.toResult("操作异常");
    }

    /**
     * 根据waybillItemId 查询卸货地货物信息
     *
     * @param
     * @return
     * @Author @Gao.
     */
    @Override
    public Map<String, Object> showGoodsByWaybillItemId(Integer waybillItemId) {
        ShowWaybillGoodDto showWaybillGoodDto = new ShowWaybillGoodDto();
        List<ShowGoodsDto> showGoodsDtos = waybillGoodsMapper.listWaybillGoodsByWaybillItemId(waybillItemId);
        showWaybillGoodDto.setShowGoodsDtos(showGoodsDtos);
        return ServiceResult.toResult(showWaybillGoodDto);
    }

    /**
     * 根据waybillId 查询装货地货物信息
     *
     * @param
     * @return
     * @Author @Gao.
     */
    @Override
    public Map<String, Object> showGoodsByWaybillId(Integer waybillId) {
        ShowLoadSiteGoodsDto showLoadSiteGoodsDto = new ShowLoadSiteGoodsDto();
        List<GetWaybillGoodsDto> waybillGoodsDtosList = waybillGoodsMapper.listGoodsByWaybillId(waybillId);
        showLoadSiteGoodsDto.setWaybillGoodsDtosList(waybillGoodsDtosList);
        return ServiceResult.toResult(showLoadSiteGoodsDto);
    }

    /**
     * 个人中心
     *
     * @return
     * @Author @Gao.
     */
    @Override
    public Map<String, Object> personalCenter() {
        ShowPersonalCenter showPersonalCenter = new ShowPersonalCenter();
        UserInfo currentUser = currentUserService.getCurrentUser();
        showPersonalCenter.setUserInfo(currentUser);
        return ServiceResult.toResult(showPersonalCenter);
    }

    /**
     * 显示货物未签收的卸货地
     *
     * @param waybillId
     * @return
     * @Author @Gao.
     */
    @Override
    public Map<String, Object> showUnloadSite(Integer waybillId) {
        ShowUnLoadSite showUnLoadSiteDto = new ShowUnLoadSite();
        List<ShowUnLoadSite> showUnLoadSites = new ArrayList<>();
        //显示未签收id
        WaybillItems waybillItemsCondtion = new WaybillItems();
        waybillItemsCondtion
                .setWaybillId(waybillId)
                .setSignStatus(SignStatusConstant.UNSIGN);
        List<Map<String, Long>> waybillIdAndUnloadSiteIdList = waybillItemsMapper.listWaybillIdIdAndSignStatus(waybillItemsCondtion);
        for (Map<String, Long> waybillIdAndUnloadSiteIdMap : waybillIdAndUnloadSiteIdList) {
            ShowUnLoadSite showUnLoadSite = new ShowUnLoadSite();
            Long waybillItemId = waybillIdAndUnloadSiteIdMap.get("waybillItemId");
            Long unloadSiteId = waybillIdAndUnloadSiteIdMap.get("unloadSiteId");
            ShowSiteDto showSite = customerClientService.getShowSiteById(unloadSiteId.intValue());
            showUnLoadSite
                    .setShowSiteDto(showSite)
                    .setWaybillItemId(waybillItemId.intValue());
            showUnLoadSites.add(showUnLoadSite);
        }
        showUnLoadSiteDto.setShowUnLoadSites(showUnLoadSites);
        return ServiceResult.toResult(showUnLoadSiteDto);
    }

    /**
     * 接单详情列表
     *
     * @return
     * @author @Gao.
     */
    @Override
    public Map<String, Object> receiptList(GetReceiptParamDto dto) {

        ShowTruckDto truckByToken;
        try {
            truckByToken = truckClientService.getTruckByToken();
        } catch (Exception e) {
            return ServiceResult.error(ResponseStatusCode.SERVER_ERROR.getCode(), "没有权限操作");
        }
        if (null != dto.getMessageId()) {
            //更新消息是否已读
            Message message = messageMapper.selectByPrimaryKey(dto.getMessageId());
            if (null != message) {
                message.setMsgStatus(MsgStatusConstant.READ);
                messageMapper.updateByPrimaryKey(message);
            }
        }
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        Waybill waybill = new Waybill();
        List<GetReceiptListAppDto> receiptList = new ArrayList<>();
        waybill
                .setWaybillStatus(WaybillStatus.RECEIVE)
                .setTruckId(truckByToken.getId())
                .setId(dto.getWaybillId());
        List<GetWaybillAppDto> waybillAppDtos = waybillMapper.selectWaybillByStatus(waybill);
        for (GetWaybillAppDto waybillAppDto : waybillAppDtos) {
            GetReceiptListAppDto receiptListAppDto = new GetReceiptListAppDto();
            receiptListAppDto.setWaybillId(waybillAppDto.getId());
            List<ShowGoodsDto> goodsList = new ArrayList<>();
            List<ShowSiteDto> unloadSite = new ArrayList<>();
            Orders orders = ordersMapper.selectByPrimaryKey(waybillAppDto.getOrderId());
            if (null != orders) {
                //要求提货时间
                receiptListAppDto.setLoadTime(waybillAppDto.getLoadTime());
                //装货地
                ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
                receiptListAppDto.setLoadSite(loadSite);
            }

            List<GetWaybillItemsAppDto> waybillItems = waybillAppDto.getWaybillItems();
            for (GetWaybillItemsAppDto waybillItem : waybillItems) {
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

        Integer waybillId = dto.getWaybillId();
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        UserInfo currentUser = currentUserService.getCurrentUser();
        if (null != waybill.getDriverId()) {
            return ServiceResult.toResult(ReceiptConstant.ALREADY_RECEIVED);
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
        if (WaybillConstant.REJECT.equals(dto.getReceiptStatus())) {
            waybill.setWaybillStatus(WaybillStatus.REJECT);
            waybillMapper.updateByPrimaryKey(waybill);
            waybillTracking
                    .setOldStatus(WaybillStatus.RECEIVE)
                    .setNewStatus(WaybillStatus.REJECT);
            waybillTrackingMapper.insertSelective(waybillTracking);
            return ServiceResult.toResult(ReceiptConstant.OPERATION_SUCCESS);
            //接单
        } else if (WaybillConstant.RECEIPT.equals(dto.getReceiptStatus())) {
            waybill.setId(waybillId);
            waybill.setDriverId(currentUser.getId());
            waybill.setWaybillStatus(WaybillStatus.DEPART);
            waybillMapper.updateByPrimaryKey(waybill);
            waybillTracking.setOldStatus(WaybillStatus.RECEIVE);
            waybillTracking.setNewStatus(WaybillStatus.DEPART);
            waybillTrackingMapper.insertSelective(waybillTracking);
            return ServiceResult.toResult(ReceiptConstant.OPERATION_SUCCESS);
        }
        return ServiceResult.toResult(ReceiptConstant.OPERATION_FAILED);
    }


    /**
     * 接单消息列表显示
     *
     * @return
     * @author @Gao.
     */
    @Override
    public Map<String, Object> receiptMessage(GetReceiptMessageParamDto dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        UserInfo currentUser = currentUserService.getCurrentUser();
        Message message = new Message();
        message.setToUserId(currentUser.getId());
        List<Message> messages = messageMapper.listMessageByCondtion(message);
        return ServiceResult.toResult(new PageInfo<>(messages));
    }

    /**
     * 运单列表公共方法
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
                List<GetWaybillItemsAppDto> waybillItems = waybillDto.getWaybillItems();
                if (null != waybillItems && waybillItems.size() > 0) {
                    for (GetWaybillItemsAppDto waybillItem : waybillItems) {
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

    /**
     * 派单
     * Author: gavin
     *
     * @param waybillId
     * @param truckId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> assignWaybillToTruck(Integer waybillId, Integer truckId) {
        Integer userId = getUserId();
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        String waybillOldStatus = waybill.getWaybillStatus();
        String waybillNewStatus = WaybillStatus.RECEIVE;
        if (null == waybill) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), waybillId + " ：id不正确");
        }
        if (null == truckClientService.getTruckByIdReturnObject(truckId)) {
            return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), truckId + " ：id不正确");
        }
        //1.更新订单状态：从已配载(STOWAGE)改为运输中(TRANSPORT)
        Orders orders = new Orders();
        orders.setId(waybill.getOrderId());
        orders.setOrderStatus(OrderStatus.TRANSPORT);
        orders.setModifyTime(new Date());
        orders.setModifyUserId(userId);
        ordersMapper.updateByPrimaryKeySelective(orders);
        //2.更新waybill
        waybill.setTruckId(truckId);
        waybill.setWaybillStatus(waybillNewStatus);
        waybill.setSendTime(new Date());
        waybill.setModifyTime(new Date());
        waybill.setModifyUserId(userId);
        waybillMapper.updateByPrimaryKeySelective(waybill);
        //3.在waybill_tracking表插入一条记录
        WaybillTracking waybillTracking = new WaybillTracking();
        waybillTracking
                .setWaybillId(waybillId)
                .setCreateTime(new Date())
                .setOldStatus(waybillOldStatus)
                .setNewStatus(waybillNewStatus)
                .setReferUserId(userId);
        waybillTrackingMapper.insertSelective(waybillTracking);

        //4.掉用Jpush接口给司机推送消息
        List<DriverReturnDto> drivers = driverClientService.listByTruckId(truckId);
        orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        //装货地
        ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
        String loadSiteName = loadSite.getSiteName();
        List<WaybillItems> waybillItems = waybillItemsMapper.listWaybillItemsByWaybillId(waybillId);
        StringBuffer unLoadSiteNames = new StringBuffer();
        for (WaybillItems waybillItem : waybillItems) {
            //卸货地
            ShowSiteDto unLoadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
            unLoadSiteNames.append(unLoadSite.getSiteName());
        }
        String alias = "";
        String msgTitle = "派单消息";
        String msgContent;
        String regId;
        for (DriverReturnDto driver : drivers) {
            Map<String, String> extraParam = new HashMap<>();
            extraParam.put("driverId", driver.getId().toString());
            extraParam.put("waybillId", waybillId.toString());
            //您有新的运单信息待接单，从｛装货地名｝到｛卸货地名1｝/｛卸货地名2｝的运单。
            msgContent = "您有新的运单信息待接单，从" + loadSiteName + "到" + unLoadSiteNames.substring(0, unLoadSiteNames.length() - 1) + "的运单。";
            regId = driver.getRegistrationId();
            JpushClientUtil.sendPush(alias, msgTitle, msgContent, regId, extraParam);
        }

        //5.在app消息表插入一条司机记录
        //6.发送短信给truckId对应的司机
        for (int i = 0; i < drivers.size(); i++) {
            DriverReturnDto driver = drivers.get(i);
            Map<String, Object> templateMap = new HashMap<>();
            templateMap.put("driverName", driver.getName());
            BaseResponse response = smsClientService.sendSMS(driver.getPhoneNumber(), "SMS_146803933", templateMap);
            log.trace("给司机发短信,driver" + i + "::::" + driver + ",短信结果:::" + response);
            System.out.println("给司机发短信,driver" + i + "::::" + driver + ",短信结果:::" + response);
            Message appMessage = new Message();
            appMessage.setReferId(waybillId);
            appMessage.setMsgType(1);
            appMessage.setMsgStatus(0);
            appMessage.setHeader(WaybillConstant.NEW__WAYBILL_FOR_RECEIVE);
            appMessage.setBody("您有新的运单信息待接单,从" + loadSiteName + "到" + unLoadSiteNames.substring(0, unLoadSiteNames.length() - 1) + "的运单。");
            appMessage.setCreateTime(new Date());
            appMessage.setCreateUserId(userId);
            appMessage.setFromUserId(userId);
            appMessage.setToUserId(driver.getId());
            messageMapper.insertSelective(appMessage);
        }
        return ServiceResult.toResult("派单成功");
    }

    /**
     * 分页查询运单
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public Map<String, Object> listWaybillByCriteria(ListWaybillCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        Set<Integer> departIds = userClientService.getDownDepartment();
        List<Integer> dids = new ArrayList<>(departIds);
        if (dids.size() != 0) {
            criteriaDto.setDepartIds(dids);
        }
        List<ListWaybillDto> listWaybillDto = waybillMapper.listWaybillByCriteria(criteriaDto);
        if (listWaybillDto != null && listWaybillDto.size() > 0) {
            for (ListWaybillDto waybillDto : listWaybillDto
            ) {
                Waybill waybill = this.waybillMapper.selectByPrimaryKey(waybillDto.getId());
                Orders orders = this.ordersMapper.selectByPrimaryKey(waybillDto.getOrderId());
                if (orders != null) {
                    ShowCustomerDto customer = this.customerClientService.getShowCustomerById(orders.getCustomerId());
                    if (customer != null) {
                        waybillDto.setCustomerName(customer.getCustomerName());
                    }
                }
                if (waybill.getCreatedUserId() != null) {
                    UserInfo userInfo = this.authClientService.getUserInfoById(waybill.getCreatedUserId(), "employee");
                    if (userInfo != null) {
                        ShowUserDto userDto = new ShowUserDto();
                        userDto.setUserName(userInfo.getName());
                        waybillDto.setCreatedUserId(userDto);
                    }
                }
                if (waybill.getTruckId() != null) {
                    waybillDto.setTruck(this.truckClientService.getTruckByIdReturnObject(waybill.getTruckId()));
                }
                if (waybill.getDriverId() != null) {
                    waybillDto.setDriver(this.driverClientService.getDriverReturnObject(waybill.getDriverId()));

                }
            }
        }
        return ServiceResult.toResult(new PageInfo<>(listWaybillDto));
    }

    private Integer getUserId() {
        UserInfo userInfo = null;
        try {
            userInfo = currentUserService.getCurrentUser();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("获取当前用户失败：currentUserService.getCurrentUser()");
            return 999999999;
        }
        if (null == userInfo) {
            return 999999999;
        } else {
            return userInfo.getId();
        }
    }
}