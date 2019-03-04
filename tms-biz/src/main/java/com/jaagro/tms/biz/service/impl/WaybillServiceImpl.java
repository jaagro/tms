package com.jaagro.tms.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.Message.CreateMessageDto;
import com.jaagro.tms.api.dto.Message.ListMessageCriteriaDto;
import com.jaagro.tms.api.dto.Message.MessageReturnDto;
import com.jaagro.tms.api.dto.account.QueryAccountDto;
import com.jaagro.tms.api.dto.base.DictionaryDto;
import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowUserDto;
import com.jaagro.tms.api.dto.customer.*;
import com.jaagro.tms.api.dto.driverapp.*;
import com.jaagro.tms.api.dto.fee.ReturnWaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillGoodsDto;
import com.jaagro.tms.api.dto.receipt.UploadReceiptImageDto;
import com.jaagro.tms.api.dto.truck.*;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.entity.ChickenImportRecord;
import com.jaagro.tms.api.service.*;
import com.jaagro.tms.biz.config.RabbitMqConfig;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.jpush.JpushClientUtil;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.*;
import com.jaagro.tms.biz.utils.PoiUtil;
import com.jaagro.tms.biz.utils.RedisLock;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author tony
 */
@Service
public class WaybillServiceImpl implements WaybillService {

    private static final Logger log = LoggerFactory.getLogger(WaybillServiceImpl.class);
    private static final int TIMEOUT = 10 * 1000;
    /**
     * 毛鸡导入屠宰日期所在列数
     */
    private static final int TRANSPORT_DAY_INDEX = 19;
    private static final String CHICKEN_IMPORT = "chicken_import_";

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
    @Autowired
    private AccountService accountService;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private WaybillCustomerFeeMapperExt waybillCustomerFeeMapperExt;
    @Autowired
    private WaybillTruckFeeMapperExt waybillTruckFeeMapperExt;
    @Autowired
    private OrderItemsService orderItemsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ChickenImportRecordMapperExt chickenImportRecordMapperExt;
    @Autowired
    private GrabWaybillRecordMapperExt grabWaybillRecordMapper;
    @Autowired
    @Qualifier(value = "objectRedisTemplate")
    private RedisTemplate<String, Object> objectRedisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 毛鸡运单导入
     * Author gavin
     *
     * @param orderId
     * @param importDtos
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean importWaybills(Integer orderId, List<ImportWaybillDto> importDtos) {
        log.info("o WaybillServiceImpl.importWaybills input size:{}", importDtos.size());
        Orders orders = ordersMapper.selectByPrimaryKey(orderId);
        Assert.notNull(orders, "订单不存在");
        if (!GoodsType.CHICKEN.equals(orders.getGoodsType())) {
            throw new RuntimeException("只能导入毛鸡订单数据");
        }
        //1.0 先删除掉所有运单
        List<Waybill> waybills = waybillMapper.listWaybillByOrderId(orderId);
        if (waybills != null) {
            for (Waybill waybill : waybills) {
                waybillMapper.removeWaybillById(waybill.getId());
            }
        }
        //1、更新订单状态为"运输中"
        Integer userId = getUserId();
        orders.setOrderStatus(OrderStatus.TRANSPORT);
        orders.setModifyTime(new Date());
        orders.setModifyUserId(userId);
        ordersMapper.updateByPrimaryKeySelective(orders);
        //
        //循环生产运单
        int successCount = 0;
        for (ImportWaybillDto importWaybillDto : importDtos) {
            //1、插入运单waybill
            Waybill waybill = new Waybill();
            waybill.setOrderId(orderId);
            waybill.setLoadSiteId(orders.getLoadSiteId());
            waybill.setLoadTime(importWaybillDto.getLoadTime());
            waybill.setNeedTruckType(importWaybillDto.getTruckTypeId());
            waybill.setTruckId(importWaybillDto.getTruckId());
            waybill.setTruckTeamContractId(importWaybillDto.getTruckTeamContractId());
            waybill.setWaybillStatus(WaybillStatus.RECEIVE);
            waybill.setSendTime(new Date());
            waybill.setCreateTime(new Date());
            waybill.setCreatedUserId(userId);
            waybill.setModifyTime(new Date());
            waybill.setModifyUserId(userId);
            waybill.setDepartmentId(currentUserService.getCurrentUser().getDepartmentId());
            waybill.setNetworkId(importWaybillDto.getLoadSiteDeptId());
            waybillMapper.insertSelective(waybill);
            int waybillId = waybill.getId();

            List<ListOrderItemsDto> orderItemsList = orderItemsService.listItemsByOrderId(orderId);
            //2、插入waybillItems、插入waybillGoods
            if (!CollectionUtils.isEmpty(orderItemsList)) {
                Assert.notNull(orderItemsList.get(0).getUnloadId(), "卸货地id为空");
                WaybillItems waybillItem = new WaybillItems();
                waybillItem.setWaybillId(waybillId);
                waybillItem.setUnloadSiteId(orderItemsList.get(0).getUnloadId());
                waybillItem.setRequiredTime(importWaybillDto.getRequiredTime());
                waybillItem.setModifyUserId(userId);
                waybillItemsMapper.insertSelective(waybillItem);
                int waybillItemsId = waybillItem.getId();

                if (!CollectionUtils.isEmpty(orderItemsList.get(0).getOrderGoodsDtoList())) {
                    GetOrderGoodsDto GetOrderGoodsDto = orderItemsList.get(0).getOrderGoodsDtoList().get(0);
                    WaybillGoods waybillGoods = new WaybillGoods();
                    waybillGoods.setWaybillId(waybillId);
                    waybillGoods.setWaybillItemId(waybillItemsId);
                    waybillGoods.setOrderGoodsId(GetOrderGoodsDto.getId());
                    waybillGoods.setGoodsName(GetOrderGoodsDto.getGoodsName());
                    waybillGoods.setGoodsUnit(GetOrderGoodsDto.getGoodsUnit());
                    waybillGoods.setJoinDrug(GetOrderGoodsDto.getJoinDrug());
                    waybillGoods.setGoodsQuantity(importWaybillDto.getGoodsQuantity());
                    waybillGoods.setModifyUserId(userId);
                    waybillGoodsMapper.insertSelective(waybillGoods);

                    //插入order_goods_margin
                    OrderGoodsMargin orderGoodsMargin;
                    orderGoodsMargin = orderGoodsMarginMapper.getMarginByGoodsId(GetOrderGoodsDto.getId());
                    if (orderGoodsMargin == null) {
                        orderGoodsMargin = new OrderGoodsMargin();
                        orderGoodsMargin.setOrderId(orderId);
                        orderGoodsMargin.setOrderItemId(orderItemsList.get(0).getId());
                        orderGoodsMargin.setOrderGoodsId(GetOrderGoodsDto.getId());
                        orderGoodsMargin.setMargin(BigDecimal.ZERO);
                        orderGoodsMarginMapper.insertSelective(orderGoodsMargin);
                    } else {
                        orderGoodsMargin.setMargin(BigDecimal.ZERO);
                        orderGoodsMarginMapper.updateByPrimaryKeySelective(orderGoodsMargin);
                    }
                }

            }

            //4.插入waybill_tracking表插入一条记录
            WaybillTracking waybillTracking = new WaybillTracking();
            waybillTracking
                    .setEnabled(true)
                    .setWaybillId(waybillId)
                    .setCreateTime(new Date())
                    .setOldStatus(WaybillStatus.SEND_TRUCK)
                    .setNewStatus(WaybillStatus.RECEIVE)
                    .setTrackingInfo("毛鸡导入自动派单 ，运单号为【" + waybillId + "】")
                    .setReferUserId(userId);
            waybillTrackingMapper.insertSelective(waybillTracking);
            //5.掉用Jpush接口给司机推送消息
            List<DriverReturnDto> drivers = driverClientService.listByTruckId(importWaybillDto.getTruckId());
            //装货地
            ShowSiteDto loadSite = customerClientService.getShowSiteById(importWaybillDto.getLoadSiteId());
            String loadSiteName = loadSite.getSiteName();

            //卸货地
            ShowSiteDto unLoadSite = customerClientService.getShowSiteById(orderItemsList.get(0).getUnloadId());
            String unloadSiteName = unLoadSite.getSiteName();
            String alias;
            String msgTitle = "派单消息";
            String msgContent;
            String regId;
            for (DriverReturnDto driver : drivers) {
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("driverId", driver.getId().toString());
                extraParam.put("waybillId", waybillId + "");
                extraParam.put("needVoice", "y");
                //您有新的运单信息待接单，从｛装货地名｝到｛卸货地名1｝/｛卸货地名2｝的运单。
                msgContent = "您有新的健安运单待接单，从" + loadSiteName + "到" + unloadSiteName + "的运单。";
                alias = driver.getPhoneNumber();
                regId = driver.getRegistrationId();
                JpushClientUtil.sendPush(alias, msgTitle, msgContent, regId, extraParam);

                Message appMessage = new Message();
                appMessage.setReferId(waybillId);
                // 消息类型：1-系统通知 2-运单相关 3-账务相关
                appMessage.setMsgType(MsgType.WAYBILL);
                //消息来源:1-APP,2-小程序,3-站内
                appMessage.setMsgSource(MsgSource.APP);
                appMessage.setMsgStatus(MsgStatusConstant.UNREAD);
                appMessage.setHeader(WaybillConstant.NEW__WAYBILL_FOR_RECEIVE);
                appMessage.setBody("您有新的健安运单待接单,从" + loadSiteName + "到" + unloadSiteName + "的运单。");
                appMessage.setCreateTime(new Date());
                appMessage.setCreateUserId(userId);
                appMessage.setFromUserId(userId);
                appMessage.setToUserId(driver.getId());
                messageMapper.insertSelective(appMessage);
            }
            successCount++;
        }

        log.info("o WaybillServiceImpl.importWaybills output size:{}", successCount);

        return true;
    }

    /**
     * 根据司机id统计未完成的运单
     *
     * @param driverId
     * @return
     */
    @Override
    public Integer countUnFinishWaybillByDriver(Integer driverId) {
        return waybillMapper.countUnDoneByDriverId(driverId);
    }

    /**
     * 修改不正确的毛鸡导入记录
     *
     * @param dto
     * @return
     */
    @Override
    public List<ChickenImportRecordDto> changeImportChickenRecord(UpdateChickenImportRecordDto dto) {
        Orders orders = ordersMapper.selectByPrimaryKey(dto.getOrderId());
        HashOperations<String, Object, Object> opsForHash = objectRedisTemplate.opsForHash();
        String key = CHICKEN_IMPORT + dto.getOrderId();
        Object object = opsForHash.get(key, dto.getSerialNumber().toString());
        if (object != null) {
            ChickenImportRecordDto chickenImportRecordDto = (ChickenImportRecordDto) object;
            BaseResponse<GetTruckDto> res = truckClientService.getByTruckNumber(dto.getTruckNumber());
            GetTruckDto truckDto = res.getData();
            if (res != null && truckDto != null) {
                ListTruckTypeDto truckTypeDto = truckDto.getTruckTypeId();
                boolean checkTruckType = truckTypeDto != null && ProductName.CHICKEN.toString().equals(truckTypeDto.getProductName()) && chickenImportRecordDto.getGoodsQuantity() != null && chickenImportRecordDto.getGoodsQuantity().toString().equals(truckTypeDto.getTruckAmount());
                if (checkTruckType) {
                    chickenImportRecordDto.setVerifyPass(true);
                }
                chickenImportRecordDto.setTruckNumber(dto.getTruckNumber());
                chickenImportRecordDto.setTruckId(truckDto.getId());
                // 获取车队合同id
                chickenImportRecordDto.setTruckTeamContractId(getTruckTeamContractId(orders.getGoodsType(), truckDto.getTruckTeamId()));
                opsForHash.put(key, dto.getSerialNumber().toString(), chickenImportRecordDto);
            }
        }
        Map<Object, Object> entries = opsForHash.entries(key);
        if (CollectionUtils.isEmpty(entries)) {
            throw new RuntimeException("操作超时了,请重新导入");
        }
        return getChickenImportRecordDtoListFromMap(entries);
    }

    /**
     * 查询未完成的运单 提供给删除报价
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public Integer countUnFinishWaybillByContract(CountUnFinishWaybillCriteriaDto criteriaDto) {
        return waybillItemsMapper.countUnFinishWaybillByContract(criteriaDto);
    }

    /**
     * 客户费用
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo listWaybillCustomerFee(ListWaybillCustomerFeeDto dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        //项目部隔离

        List<ReturnWaybillCustomerFeeDto> customerFeeDtoList = waybillCustomerFeeMapperExt.listWaybillCustomerFee(dto);
        if (!CollectionUtils.isEmpty(customerFeeDtoList)) {

        }
        return null;
    }

    /**
     * @param waybillDtoList
     * @return
     * @Author gavins
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
            ShowSiteDto showSiteDto = customerClientService.getShowSiteById(createWaybillDto.getLoadSiteId());
            if (showSiteDto == null) {
                throw new RuntimeException("装货地不存在");
            }
            Integer orderId = createWaybillDto.getOrderId();
            Waybill waybill = new Waybill();
            waybill.setOrderId(orderId);
            waybill.setLoadSiteId(createWaybillDto.getLoadSiteId());
            waybill.setLoadTime(createWaybillDto.getLoadTime());
            waybill.setNeedTruckType(createWaybillDto.getNeedTruckTypeId());
            waybill.setWaybillStatus(WaybillStatus.SEND_TRUCK);
            waybill.setCreateTime(new Date());
            waybill.setCreatedUserId(userId);
            waybill.setDepartmentId(currentUserService.getCurrentUser().getDepartmentId());
            waybill.setNetworkId(showSiteDto.getDeptId());
            waybillMapper.insertSelective(waybill);
            int waybillId = waybill.getId();
            List<CreateWaybillItemsDto> waybillItemsList = createWaybillDto.getWaybillItems();

            for (int i = 0; i < waybillItemsList.size(); i++) {

                CreateWaybillItemsDto waybillItemsDto = waybillItemsList.get(i);
                if (StringUtils.isEmpty(waybillItemsDto.getUnloadSiteId())) {
                    throw new NullPointerException("卸货地id为空");
                }
                int waybillItemsId = 0;
                if (i == 0) {
                    WaybillItems waybillItem = new WaybillItems();
                    waybillItem.setWaybillId(waybillId);
                    waybillItem.setUnloadSiteId(waybillItemsDto.getUnloadSiteId());
                    waybillItem.setRequiredTime(waybillItemsDto.getRequiredTime());
                    waybillItem.setModifyUserId(userId);
                    waybillItemsMapper.insertSelective(waybillItem);
                    waybillItemsId = waybillItem.getId();
                } else {
                    List<WaybillItems> waybillItemsDoList = waybillItemsMapper.listWaybillItemsByWaybillId(waybillId);
                    List<WaybillItems> list = waybillItemsDoList.stream().filter(c -> c.getUnloadSiteId().equals(waybillItemsDto.getUnloadSiteId())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(list)) {
                        WaybillItems waybillItem = new WaybillItems();
                        waybillItem.setWaybillId(waybillId);
                        waybillItem.setUnloadSiteId(waybillItemsDto.getUnloadSiteId());
                        waybillItem.setRequiredTime(waybillItemsDto.getRequiredTime());
                        waybillItem.setModifyUserId(userId);
                        waybillItemsMapper.insertSelective(waybillItem);
                        waybillItemsId = waybillItem.getId();
                    } else {
                        waybillItemsId = list.get(0).getId();
                    }
                }

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
            //删掉无计划时指定的默认卸货地
            if (items.getUnloadSiteId() == 0) {
                continue;
            }
            GetWaybillItemDto getWaybillItemsDto = new GetWaybillItemDto();
            BeanUtils.copyProperties(items, getWaybillItemsDto);
            List<GetWaybillGoodsDto> getWaybillGoodsDtoList = new LinkedList<>();
            List<WaybillGoods> waybillGoodsList = waybillGoodsMapper.listWaybillGoodsByItemId(items.getId());
            if (null == getWaybillItemsDto.getTotalQuantity()) {
                getWaybillItemsDto.setTotalQuantity(0);
            }
            if (null == getWaybillItemsDto.getTotalWeight()) {
                getWaybillItemsDto.setTotalWeight(new BigDecimal(0));
            }
            for (WaybillGoods wg : waybillGoodsList) {
                GetWaybillGoodsDto getWaybillGoodsDto = new GetWaybillGoodsDto();
                BeanUtils.copyProperties(wg, getWaybillGoodsDto);
                getWaybillGoodsDtoList.add(getWaybillGoodsDto);
                if (null == wg.getGoodsQuantity()) {
                    wg.setGoodsQuantity(0);
                }
                if (null == wg.getGoodsWeight()) {
                    wg.setGoodsWeight(new BigDecimal(0));
                }
                getWaybillItemsDto
                        .setTotalQuantity(getWaybillItemsDto.getTotalQuantity() + wg.getGoodsQuantity())
                        .setTotalWeight(getWaybillItemsDto.getTotalWeight().add(wg.getGoodsWeight()));
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
        List<ShowTrackingDto> showTrackingDtos = waybillTrackingMapper.getWaybillTrackingByWaybillId(waybill.getId());
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
            for (GetTrackingImagesDto getTrackingImagesDto : imageList) {
                String[] strArray = {getTrackingImagesDto.getImageUrl()};
                List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray);
                getTrackingImagesDto.setImageUrl(urls.get(0).toString());
            }
            // 图片排序 add by jia.yu 20181129
            Collections.sort(imageList, Comparator.comparingInt(GetTrackingImagesDto::getImageType));
            getTrackingDto.setImageList(imageList);
        }
        Orders ordersData = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        GetWaybillDto getWaybillDto = new GetWaybillDto();
        getWaybillDto.setTracking(getTrackingDtos);
        BeanUtils.copyProperties(waybill, getWaybillDto);
        getWaybillDto
                .setLoadSite(loadSiteDto)
                .setNeedTruckType(truckTypeDto)
                .setTruckId(truckDto)
                .setDriverId(showDriverDto)
                .setWaybillItems(getWaybillItemsDtoList)
                .setGoodType(ordersData.getGoodsType())
                .setTotalQuantity(getWaybillDto.getWaybillItems().stream().mapToInt(GetWaybillItemDto::getTotalQuantity).sum())
                .setTotalWeight(getWaybillDto.getWaybillItems().stream().map(GetWaybillItemDto::getTotalWeight).reduce(BigDecimal.ZERO, BigDecimal::add));

        //订单的客户20181207 begin
        ShowCustomerDto customerDto = customerClientService.getShowCustomerById(ordersData.getCustomerId());

        //客户的联系人
        CustomerContactsReturnDto customerContactsDto = customerClientService.getCustomerContactByCustomerId(ordersData.getCustomerId());
        getWaybillDto.setCustomerDto(customerDto);
        getWaybillDto.setCustomerContactsDto(customerContactsDto);
        //20181207 end
        //20190128 如果运单已经回单补录，详情显示状态为"已补录"
        if (waybill.getWaybillStatus().equals(WaybillStatus.ACCOMPLISH) && waybill.getReceiptStatus() == 2) {
            getWaybillDto.setWaybillStatus(WaybillStatus.UNLOAD_RECEIPT);
        }
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
            GrabWaybillRecord grabWaybill = grabWaybillRecordMapper.getGrabWaybillByWaybillId(waybillId);
            if (grabWaybill != null) {
                getWaybillDto.setGrabWaybillStatus(true);
            }
            //运单状态为已拒单填充拒单理由(取时间倒序 limit 1)
            if (getWaybillDto.getWaybillStatus().equals(WaybillStatus.REJECT)) {
                GetRefuseTrackingDto trackingDto = new GetRefuseTrackingDto();
                WaybillTracking waybillTracking = waybillTrackingMapper.getRefuseTrackingByWaybillId(getWaybillDto.getId());
                if (waybillTracking != null) {
                    BeanUtils.copyProperties(waybillTracking, trackingDto);
                    if (waybillTracking.getRefuseReasonId() == null) {
                        trackingDto.setRefuseReason("系统自动拒单");
                    } else {
                        DictionaryDto dictionaryDto = customerClientService.getDictionaryById(waybillTracking.getRefuseReasonId());
                        if (dictionaryDto != null) {
                            trackingDto.setRefuseReason(dictionaryDto.getTypeName());
                        }
                    }
                    getWaybillDto.setRefuseDto(trackingDto);
                }
            }
            getWaybillDtoList.add(getWaybillDto);
        }
        GetWaybillPlanDto getWaybillPlanDto = new GetWaybillPlanDto();
        getWaybillPlanDto
                .setOrderDto(getOrderDto)
                .setWaybillDtoList(getWaybillDtoList);
        return getWaybillPlanDto;
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
        if (!CollectionUtils.isEmpty(waybillAppDtos) && null != waybillAppDtos.get(0)) {
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
                Date loadTime = waybillAppDtos.get(0).getLoadTime();
                loadSiteAppDto.setLoadTime(loadTime);
                //货物信息
                List<ShowGoodsDto> showGoodsDtos = new ArrayList<>();
                for (GetWaybillAppDto waybillAppDto : waybillAppDtos) {
                    List<GetWaybillItemsAppDto> waybillItems = waybillAppDto.getWaybillItems();
                    if (null != waybillItems && waybillItems.size() > 0) {
                        for (GetWaybillItemsAppDto waybillItem : waybillItems) {
                            //删掉无计划时指定的默认卸货地和货物
                            if (waybillItem.getUnloadSiteId() == 0) {
                                continue;
                            }
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
                waybillTrackingImages.setType(1);
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
                //删掉无计划时指定的默认卸货地
                if (waybillItem.getUnloadSiteId() == 0) {
                    continue;
                }
                List<ShowGoodsDto> goods = waybillItem.getGoods();
                ShowSiteDto unloadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
                ShowSiteAppDto unloadSiteApp = new ShowSiteAppDto();
                BeanUtils.copyProperties(unloadSite, unloadSiteApp);
                unloadSiteApp.setGoods(goods);
                unloadSiteApp.setRequiredTime(waybillItem.getRequiredTime());
                //卸货单
                waybillTrackingImages.setWaybillId(waybillId);
                waybillTrackingImages.setSiteId(waybillItem.getUnloadSiteId());
                waybillTrackingImages.setType(2);
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
        }
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
    public ShowWaybillTrackingDto showWaybillTrucking(Integer waybillId) {
        ShowWaybillTrackingDto showWaybillTrackingDto = new ShowWaybillTrackingDto();
        List<ShowTrackingDto> showTrackingDtos = waybillTrackingMapper.listWaybillTrackingByWaybillId(waybillId);
        // 过滤非运输轨迹 add by yj 20181115
        Iterator<ShowTrackingDto> iterator = showTrackingDtos.iterator();
        while (iterator.hasNext()) {
            ShowTrackingDto showTrackingDto = iterator.next();
            if (!TrackingType.TRANSPORT.equals(showTrackingDto.getTrackingType())) {
                iterator.remove();
            }
        }
        showWaybillTrackingDto.setShowTrackingDtos(showTrackingDtos);
        return showWaybillTrackingDto;
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
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
        waybill.setModifyTime(new Date());
        WaybillTracking waybillTracking = new WaybillTracking();
        waybillTracking
                .setWaybillId(waybillId)
                .setDriverId(currentUser.getId())
                .setDevice(dto.getDevice())
                .setTrackingInfo(dto.getTrackingInfo())
                .setLatitude(dto.getLatitude())
                .setLatitude(dto.getLongitude())
                .setCreateTime(new Date())
                .setEnabled(true);
        //司机出发
        if (WaybillStatus.DEPART.equals(dto.getWaybillStatus())) {
            Waybill wb = new Waybill();
            wb.setDriverId(currentUser.getId());
            List<ListWaybillDto> waybills = waybillMapper.listCriteriaWaybill(wb);
            if (!CollectionUtils.isEmpty(waybills)) {
                return ServiceResult.toResult(SignStatusConstant.CRITERIA);
            }
            waybillTracking
                    .setNewStatus(WaybillStatus.ARRIVE_LOAD_SITE)
                    .setOldStatus(waybill.getWaybillStatus())
                    .setTrackingInfo("司机【" + currentUser.getName() + "】已出发");
            waybillTrackingMapper.insertSelective(waybillTracking);
            waybill.setWaybillStatus(WaybillStatus.ARRIVE_LOAD_SITE);
            waybillMapper.updateByPrimaryKey(waybill);
            return ServiceResult.toResult("操作成功");
        }
        //到达货厂
        if (WaybillStatus.ARRIVE_LOAD_SITE.equals(dto.getWaybillStatus())) {
            waybillTracking
                    .setNewStatus(WaybillStatus.LOAD_PRODUCT)
                    .setOldStatus(waybill.getWaybillStatus())
                    .setTrackingInfo("已到达提货地【" + loadSite.getSiteName() + "】");
            waybillTrackingMapper.insertSelective(waybillTracking);
            waybill.setWaybillStatus(WaybillStatus.LOAD_PRODUCT);
            waybillMapper.updateByPrimaryKey(waybill);
            return ServiceResult.toResult("操作成功");
        }
        //完成提货
        if (WaybillStatus.LOAD_PRODUCT.equals(dto.getWaybillStatus())) {

            List<ConfirmProductDto> confirmProductDtosList = dto.getConfirmProductDtos();
            waybillTracking
                    .setNewStatus(WaybillStatus.DELIVERY)
                    .setOldStatus(waybill.getWaybillStatus()).setTrackingInfo("在【" + loadSite.getSiteName() + "】已提货");
            waybillTrackingMapper.insertSelective(waybillTracking);
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

            if (!CollectionUtils.isEmpty(dto.getWaybillImagesUrl())) {
                //批量插入提货单
                List<WaybillImagesUrlDto> imagesUrls = dto.getWaybillImagesUrl();
                List<String> urlList = new ArrayList<>();
                for (int i = 0; i < imagesUrls.size(); i++) {
                    WaybillImagesUrlDto waybillImagesUrl = imagesUrls.get(i);
                    WaybillTrackingImages waybillTrackingImages = new WaybillTrackingImages();
                    waybillTrackingImages
                            .setWaybillId(waybillId)
                            .setSiteId(loadSite.getId())
                            .setCreateTime(new Date())
                            .setCreateUserId(currentUser.getId())
                            .setImageUrl(waybillImagesUrl.getImagesUrl())
                            .setWaybillTrackingId(waybillTracking.getId());
                    //出库单
                    if (ImagesTypeConstant.OUTBOUND_BILL.equals(waybillImagesUrl.getImagesType())) {
                        waybillTrackingImages.setImageType(ImagesTypeConstant.OUTBOUND_BILL);
                    }
                    if (ImagesTypeConstant.POUND_BILL.equals(waybillImagesUrl.getImagesType())) {
                        //磅单
                        waybillTrackingImages.setImageType(ImagesTypeConstant.POUND_BILL);
                        urlList.add(waybillImagesUrl.getImagesUrl());
                    }
                    if (!"invalidPicUrl".equalsIgnoreCase(waybillImagesUrl.getImagesUrl())) {
                        waybillTrackingImagesMapper.insertSelective(waybillTrackingImages);
                    }
                }
                //****************gavin *牧源绿色磅单图片识别begin
                if (orders.getCustomerId() == 248 && !CollectionUtils.isEmpty(urlList)) {
                    try {
                        Map<String, String> map = new HashMap<>(16);
                        map.put("waybillId", waybillId.toString());
                        map.put("imageUrl", urlList.get(0));
                        amqpTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE, "muyuan.ocr", map);
                    } catch (Exception e) {
                        log.info("O upDateWaybillTrucking 图片识别失败，{}", waybillId);
                    }
                }
                //****************gavin *牧源绿色磅单图片识别end
            }
            waybill.setWaybillStatus(WaybillStatus.DELIVERY);
            waybillMapper.updateByPrimaryKey(waybill);
            return ServiceResult.toResult("操作成功");
        }
        //司机送达
        if (WaybillStatus.DELIVERY.equals(dto.getWaybillStatus())) {
            waybillTracking
                    .setNewStatus(WaybillStatus.SIGN)
                    .setOldStatus(WaybillStatus.DELIVERY)
                    .setTrackingInfo("司机已到达卸货地");
            waybillTrackingMapper.insertSelective(waybillTracking);
            waybill.setWaybillStatus(WaybillStatus.SIGN);
            waybillMapper.updateByPrimaryKey(waybill);
        }
        //客户签收
        if (WaybillStatus.SIGN.equals(dto.getWaybillStatus())) {
            List<ConfirmProductDto> unLoadSiteConfirmProductDtos = dto.getConfirmProductDtos();
            ShowSiteDto showSiteById = null;
            if (!CollectionUtils.isEmpty(unLoadSiteConfirmProductDtos) && null != unLoadSiteConfirmProductDtos.get(0)) {
                showSiteById = customerClientService.getShowSiteById(unLoadSiteConfirmProductDtos.get(0).getUnLoadSiteId());
            }

            //查询出卸货地未签收的
            WaybillItems waybillItemsCondition = new WaybillItems();
            waybillItemsCondition
                    .setWaybillId(waybillId)
                    .setSignStatus(SignStatusConstant.UNSIGN);
            List<Map<String, Long>> unSignUnloadSite = waybillItemsMapper.listWaybillIdIdAndSignStatus(waybillItemsCondition);
            if (!CollectionUtils.isEmpty(unSignUnloadSite)) {
                waybillTracking.setOldStatus(WaybillStatus.SIGN);
                if (unSignUnloadSite.size() == 1) {
                    waybillTracking
                            .setNewStatus(WaybillStatus.ACCOMPLISH)
                            .setTrackingInfo("已签收，签收地为【" + showSiteById.getSiteName() + "】");
                } else {
                    waybillTracking
                            .setNewStatus(WaybillStatus.DELIVERY)
                            .setTrackingInfo("已签收，签收地为【" + showSiteById.getSiteName() + "】");
                }
                waybillTrackingMapper.insertSelective(waybillTracking);
                //更新卸货物信息
                for (ConfirmProductDto unLoadSiteConfirmProductDto : unLoadSiteConfirmProductDtos) {
                    WaybillGoods waybillGoods = new WaybillGoods();
                    waybillGoods.setId(unLoadSiteConfirmProductDto.getWaybillGoodId());
                    //单位 头 更新数量
                    if (unLoadSiteConfirmProductDto.getGoodsUnit() == 2) {
                        waybillGoods.setUnloadQuantity(unLoadSiteConfirmProductDto.getUnloadQuantity());
                    } else {
                        waybillGoods.setUnloadWeight(unLoadSiteConfirmProductDto.getUnloadWeight());
                    }
                    waybillGoodsMapper.updateByPrimaryKeySelective(waybillGoods);
                }
                //批量插入卸货单
                if (!CollectionUtils.isEmpty(dto.getWaybillImagesUrl())) {
                    List<WaybillImagesUrlDto> imagesUrls = dto.getWaybillImagesUrl();
                    for (int i = 0; i < imagesUrls.size(); i++) {
                        WaybillImagesUrlDto waybillImagesUrl = imagesUrls.get(i);
                        WaybillTrackingImages waybillTrackingImages = new WaybillTrackingImages();
                        waybillTrackingImages
                                .setWaybillId(waybillId)
                                .setSiteId(unLoadSiteConfirmProductDtos.get(0).getUnLoadSiteId())
                                .setCreateTime(new Date())
                                .setCreateUserId(currentUser.getId())
                                .setImageUrl(waybillImagesUrl.getImagesUrl())
                                .setWaybillTrackingId(waybillTracking.getId());
                        //签收单
                        if (ImagesTypeConstant.SIGN_BILL.equals(waybillImagesUrl.getImagesType())) {
                            waybillTrackingImages.setImageType(ImagesTypeConstant.SIGN_BILL);
                        }
                        if (ImagesTypeConstant.POUND_BILL.equals(waybillImagesUrl.getImagesType())) {
                            //磅单
                            waybillTrackingImages.setImageType(ImagesTypeConstant.POUND_BILL);
                        }
                        if (!"invalidPicUrl".equalsIgnoreCase(waybillImagesUrl.getImagesUrl())) {
                            waybillTrackingImagesMapper.insertSelective(waybillTrackingImages);
                        }
                    }
                }
                //更新该运单签收
                WaybillItems waybillItems = new WaybillItems();
                waybillItems
                        .setSignStatus(SignStatusConstant.SIGN)
                        .setId(unLoadSiteConfirmProductDtos.get(0).getWaybillItemId())
                        .setModifyTime(new Date());
                waybillItemsMapper.updateByPrimaryKeySelective(waybillItems);
            }
            //如果运单全部签收 运单状态
            if (unSignUnloadSite.size() == 1) {
                updateContractOfWaybillAndOrders(waybill, orders);
                //更改运单状态
                waybill
                        .setWaybillStatus(WaybillStatus.ACCOMPLISH)
                        .setModifyTime(new Date());
                waybillMapper.updateByPrimaryKeySelective(waybill);
                ShowCustomerDto customerDto = customerClientService.getShowCustomerById(orders.getCustomerId());
                //磅单超过千分之二 进行于预警判断
                boolean flag = !StringUtils.isEmpty(customerDto.getEnableDirectOrder()) && "y".equals(customerDto.getEnableDirectOrder());
                //牧原客户手机提交 不做磅差判断
                if (!flag) {
                    pounderAlert(waybillId, true);
                }
                //判断当前订单 下的运单是否全部签收 如果全部签收 更新订单状态
                List<Waybill> waybills = waybillMapper.listWaybillByOrderId(waybill.getOrderId());
                int count = 0;
                for (Waybill w : waybills) {
                    if ("已完成".equals(w.getWaybillStatus()) || "已作废".equals(w.getWaybillStatus())) {
                        count++;
                    }
                }
                if (waybills.size() == count) {
                    Orders orderUpdate = new Orders();
                    //更改订单状态
                    orderUpdate
                            .setId(orders.getId())
                            .setModifyTime(new Date())
                            .setOrderStatus(OrderStatus.ACCOMPLISH);
                    ordersMapper.updateByPrimaryKeySelective(orderUpdate);
                } else {
                    log.debug("当前订单下的运单未全部操作完毕，不修改状态");
                }
                return ServiceResult.toResult(SignStatusConstant.SIGN_ALL);
            }

            return ServiceResult.toResult("操作成功");
        }
        return ServiceResult.toResult("操作异常");
    }

    /**
     * 运单全部签收完成时，运单的运力合同id更新到最新，订单的客户合同id更新到最新
     * Gavin
     *
     * @param waybill
     * @param orders
     */
    private void updateContractOfWaybillAndOrders(Waybill waybill, Orders orders) {
        ContractDto customerContractDto = new ContractDto();
        ContractDto truckTeamContractDto = new ContractDto();
        try {
            Date date = new Date();
            //1、更新订单的最新有效的客户合同id
            customerContractDto.setContractType(1)
                    .setCustomerId(orders.getCustomerId())
                    .setGoodsType(orders.getGoodsType())
                    .setWaybillDoneDate(date);

            customerContractDto = truckClientService.getContractByContractDto(customerContractDto);
            if (null != customerContractDto && null != customerContractDto.getId()) {
                orders.setCustomerContractId(customerContractDto.getId());
                ordersMapper.updateByPrimaryKeySelective(orders);
            }
            //2、更新运单的最新运力合同id
            ShowTruckDto truckDto = truckClientService.getTruckByIdReturnObject(waybill.getTruckId());
            if (null != truckDto && null != truckDto.getTruckTeamId()) {
                truckTeamContractDto.setContractType(2)
                        .setTruckTeamId(truckDto.getTruckTeamId())
                        .setGoodsType(orders.getGoodsType())
                        .setWaybillDoneDate(date);

                truckTeamContractDto = truckClientService.getContractByContractDto(truckTeamContractDto);
                if (null != truckTeamContractDto && null != truckTeamContractDto.getId()) {
                    waybill.setTruckTeamContractId(truckTeamContractDto.getId());
                    waybillMapper.updateByPrimaryKeySelective(waybill);
                }
            }
        } catch (Exception ex) {
            log.error("R waybillServiceImpl.updateContractOfWaybillAndOrders() failed waybill=" + waybill + "orders=" + orders, ex);
        }
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
    public ShowPersonalCenter personalCenter() {
        try {
            ShowPersonalCenter showPersonalCenter = new ShowPersonalCenter();
            UserInfo currentUser = currentUserService.getCurrentUser();
            showPersonalCenter.setUserInfo(currentUser);
            // 设置账户信息 add by yj 20181112
            QueryAccountDto queryAccountDto = new QueryAccountDto();
            queryAccountDto
                    .setAccountType(AccountType.CASH)
                    .setUserId(currentUser == null ? null : currentUser.getId())
                    .setUserType(AccountUserType.DRIVER);
            showPersonalCenter.setAccountInfo(accountService.getByQueryAccountDto(queryAccountDto));
            // 我的驾驶证信息 add by @Gao. 20181204
            ListDriverLicenseDto listDriverLicenseDto = new ListDriverLicenseDto();
            ShowDriverDto driver = driverClientService.getDriverReturnObject(currentUser == null ? null : currentUser.getId());
            if (null != driver) {
                listDriverLicenseDto
                        .setStatus(expiryDrivingLicenseIsNormal(driver))
                        .setIdentityCard(driver.getIdentityCard())
                        .setDrivingLicense(driver.getDrivingLicense())
                        .setValidityInspection(dateToString(stringToDate(driver.getExpiryDrivingLicense())))
                        .setExpiryDrivingLicense(dateToString(stringToDate(driver.getExpiryDrivingLicense())))
                        .setAllocationTime(allocationTime(driver.getExpiryDrivingLicense()));
                showPersonalCenter.setDriverLicenseDto(listDriverLicenseDto);
            }
            // 我的车辆证照信息
            ListTruckLicenseDto listTruckLicenseDto = new ListTruckLicenseDto();
            ShowTruckDto truckByToken = truckClientService.getTruckByToken();
            if (truckByToken != null) {
                listTruckLicenseDto
                        .setTruckNumber(truckByToken.getTruckNumber())
                        .setBuyTime(truckByToken.getBuyTime() == null ? null : dateFormat(truckByToken.getBuyTime()))
                        .setExpiryDate(truckByToken.getExpiryDate() == null ? null : dateFormat(truckByToken.getExpiryDate()))
                        .setExpiryAnnual(truckByToken.getExpiryAnnual() == null ? null : dateFormat(truckByToken.getExpiryAnnual()))
                        .setTruckStatus(truckIsNormal(truckByToken));
            }
            showPersonalCenter.setTruckLicenseDto(listTruckLicenseDto);
            // 车辆信息 add by jia.yu
            showPersonalCenter.setTruckInfo(getTruckInfo(truckByToken));
            return showPersonalCenter;
        } catch (Exception ex) {
            log.info("personalCenter", ex);
            return new ShowPersonalCenter();
        }

    }

    private ShowTruckInfoDto getTruckInfo(ShowTruckDto truckByToken) {
        if (truckByToken != null) {
            ShowTruckInfoDto showTruckDto = new ShowTruckInfoDto();
            BeanUtils.copyProperties(truckByToken, showTruckDto);
            showTruckDto.setTruckId(truckByToken.getId());
            if (!CollectionUtils.isEmpty(truckByToken.getDrivers())) {
                UserInfo currentUser = currentUserService.getCurrentUser();
                if (currentUser != null) {
                    for (ShowDriverDto driverDto : truckByToken.getDrivers()) {
                        if (currentUser.getId().equals(driverDto.getId())) {
                            BeanUtils.copyProperties(driverDto, showTruckDto);
                            showTruckDto.setDriverId(driverDto.getId())
                                    .setDriverName(driverDto.getName())
                                    .setMainDriver(driverDto.getMaindriver());
                        }
                    }
                }
                return showTruckDto;
            }
        }
        return null;
    }

    /**
     * 驾驶证清分时间
     *
     * @param expiryDate
     * @return
     * @Author @Gao.
     */
    private static String allocationTime(String expiryDate) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthDayFormat = new SimpleDateFormat("MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = null;
        if (null != expiryDate) {
            String currentStringDate = simpleDateFormat.format(new Date());
            Date currentDate = stringToDate(currentStringDate);
            Date drivingLicenseDate = stringToDate(expiryDate);
            String year = yearFormat.format(new Date());
            String monthDay = monthDayFormat.format(drivingLicenseDate);
            String yearMonth = year + "-" + monthDay;
            Date yearMonthDate = stringToDate(yearMonth);
            if (currentDate.equals(yearMonthDate) || currentDate.before(yearMonthDate)) {
                dateString = simpleDateFormat.format(yearMonthDate);
            } else {
                dateString = simpleDateFormat.format(DateUtils.addYears(yearMonthDate, 1));
            }
        }
        return dateString;
    }

    /**
     * 判断驾驶证是否过期
     *
     * @param driver
     * @return
     * @Author @Gao.
     */
    private int expiryDrivingLicenseIsNormal(ShowDriverDto driver) {
        Date currentDate = dateFormat(new Date());
        if (driver.getExpiryDrivingLicense() != null) {
            Date expiryDrivingLicense = stringToDate(driver.getExpiryDrivingLicense());
            if (currentDate.after(expiryDrivingLicense)) {
                return 1;
            }
        }
        return 3;
    }

    /**
     * 判断车辆是否过期
     *
     * @param
     * @return
     */
    private int truckIsNormal(ShowTruckDto truck) {
        Date currentStringDate = dateFormat(new Date());
        if (null == truck.getExpiryAnnual() || null == truck.getExpiryDate()) {
            return 1;
        }
        if (currentStringDate.after(dateFormat(truck.getExpiryAnnual())) || currentStringDate.after(dateFormat(truck.getExpiryDate()))) {
            return 1;
        }
        return 3;
    }

    /**
     * @param date
     * @return
     */
    private static Date dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        return stringToDate(format);
    }

    /**
     * 将时间字符串转化为Date
     *
     * @param stringDate
     * @return
     * @Author @Gao.
     */
    private static Date stringToDate(String stringDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(stringDate);
        } catch (ParseException e) {
            log.error("I stringToDate-{}", e);
        }
        return date;
    }

    /**
     * 将时间转化为字符串
     *
     * @param date
     * @return
     */
    private String dateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
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
        List<GetReceiptListAppDto> receiptList = new ArrayList<>();
        ReceiptListParamDto receiptListParamDto = new ReceiptListParamDto();
        receiptListParamDto.setWaybillStatus(WaybillStatus.RECEIVE)
                .setTruckId(truckByToken.getId())
                .setDriverId(getUserId());
        List<GetWaybillAppDto> waybillAppDtos = waybillMapper.listWaybillByStatus(receiptListParamDto);
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
        log.info("O upDateReceiptStatus:{}", dto);
        Integer waybillId = dto.getWaybillId();
        //加锁
        long time = System.currentTimeMillis() + TIMEOUT;
        boolean success = redisLock.lock("redisLock" + waybillId + dto.getReceiptStatus(), String.valueOf(time));
        if (!success) {
            return ServiceResult.toResult(ReceiptConstant.SERVICE_REQUEST);
        }

        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        if (null != waybill.getDriverId()) {
            redisLock.unLock("redisLock" + waybillId + dto.getReceiptStatus());
            return ServiceResult.toResult(ReceiptConstant.ALREADY_RECEIVED);
        }
        UserInfo currentUser = currentUserService.getCurrentUser();
        ShowTruckDto truckByToken = truckClientService.getTruckByToken();
        GraWaybillConditionDto graWaybillConditionDto = new GraWaybillConditionDto();
        graWaybillConditionDto
                .setWaybillId(waybillId)
                .setDriverId(currentUser.getId())
                .setStatus(GrabWaybillStatusType.NOT_ROB);
        List<GrabWaybillRecord> grabWaybillRecords = grabWaybillRecordMapper.listGrabWaybillByCondition(graWaybillConditionDto);
        WaybillTracking waybillTracking = new WaybillTracking();
        waybillTracking
                .setEnabled(true)
                .setWaybillId(waybillId)
                .setCreateTime(new Date())
                .setDriverId(currentUser.getId())
                .setDevice(dto.getDevice())
                .setLatitude(dto.getLatitude())
                .setLatitude(dto.getLongitude());
        //拒单
        if (WaybillConstant.REJECT.equals(dto.getReceiptStatus())) {
            if (StringUtils.isEmpty(dto.getRefuseReasonId())) {
                throw new RuntimeException("拒单理由不能为空");
            }
            waybillTracking
                    .setRefuseReasonId(dto.getRefuseReasonId())
                    .setOldStatus(WaybillStatus.RECEIVE)
                    .setNewStatus(WaybillStatus.REJECT).setTrackingInfo("运单已被【" + currentUser.getName() + "】取消");
            /**-------添加自动拒单消息----addBy白弋冉**/
            CreateMessageDto createMessageDto = new CreateMessageDto();
            createMessageDto
                    .setCreateUserId(currentUser.getId())
                    .setBody("运单号为（" + waybill.getId() + "）的运单，司机已手动拒单，请及时处理！")
                    .setFromUserId(FromUserType.SYSTEM)
                    .setFromUserType(FromUserType.SYSTEM)
                    .setHeader("你有一个拒单待处理")
                    .setMsgSource(MsgSource.WEB)
                    .setMsgType(MsgType.WAYBILL)
                    .setReferId(waybill.getId())
                    .setToUserId(waybillTrackingMapper.getCreateUserByWaybillId(waybill.getId()))
                    .setToUserType(ToUserType.EMPLOYEE)
                    .setCategory(MsgCategory.WARNING)
                    .setRefuseType(RefuseType.MANUAL);
            if (!CollectionUtils.isEmpty(grabWaybillRecords)) {
                //更新当前人已拒单
                grabWaybillRecordMapper.updateGrabWaybillRecordByReject(graWaybillConditionDto);
                grabWaybillRecords = grabWaybillRecordMapper.listNotRobGrabWaybill(graWaybillConditionDto);
                if (CollectionUtils.isEmpty(grabWaybillRecords)) {
                    waybill.setWaybillStatus(WaybillStatus.REJECT);
                    waybillTrackingMapper.insertSelective(waybillTracking);
                    messageService.createMessage(createMessageDto);
                }
            } else {
                waybill.setWaybillStatus(WaybillStatus.REJECT);
                waybillTrackingMapper.insertSelective(waybillTracking);
                messageService.createMessage(createMessageDto);
            }
            waybill.setModifyUserId(currentUser.getId());
            waybill.setModifyTime(new Date());
            waybillMapper.updateByPrimaryKey(waybill);
            redisLock.unLock("redisLock" + waybillId + dto.getReceiptStatus());
            return ServiceResult.toResult(ReceiptConstant.OPERATION_SUCCESS);
            //接单
        } else if (WaybillConstant.RECEIPT.equals(dto.getReceiptStatus())) {
            waybill.setId(waybillId);
            waybill.setModifyTime(new Date());
            waybill.setDriverId(currentUser.getId());
            waybill.setWaybillStatus(WaybillStatus.DEPART);
            //抢单模式
            if (!CollectionUtils.isEmpty(grabWaybillRecords)) {
                ShowTruckDto truckDto = truckClientService.getTruckByIdReturnObject(grabWaybillRecords.get(0).getTruckId());
                Integer truckTeamContractId = getTruckTeamContractId(orders.getGoodsType(), truckDto.getTruckTeamId());
                waybill
                        .setTruckId(truckDto.getId())
                        .setTruckTeamContractId(truckTeamContractId);
                //更新当前抢单为已抢单
                grabWaybillRecordMapper.updateGrabWaybillRecordByReceipt(graWaybillConditionDto);
                //其他未抢到为已结束
                graWaybillConditionDto
                        .setDriverId(null)
                        .setStatus(GrabWaybillStatusType.NOT_ROB);
                List<Integer> ids = new ArrayList<>();
                grabWaybillRecords = grabWaybillRecordMapper.listGrabWaybillByCondition(graWaybillConditionDto);
                for (GrabWaybillRecord grabWaybillRecord : grabWaybillRecords) {
                    ids.add(grabWaybillRecord.getId());
                }
                grabWaybillRecordMapper.batchUpdate(ids);

            }
            waybillMapper.updateByPrimaryKey(waybill);
            waybillTracking.setOldStatus(WaybillStatus.RECEIVE);
            waybillTracking.setNewStatus(WaybillStatus.DEPART);
            waybillTracking.setTrackingInfo("司机【" + currentUser.getName() + "】已接单，车牌号为【" + truckByToken.getTruckNumber() + "】");
            waybillTrackingMapper.insertSelective(waybillTracking);
            redisLock.unLock("redisLock" + waybillId + dto.getReceiptStatus());
            return ServiceResult.toResult(ReceiptConstant.OPERATION_SUCCESS);
        }
        //解锁
        redisLock.unLock("redisLock" + waybillId + dto.getReceiptStatus());
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
        if(WaybillStatus.SEND_TRUCK.equals(waybillOldStatus) || WaybillStatus.REJECT.equals(waybillOldStatus)) {
            String waybillNewStatus = WaybillStatus.RECEIVE;
            if (null == waybill) {
                return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), waybillId + " ：id不正确");
            }
            if (null == truckClientService.getTruckByIdReturnObject(truckId)) {
                return ServiceResult.error(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), truckId + " ：id不正确");
            }

            //1.更新订单状态：从已配载(STOWAGE)改为运输中(TRANSPORT)
            Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
            orders.setId(waybill.getOrderId());
            orders.setOrderStatus(OrderStatus.TRANSPORT);
            orders.setModifyTime(new Date());
            orders.setModifyUserId(userId);
            ordersMapper.updateByPrimaryKeySelective(orders);
            //2.更新waybill
            ShowTruckDto truckDto = truckClientService.getTruckByIdReturnObject(truckId);
            Integer truckTeamContractId = getTruckTeamContractId(orders.getGoodsType(), truckDto.getTruckTeamId());
            waybill.setTruckId(truckId);
            waybill.setTruckTeamContractId(truckTeamContractId);
            waybill.setWaybillStatus(waybillNewStatus);
            waybill.setSendTime(new Date());
            waybill.setModifyTime(new Date());
            waybill.setModifyUserId(userId);
            waybillMapper.updateByPrimaryKeySelective(waybill);
            //3.在waybill_tracking表插入一条记录
            WaybillTracking waybillTracking = new WaybillTracking();
            waybillTracking
                    .setEnabled(true)
                    .setWaybillId(waybillId)
                    .setCreateTime(new Date())
                    .setOldStatus(waybillOldStatus)
                    .setNewStatus(waybillNewStatus)
                    .setTrackingInfo("已派单 ，运单号为【" + waybillId + "】")
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
                unLoadSiteNames.append(unLoadSite.getSiteName() + "、");
            }
            String unloadSiteName = unLoadSiteNames.substring(0, unLoadSiteNames.length() - 1);
            String alias;
            String msgTitle = "派单消息";
            String msgContent;
            String regId;
            for (DriverReturnDto driver : drivers) {
                Map<String, String> extraParam = new HashMap<>();
                extraParam.put("driverId", driver.getId().toString());
                extraParam.put("waybillId", waybillId.toString());
                extraParam.put("needVoice", "y");
                //您有新的运单信息待接单，从｛装货地名｝到｛卸货地名1｝/｛卸货地名2｝的运单。
                msgContent = "您有新的健安运单待接单，从" + loadSiteName + "到" + unloadSiteName + "的运单。";
                alias = driver.getPhoneNumber();
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
                // 消息类型：1-系统通知 2-运单相关 3-账务相关
                appMessage.setMsgType(MsgType.WAYBILL);
                //消息来源:1-APP,2-小程序,3-站内
                appMessage.setMsgSource(MsgSource.APP);
                appMessage.setMsgStatus(MsgStatusConstant.UNREAD);
                appMessage.setHeader(WaybillConstant.NEW__WAYBILL_FOR_RECEIVE);
                appMessage.setBody("您有新的健安运单待接单,从" + loadSiteName + "到" + unloadSiteName + "的运单。");
                appMessage.setCreateTime(new Date());
                appMessage.setCreateUserId(userId);
                appMessage.setFromUserId(userId);
                appMessage.setToUserId(driver.getId());
                messageMapper.insertSelective(appMessage);
            }
            //7.删除抢单记录
            grabWaybillRecordMapper.deleteByWaybillId(waybillId);
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
        List<Integer> departIds = userClientService.getDownDepartment();
        if (departIds.size() != 0) {
            criteriaDto.setDepartIds(departIds);
        }
        if (!StringUtils.isEmpty(criteriaDto.getWaybillStatus()) && criteriaDto.getWaybillStatus().equals(WaybillStatus.UNLOAD_RECEIPT)) {
            criteriaDto.setWaybillStatus("");
            criteriaDto.setReceiptStatus(2);
        }
        List<ListWaybillDto> listWaybillDto = new ArrayList<>();
        if (!StringUtils.isEmpty(criteriaDto.getTruckNumber())) {
            List<Integer> truckIds = this.customerClientService.getTruckIdsByTruckNum(criteriaDto.getTruckNumber());
            if (truckIds.size() > 0) {
                criteriaDto.setTruckIds(truckIds);
            } else {
                return ServiceResult.toResult(new PageInfo<>(listWaybillDto));
            }
        }
        if (criteriaDto.getCustomerId() != null) {
            List<Integer> orderIds = this.orderService.getOrderIdsByCustomerId(criteriaDto.getCustomerId());
            if (orderIds.size() > 0) {
                criteriaDto.setOrderIds(orderIds);
            } else {
                return ServiceResult.toResult(new PageInfo<>(listWaybillDto));
            }
        }
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        listWaybillDto = waybillMapper.listWaybillByCriteria(criteriaDto);
        if (listWaybillDto != null && listWaybillDto.size() > 0) {
            for (ListWaybillDto waybillDto : listWaybillDto) {
                if (waybillDto.getWaybillStatus().equals(WaybillStatus.ACCOMPLISH) && waybillDto.getReceiptStatus() == 2) {
                    waybillDto.setWaybillStatus(WaybillStatus.UNLOAD_RECEIPT);
                }
                Waybill waybill = this.waybillMapper.selectByPrimaryKey(waybillDto.getId());
                Orders orders = this.ordersMapper.selectByPrimaryKey(waybillDto.getOrderId());
                if (orders != null) {
                    ShowCustomerDto customer = this.customerClientService.getShowCustomerById(orders.getCustomerId());
                    if (customer != null) {
                        waybillDto.setCustomerName(customer.getCustomerName());
                    }
                }
                if (null != orders.getGoodsType()) {
                    waybillDto.setGoodsType(orders.getGoodsType());
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
                //填充卸货地名称
                List<WaybillItems> itemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybillDto.getId());
                if (!CollectionUtils.isEmpty(itemsList)) {
                    List<Integer> siteIds = new ArrayList<>();
                    for (WaybillItems items : itemsList) {
                        siteIds.add(items.getUnloadSiteId());
                    }
                    if (!CollectionUtils.isEmpty(siteIds)) {
                        List<String> siteNameList = customerClientService.listSiteNameByIds(siteIds);
                        if (!CollectionUtils.isEmpty(siteNameList)) {
                            waybillDto.setUnloadName(siteNameList);
                        }
                    }
                }
                //如果当前运单有预警提示消息 则在运单列表显示预警小图标
                if (!CollectionUtils.isEmpty(listPoundAnomaly())) {
                    if (listPoundAnomaly().contains(waybill.getId())) {
                        if (pounderAlert(waybill.getId(), false)) {
                            waybillDto.setPoundAlert(true);
                        }
                    }
                }
            }
        }
        return ServiceResult.toResult(new PageInfo<>(listWaybillDto));
    }

    /**
     * 列出所有磅差异常消息所对应的运单id
     *
     * @return
     * @Author @Gao.
     */
    private List<Integer> listPoundAnomaly() {
        List<Integer> waybillIds = new ArrayList<>();
        ListMessageCriteriaDto listMessageCriteriaDto = new ListMessageCriteriaDto();
        listMessageCriteriaDto
                .setMsgType(MsgType.POUNDS_DIFF);
        List<MessageReturnDto> messageReturnDtos = messageMapper.listMessageByCriteriaDto(listMessageCriteriaDto);
        if (!CollectionUtils.isEmpty(messageReturnDtos)) {
            for (MessageReturnDto messageReturnDto : messageReturnDtos) {
                waybillIds.add(messageReturnDto.getReferId());
            }
        }
        return waybillIds;
    }

    /**
     * 撤回待接单的运单
     *
     * @param waybillId
     * @return
     * @Author gavin
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean withdrawWaybill(Integer waybillId) {
        if (null == waybillId) {
            throw new NullPointerException("运单Id不能为空");
        }
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        if (null == waybill) {
            throw new NullPointerException("运单不存在");
        }
        if (!WaybillStatus.RECEIVE.equals(waybill.getWaybillStatus())) {
            throw new RuntimeException("只有待接单的运单才可以撤回以便重新派单");
        }
        try {
            Integer userId = getUserId();
            Integer truckId = waybill.getTruckId();
            //1.把所派车辆置空、状态改为带派单
            waybill.setTruckId(null);
            waybill.setDriverId(null);
            waybill.setWaybillStatus(WaybillStatus.SEND_TRUCK);
            waybill.setModifyTime(new Date());
            waybill.setModifyUserId(userId);
            waybillMapper.updateByPrimaryKey(waybill);
            //2.删除司机的短信
            Set<Integer> driverIdSet = new HashSet<>();
            GraWaybillConditionDto graWaybillConditionDto = new GraWaybillConditionDto();
            graWaybillConditionDto.setWaybillId(waybillId);
            List<GrabWaybillRecord> grabWaybillRecords = grabWaybillRecordMapper.listGrabWaybillByCondition(graWaybillConditionDto);
            //2.1.抢单模式撤单
            if (!CollectionUtils.isEmpty(grabWaybillRecords)) {
                for (GrabWaybillRecord grabWaybillRecord : grabWaybillRecords) {
                    driverIdSet.add(grabWaybillRecord.getDriverId());
                }
                //删除抢单记录表
                grabWaybillRecordMapper.deleteByWaybillId(waybillId);
            } else {
                //2.2.派单模式撤单
                List<DriverReturnDto> drivers = driverClientService.listByTruckId(truckId);
                for (int i = 0; i < drivers.size(); i++) {
                    driverIdSet.add(drivers.get(i).getId());
                }
            }
            List<Integer> driverIds = new ArrayList<Integer>(driverIdSet);
            if (!CollectionUtils.isEmpty(driverIds)) {
                messageMapper.deleteMessage(waybillId, driverIds);
            }
        } catch (Exception ex) {
            log.error("删除司机短信失败,运单id:{},原因{}", waybillId, ex.getMessage());
            throw ex;
        }

        return true;
    }


    /**
     * 回单修改提货信息
     *
     * @param updateWaybillGoodsDtoList
     * @return
     * @author yj
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLoadGoodsReceipt(List<UpdateWaybillGoodsDto> updateWaybillGoodsDtoList) {
        Integer waybillId = null;
        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        if (!CollectionUtils.isEmpty(updateWaybillGoodsDtoList)) {
            //运单卸货地
            Set<WaybillItems> waybillItemsSet = new HashSet<>();
            //运单货物
            List<WaybillGoods> waybillGoodsList = new LinkedList<>();
            for (UpdateWaybillGoodsDto waybillGoodsDto : updateWaybillGoodsDtoList) {
                waybillId = waybillGoodsDto.getWaybillId();
                Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
                if (waybill == null) {
                    throw new RuntimeException("运单id=" + waybillId + "不存在");
                }
                WaybillItems waybillItems = new WaybillItems();
                waybillItems
                        .setModifyTime(new Date())
                        .setModifyUserId(currentUserId)
                        .setRequiredTime(waybillGoodsDto.getRequiredTime() == null ? new Date() : waybillGoodsDto.getRequiredTime())
                        .setModifyUserId(currentUserId)
                        .setModifyTime(new Date())
                        .setUnloadSiteId(waybillGoodsDto.getUnloadSiteId())
                        .setWaybillId(waybillId)
                        .setEnabled(true)
                        .setSignStatus(waybillGoodsDto.getSignStatus() == null ? false : waybillGoodsDto.getSignStatus());
                waybillItemsSet.add(waybillItems);
            }
            // 插入提货补录轨迹
            WaybillTracking waybillTracking = new WaybillTracking();
            List<ShowTrackingDto> showTrackingDtos = waybillTrackingMapper.getWaybillTrackingByWaybillId(waybillId);
            if (!CollectionUtils.isEmpty(showTrackingDtos)) {
                for (ShowTrackingDto showTrackingDto : showTrackingDtos) {
                    if (TrackingType.LOAD_RECEIPT.equals(showTrackingDto.getTrackingType())) {
                        throw new RuntimeException("补录实提只能提交一次");
                    }
                }
                ShowTrackingDto showTrackingDto = showTrackingDtos.get(0);
                waybillTracking
                        .setOldStatus(showTrackingDto.getOldStatus())
                        .setNewStatus(showTrackingDto.getNewStatus());
            }
            waybillTracking
                    .setEnabled(true)
                    .setCreateTime(new Date())
                    .setWaybillId(waybillId)
                    .setTrackingInfo("补录实提")
                    .setReferUserId(currentUserId)
                    .setTrackingType(TrackingType.LOAD_RECEIPT);
            Integer insertTrackingNum = waybillTrackingMapper.insertSelective(waybillTracking);
            if (insertTrackingNum < 1) {
                throw new RuntimeException("插入运单轨迹(补录实提)失败");
            }
            // 删除原有运单货物
            Integer deleteGoodsNum = waybillGoodsMapper.deleteByWaybillId(waybillId);
            if (deleteGoodsNum < 1) {
                throw new RuntimeException("删除运单货物失败");
            }
            // 删除原有运单卸货地
            Integer deleteItemsNum = waybillItemsMapper.deleteByWaybillId(waybillId);
            if (deleteItemsNum < 1) {
                throw new RuntimeException("删除运单卸货地失败");
            }
            // 插入卸货地
            List<WaybillItems> waybillItemsList = new LinkedList<>(waybillItemsSet);
            Integer insertItemsNum = waybillItemsMapper.batchInsert(waybillItemsList);
            if (!insertItemsNum.equals(waybillItemsSet.size())) {
                throw new RuntimeException("插入卸货地失败");
            }
            waybillItemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybillId);
            // 设置运单货物运单卸货地id
            for (UpdateWaybillGoodsDto waybillGoodsDto : updateWaybillGoodsDtoList) {
                WaybillGoods waybillGoods = new WaybillGoods();
                BeanUtils.copyProperties(waybillGoodsDto, waybillGoods);
                waybillGoods
                        .setModifyUserId(currentUserId)
                        .setModifyTime(new Date());
                for (WaybillItems waybillItems : waybillItemsList) {
                    if (waybillGoodsDto.getWaybillId().equals(waybillItems.getWaybillId()) && waybillGoodsDto.getUnloadSiteId().equals(waybillItems.getUnloadSiteId())) {
                        waybillGoods.setWaybillItemId(waybillItems.getId());
                    }
                }
                // 设置是否加药,订单货物id,是否有效默认值
                if (waybillGoods.getEnabled() == null) {
                    waybillGoods.setEnabled(true);
                }
                if (waybillGoods.getJoinDrug() == null) {
                    waybillGoods.setJoinDrug(false);
                }
                if (waybillGoods.getOrderGoodsId() == null) {
                    waybillGoods.setOrderGoodsId(0);
                }
                waybillGoodsList.add(waybillGoods);
            }
            Integer insertGoodsNum = waybillGoodsMapper.batchInsert(waybillGoodsList);
            if (!insertGoodsNum.equals(waybillGoodsList.size())) {
                throw new RuntimeException("插入运单货物失败");
            }
            // 修改运单回单状态(不设置修改人和修改时间防止影响统计运单完成数)
            Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
            if (waybill != null) {
                waybill.setReceiptStatus(ReceiptStatus.LOAD_RECEIPT);
                int updateWaybillNum = waybillMapper.updateByPrimaryKeySelective(waybill);
                if (updateWaybillNum < 1) {
                    throw new RuntimeException("修改运单失败");
                }
            }
        }
        return true;
    }

    /**
     * 回单修改卸货信息
     *
     * @param updateWaybillGoodsDtoList
     * @return
     * @author yj
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUnLoadGoodsReceipt(List<UpdateWaybillGoodsDto> updateWaybillGoodsDtoList) {
        if (!CollectionUtils.isEmpty(updateWaybillGoodsDtoList)) {
            UserInfo currentUser = currentUserService.getCurrentUser();
            Integer currentUserId = currentUser == null ? null : currentUser.getId();
            List<WaybillGoods> waybillGoodsList = new LinkedList<>();
            Integer waybillId = null;
            for (UpdateWaybillGoodsDto goodsDto : updateWaybillGoodsDtoList) {
                waybillId = goodsDto.getWaybillId();
                WaybillGoods waybillGoods = new WaybillGoods();
                BeanUtils.copyProperties(goodsDto, waybillGoods);
                waybillGoods
                        .setModifyTime(new Date())
                        .setModifyUserId(currentUserId);
                waybillGoodsList.add(waybillGoods);
            }
            // 插入卸货补录轨迹
            WaybillTracking waybillTracking = new WaybillTracking();
            waybillTracking.setEnabled(true);
            List<ShowTrackingDto> showTrackingDtos = waybillTrackingMapper.getWaybillTrackingByWaybillId(waybillId);
            boolean hasLoadTracking = false;
            if (!CollectionUtils.isEmpty(showTrackingDtos)) {
                for (ShowTrackingDto showTrackingDto : showTrackingDtos) {
                    if (TrackingType.UNLOAD_RECEIPT.equals(showTrackingDto.getTrackingType())) {
                        throw new RuntimeException("补录实卸只能提交一次");
                    }
                    if (TrackingType.LOAD_RECEIPT.equals(showTrackingDto.getTrackingType())) {
                        hasLoadTracking = true;
                    }
                }
                ShowTrackingDto showTrackingDto = showTrackingDtos.get(0);
                waybillTracking
                        .setOldStatus(showTrackingDto.getOldStatus())
                        .setNewStatus(showTrackingDto.getNewStatus());
            }
            waybillTracking
                    .setCreateTime(new Date())
                    .setWaybillId(waybillId)
                    .setTrackingInfo("补录实卸")
                    .setReferUserId(currentUserId)
                    .setTrackingType(TrackingType.UNLOAD_RECEIPT);
            Integer insertUnloadTrackingNum = waybillTrackingMapper.insertSelective(waybillTracking);
            if (insertUnloadTrackingNum < 1) {
                throw new RuntimeException("插入运单轨迹(补录实卸)失败");
            }
            if (!hasLoadTracking) {
                waybillTracking
                        .setId(null)
                        .setTrackingInfo("补录实提")
                        .setTrackingType(TrackingType.LOAD_RECEIPT);
                Integer insertLoadTrackingNum = waybillTrackingMapper.insertSelective(waybillTracking);
                if (insertLoadTrackingNum < 1) {
                    throw new RuntimeException("插入运单轨迹(补录实提)失败");
                }
            }
            waybillGoodsMapper.batchUpdateByPrimaryKeySelective(waybillGoodsList);
            // 修改运单回单状态(不设置修改人和修改时间防止影响统计运单完成数)
            Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
            if (waybill != null) {
                waybill.setReceiptStatus(ReceiptStatus.UNLOAD_RECEIPT);
                int updateWaybillNum = waybillMapper.updateByPrimaryKeySelective(waybill);
                if (updateWaybillNum < 1) {
                    throw new RuntimeException("修改运单失败");
                }
            }
            // 磅差异常提醒
            try {
                pounderAlert(waybillId, true);
            } catch (Exception ex) {
                log.error("pounderAlert error waybillId=" + waybillId, ex);
            }
        }
        return true;
    }


    /**
     * 上传回单图片
     *
     * @param uploadReceiptImageDto
     * @return
     * @author yj
     */
    @Override
    @Deprecated
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadReceiptImage(UploadReceiptImageDto uploadReceiptImageDto) {
        // 获取公共参数
        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        Integer waybillId = uploadReceiptImageDto.getWaybillId();
        List<ShowTrackingDto> waybillTrackingList = waybillTrackingMapper.getWaybillTrackingByWaybillId(waybillId);
        ShowTrackingDto showTrackingDto = null;
        if (!CollectionUtils.isEmpty(waybillTrackingList)) {
            showTrackingDto = waybillTrackingList.get(0);
        }

        // 物理删除补录单据，补录单据轨迹
        List<GetTrackingImagesDto> uploadImages = uploadReceiptImageDto.getUploadImages();
        if (!CollectionUtils.isEmpty(uploadImages)) {
            List<WaybillTracking> waybillTrackings = new LinkedList<>();
            List<WaybillTrackingImages> waybillTrackingImagesList = new LinkedList<>();
            waybillTrackingImagesMapper.deleteByWaybillIdAndImageType(waybillId, ImagesTypeConstant.RECEIPT_BILL);
            for (GetTrackingImagesDto imagesDto : uploadImages) {
                WaybillTracking waybillTracking = new WaybillTracking();
                waybillTracking
                        .setEnabled(true)
                        .setOldStatus(showTrackingDto.getOldStatus())
                        .setNewStatus(showTrackingDto.getNewStatus())
                        .setTrackingType(TrackingType.LOAD_BILLS_RECEIPT)
                        .setReferUserId(currentUserId)
                        .setTrackingInfo("回单补传单据")
                        .setWaybillId(waybillId)
                        .setCreateTime(new Date());
                waybillTrackings.add(waybillTracking);
                WaybillTrackingImages trackingImages = new WaybillTrackingImages();
                trackingImages
                        .setWaybillTrackingId(waybillTracking.getId())
                        .setImageUrl(imagesDto.getImageUrl())
                        .setImageType(ImagesTypeConstant.RECEIPT_BILL)
                        .setCreateUserId(currentUserId)
                        .setCreateTime(new Date())
                        .setWaybillId(waybillId);
                waybillTrackingImagesList.add(trackingImages);
            }
            Integer insertNum = waybillTrackingImagesMapper.batchInsert(waybillTrackingImagesList);
            if (waybillTrackingImagesList.size() != insertNum) {
                throw new RuntimeException("插入补录单据失败");
            }
            insertNum = waybillTrackingMapper.batchInsert(waybillTrackings);
            if (waybillTrackingList.size() != insertNum) {
                throw new RuntimeException("插入补录单据轨迹失败");
            }
        }
        return true;
    }

    /**
     * 根据订单id获取运单
     *
     * @param orderId
     * @return
     */
    @Override
    public List<ListWaybillDto> listWaybillByOrderId(Integer orderId) {
        return waybillMapper.listWaybillDtoByOrderId(orderId);
    }

    /**
     * 根据订单id获取运单分页
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public PageInfo listWaybillByCriteriaForWechat(ListWaybillCriteriaDto criteriaDto) {
        PageHelper.startPage(criteriaDto.getPageNum(), criteriaDto.getPageSize());
        List<Integer> waybillIds = waybillMapper.listWaybillIdByOrderId(criteriaDto.getOrderId());
        if (null == waybillIds) {
            throw new NullPointerException(criteriaDto.getOrderId() + " :当前订单无有效运单");
        }
        List<GetWaybillDto> getWaybillDtoList = new ArrayList<>(12);
        for (Integer waybillId : waybillIds) {
            GetWaybillDto getWaybillDto = this.getWaybillById(waybillId);
            getWaybillDtoList.add(getWaybillDto);
        }
        return new PageInfo<>(getWaybillDtoList);
    }

    /**
     * 根据订单id查询 待派单的运单
     *
     * @param id
     * @return
     */
    @Override
    public List<ListWaybillDto> listWaybillWaitByOrderId(Integer id) {
        return waybillMapper.listWaybillDtoWaitByOrderId(id);
    }

    /**
     * 根据订单id查询已拒单的个数
     *
     * @param orderId
     * @return
     */
    @Override
    public Integer listRejectWaybillByOrderId(Integer orderId) {
        return waybillMapper.listRejectWaybillByOrderId(orderId);
    }

    /**
     * 根据订单id查询待派单的运单
     *
     * @param id
     * @return
     */
    @Override
    public Integer listWaitWaybillByOrderId(Integer id) {
        return waybillMapper.listWaitWaybillByOrderId(id);
    }

    /**
     * 获取当前登录司机的车辆信息
     *
     * @return
     */
    @Override
    public ShowTruckInfoDto getTruckInfo() {
        ShowTruckDto truckByToken = truckClientService.getTruckByToken();
        if (truckByToken != null) {
            return getTruckInfo(truckByToken);
        }
        return null;
    }

    /**
     * 我要换车列表
     *
     * @return
     */
    @Override
    public List<ChangeTruckDto> getChangeTruckList() {
        UserInfo userInfo = currentUserService.getCurrentUser();
        if (userInfo != null) {
            DriverReturnDto driverDto = driverClientService.getDriverByIdFeign(userInfo == null ? null : userInfo.getId());
            if (driverDto != null) {
                BaseResponse<List<ChangeTruckDto>> baseResponse = truckClientService.listTruckByTruckTeamId(driverDto.getTruckTeamId());
                if (baseResponse != null) {
                    List<ChangeTruckDto> changeTruckDtoList = baseResponse.getData();
                    return changeTruckDtoList;
                }
            }
        }
        return null;
    }

    /**
     * 换车提交
     *
     * @param truckDto
     * @return
     */
    @Override
    public Map<String, Object> changeTruck(TransferTruckDto truckDto) {
        //判断当前车辆是否满足换车条件
        Integer amount = waybillMapper.countUnDoneByDriverId(truckDto.getDriverId());
        if (amount != null && amount > 0) {
            return ServiceResult.error("本次及之前任务未完成的车辆不能申请换车");
        }
        ShowDriverDto driverDto = driverClientService.getDriverReturnObject(truckDto.getDriverId());
        if (driverDto == null) {
            return ServiceResult.error("司机信息有误");
        }
        //判断要换的车辆是否符合条件
        ShowTruckDto showTruckDto = truckClientService.getTruckByIdReturnObject(truckDto.getTruckId());
        if (showTruckDto == null) {
            return ServiceResult.error("车辆信息有误");
        }
        if (showTruckDto.getTruckStatus() != 1) {
            return ServiceResult.error("车辆状态不符合换车条件");
        }
        //满足条件--将司机所属车辆换掉
        UpdateDriverDto updateDriverDto = new UpdateDriverDto();
        updateDriverDto
                .setId(truckDto.getDriverId())
                .setTruckId(truckDto.getTruckId())
                .setTruckTeamId(showTruckDto.getTruckTeamId());
        try {
            driverClientService.updateDriverFeign(updateDriverDto);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServiceResult.error(ex.getMessage());
        }
        //修改司机资质的信息
        BaseResponse baseResponse = truckClientService.listQualificationByDriverId(truckDto.getDriverId());
        if (baseResponse != null) {
            List<ListTruckQualificationDto> qualificationDtoList = (List<ListTruckQualificationDto>) baseResponse.getData();
            if (!CollectionUtils.isEmpty(qualificationDtoList)) {
                for (ListTruckQualificationDto qualificationDto : qualificationDtoList) {
                    TruckQualification truckQualification = new TruckQualification();
                    BeanUtils.copyProperties(qualificationDto, truckQualification);
                    truckQualification
                            .setTruckId(truckDto.getTruckId())
                            .setDriverId(truckDto.getDriverId())
                            .setTruckTeamId(showTruckDto.getTruckTeamId());
                    truckClientService.truckQualificationToFeign(truckQualification);
                }
            }
        }
        return ServiceResult.toResult("换车成功");
    }

    private Integer getUserId() {
        UserInfo userInfo = null;
        try {
            userInfo = currentUserService.getCurrentUser();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("获取当前用户失败：currentUserService.getCurrentUser()");
            return 1;
        }
        if (null == userInfo) {
            return 1;
        } else {
            return userInfo.getId();
        }
    }

    /**
     * 磅差超过千分之二，进行预警提醒
     *
     * @param
     * @return
     * @Author @Gao.
     */
    private boolean pounderAlert(Integer waybillId, boolean sendMessage) {
        List<GetWaybillGoodsDto> waybillGoodsDtos = waybillGoodsMapper.listGoodsByWaybillId(waybillId);
        BigDecimal totalLoadWeight = BigDecimal.ZERO;
        BigDecimal totalUnloadWeight = BigDecimal.ZERO;
        for (GetWaybillGoodsDto waybillGoodsDto : waybillGoodsDtos) {
            boolean flag = (null != waybillGoodsDto.getUnloadWeight() && null != waybillGoodsDto.getLoadWeight()
                    && GoodsUnit.TON.equals(waybillGoodsDto.getGoodsUnit()));
            if (flag) {
                //单位 羽 吨 累计提货重量
                totalLoadWeight = totalLoadWeight.add(waybillGoodsDto.getLoadWeight());
                //单位 羽 吨 累计卸货重量
                totalUnloadWeight = totalUnloadWeight.add(waybillGoodsDto.getUnloadWeight());
            }
        }
        BigDecimal weightDiff = totalLoadWeight.subtract(totalUnloadWeight).abs();
        if (totalLoadWeight.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal weightDivide = weightDiff.divide(totalLoadWeight, 6, BigDecimal.ROUND_HALF_UP);
            if (DataConstant.DIFFWEIGHT.compareTo(weightDivide) == -1) {
                //插入预警提醒信息
                if (true == sendMessage) {
                    Message message = new Message();
                    message
                            .setToUserId(0)
                            .setReferId(waybillId)
                            .setCreateUserId(0)
                            .setMsgSource(MsgSource.WEB)
                            .setFromUserId(0)
                            .setFromUserType(0)
                            .setMsgType(MsgType.POUNDS_DIFF)
                            .setBody("运单号为（" + waybillId + "）的运单，出现磅差异常，请及时处理。")
                            .setHeader("你有一个运单异常消息待接收");
                    messageMapper.insertSelective(message);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 运单作废
     * 20181116
     *
     * @param waybillId
     * @return
     * @Author gavin
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean abandonWaybill(Integer waybillId) {

        Waybill waybillData = waybillMapper.selectByPrimaryKey(waybillId);
        if (null == waybillData) {
            throw new NullPointerException("运单不存在");
        }
        //1.只有带派单的运单才可以作废
        if (!waybillData.getWaybillStatus().equals(WaybillStatus.SEND_TRUCK)) {
            throw new RuntimeException("只有【待派单】的运单才可以作废");
        }

        if (!waybillData.getEnabled()) {
            throw new RuntimeException("该运单已删除");
        }
        //2.把运单状态修改为作废
        waybillData.setWaybillStatus(WaybillStatus.ABANDON);
        waybillMapper.updateByPrimaryKeySelective(waybillData);

        //3.更新订单的状态为"已完成"
        List<Waybill> waybillList = waybillMapper.listWaybillByOrderId(waybillData.getOrderId());
        boolean canChangeFlag = true;
        for (Waybill waybill : waybillList) {
            boolean flag = waybill.getWaybillStatus().equals(WaybillStatus.RECEIVE) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.SEND_TRUCK) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.DEPART) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.REJECT) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.SIGN) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.LOAD_PRODUCT) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.DELIVERY) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.CANCEL) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.DELIVERY) ||
                    waybill.getWaybillStatus().equals(WaybillStatus.ARRIVE_LOAD_SITE);
            if (flag) {
                canChangeFlag = false;
                break;
            }
        }
        //4.只有订单下所有的运单的状态变成"已完成"或者"已作废"
        if (canChangeFlag) {
            Orders order = ordersMapper.selectByPrimaryKey(waybillData.getOrderId());
            order.setOrderStatus(OrderStatus.ACCOMPLISH);
            ordersMapper.updateByPrimaryKeySelective(order);

        }
        //5.删除抢单记录
        grabWaybillRecordMapper.deleteByWaybillId(waybillId);
        return true;
    }

    /**
     * @param waybillIds
     * @return
     * @Author gavin
     * 20181222
     * 客户结算
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<Map<Integer, BigDecimal>> calculatePaymentFromCustomer(List<Integer> waybillIds) {
        List<CalculatePaymentDto> paymentDtos = getCalculatePaymentDtoList(waybillIds);
        List<Map<Integer, BigDecimal>> feeFromCustomerList = customerClientService.calculatePaymentFromCustomer(paymentDtos);
        UserInfo currentUser = currentUserService.getCurrentUser();
        Integer currentUserId = currentUser == null ? null : currentUser.getId();
        for (Map<Integer, BigDecimal> map : feeFromCustomerList) {
            WaybillCustomerFee waybillCustomerFee = new WaybillCustomerFee();
            Iterator<Entry<Integer, BigDecimal>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Integer, BigDecimal> entry = it.next();
                waybillCustomerFee.setWaybillId(entry.getKey());
                waybillCustomerFee.setMoney(entry.getValue());
                waybillCustomerFee.setEarningType(CostType.FREIGHT);
                waybillCustomerFee.setDirection(Direction.PLUS);
                waybillCustomerFee.setCreatedUserId(currentUserId);
                waybillCustomerFee.setSettleStatus(SettleStatus.UN_SETTLE);
            }
            waybillCustomerFeeMapperExt.deleteRecordByCritera(waybillCustomerFee);
            waybillCustomerFeeMapperExt.insertSelective(waybillCustomerFee);

        }
        return feeFromCustomerList;
    }

    /**
     * 司机结算计算价格
     *
     * @param waybillIds
     * @return
     * @author yj
     * @since 20181226
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Map<Integer, BigDecimal>> calculatePaymentFromDriver(List<Integer> waybillIds) {
        // 拼装计价参数
        List<CalculatePaymentDto> paymentDtoList = getCalculatePaymentDtoList(waybillIds);
        // 获取计算后的价格
        List<Map<Integer, BigDecimal>> paymentList = customerClientService.calculatePaymentFromDriver(paymentDtoList);
        if (!CollectionUtils.isEmpty(paymentList)) {
            List<WaybillTruckFee> waybillTruckFeeList = new ArrayList<>();
            UserInfo currentUser = currentUserService.getCurrentUser();
            Integer currentUserId = currentUser == null ? null : currentUser.getId();
            for (Map<Integer, BigDecimal> map : paymentList) {
                WaybillTruckFee waybillTruckFee = new WaybillTruckFee();
                Iterator<Entry<Integer, BigDecimal>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<Integer, BigDecimal> entry = it.next();
                    waybillTruckFee.setCreatedUserId(currentUserId)
                            .setCreateTime(new Date())
                            .setDirection(Direction.SUBSTRACT)
                            .setEnabled(Boolean.TRUE)
                            .setCostType(CostType.FREIGHT)
                            .setMoney(entry.getValue())
                            .setWaybillId(entry.getKey())
                            .setSettleStatus(SettleStatus.UN_SETTLE);
                }
                waybillTruckFeeList.add(waybillTruckFee);
            }
            // 批量删除已创建的运单车辆费用
            waybillTruckFeeMapperExt.batchDelete(waybillTruckFeeList);
            // 批量新增运单车辆费用
            waybillTruckFeeMapperExt.batchInsert(waybillTruckFeeList);
        }
        return paymentList;
    }

    /**
     * 毛鸡导入预览
     *
     * @param preImportChickenRecordDto
     * @return
     */
    @Override
    public List<ChickenImportRecordDto> preImportChickenWaybill(PreImportChickenRecordDto preImportChickenRecordDto) {
        try {
            // 判断订单状态,只有已下单的运单才能做毛鸡导入
            judgeOrderForChickenImport(preImportChickenRecordDto.getOrderId());
            // 读取excel内容
            List<List<String[]>> lists = PoiUtil.readExcel(preImportChickenRecordDto.getUploadUrl());
            if (CollectionUtils.isEmpty(lists) || CollectionUtils.isEmpty(lists.get(0))) {
                return new ArrayList<>();
            }
            log.info("uploadUrl={},excelContent={}", preImportChickenRecordDto.getUploadUrl(), JSON.toJSONString(lists.get(0)));
            // 将excel内容解析为dto
            List<ChickenImportRecordDto> chickenImportRecordDtoList = parsingExcel(lists.get(0), preImportChickenRecordDto);
            log.info("parsingExcel end chickenImportRecordDtoList={}", JSON.toJSONString(chickenImportRecordDtoList));
            // 数据存入redis 批量插入存入hash保证原子性
            putChickenImportRecordToRedis(chickenImportRecordDtoList);
            log.info("putChickenImportRecordToRedis success");
            return chickenImportRecordDtoList;
        } catch (Exception e) {
            log.error("preImportChickenWaybill error preImportChickenRecordDto=" + preImportChickenRecordDto, e);
        }
        return new ArrayList<>();
    }

    /**
     * 毛鸡导入记录入库并生成运单派单给车辆下所有司机
     *
     * @param orderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importChickenWaybill(Integer orderId) {
        String key = CHICKEN_IMPORT + orderId;
        HashOperations<String, Object, Object> opsForHash = objectRedisTemplate.opsForHash();
        Map<Object, Object> entries = opsForHash.entries(key);
        List<ChickenImportRecordDto> chickenImportRecordDtoValidList = getChickenImportRecordDtoListFromMap(entries);
        log.info("importChickenWaybill orderId={},chickenImportRecordDtoValidList={}", orderId, JSON.toJSONString(chickenImportRecordDtoValidList));
        if (!CollectionUtils.isEmpty(chickenImportRecordDtoValidList)) {
            // 判断运单状态,只有已下单的运单才能做毛鸡导入
            judgeOrderForChickenImport(orderId);
            List<ChickenImportRecord> chickenImportRecordList = new ArrayList<>();
            UserInfo currentUser = currentUserService.getCurrentUser();
            Integer currentUserId = currentUser == null ? null : currentUser.getId();
            List<ImportWaybillDto> importWaybillDtoList = new ArrayList<>();
            for (ChickenImportRecordDto dto : chickenImportRecordDtoValidList) {
                if (dto.getVerifyPass() == null || !dto.getVerifyPass()) {
                    throw new RuntimeException("有无效车牌，请重新确认");
                }

                ChickenImportRecord record = new ChickenImportRecord();
                ImportWaybillDto importWaybillDto = new ImportWaybillDto();
                BeanUtils.copyProperties(dto, importWaybillDto);
                BeanUtils.copyProperties(dto, record);
                record.setCreateTime(new Date())
                        .setCreateUserId(currentUserId)
                        .setEnable(true);
                chickenImportRecordList.add(record);
                importWaybillDtoList.add(importWaybillDto);
            }
            // 毛鸡导入原始记录入库
            chickenImportRecordMapperExt.batchInsert(chickenImportRecordList);
            // 生成运单并派给车辆,如果表格数量过多需要起任务处理,防止处理时间过长事务不释放一直占用数据库链接
            importWaybills(orderId, importWaybillDtoList);
            // 清空缓存
            objectRedisTemplate.delete(key);
        } else {
            throw new RuntimeException("导入失败");
        }
    }

    private List<CalculatePaymentDto> getCalculatePaymentDtoList(List<Integer> waybillIds) {
        if (!CollectionUtils.isEmpty(waybillIds)) {
            List<CalculatePaymentDto> paymentDtoList = new ArrayList<>();
            for (Integer waybillId : waybillIds) {
                BigDecimal unloadWeight = new BigDecimal(0.00);
                Integer unloadQuantity = 0;
                Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
                if (waybill != null && waybill.getWaybillStatus().equals(WaybillStatus.ACCOMPLISH)) {
                    CalculatePaymentDto calculatePaymentDto = new CalculatePaymentDto();
                    Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
                    // 合同未审核通过不算报价
                    if (orders != null) {
                        calculatePaymentDto.setWaybillId(waybillId);
                        calculatePaymentDto.setDoneDate(waybill.getModifyTime());
                        calculatePaymentDto.setTruckTypeId(waybill.getNeedTruckType());
                        calculatePaymentDto.setProductType(orders.getGoodsType());
                        calculatePaymentDto.setCustomerContractId(orders.getCustomerContractId());
                        calculatePaymentDto.setTruckTeamContractId(waybill.getTruckTeamContractId());
                        List<WaybillItems> itemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybillId);
                        if (!CollectionUtils.isEmpty(itemsList)) {
                            List<SiteDto> siteDtoList = new ArrayList<>();
                            for (WaybillItems waybillItems : itemsList) {
                                SiteDto siteDto = new SiteDto();
                                siteDto.setLoadSiteId(orders.getLoadSiteId())
                                        .setUnloadSiteId(waybillItems.getUnloadSiteId());
                                siteDtoList.add(siteDto);
                            }
                            calculatePaymentDto.setSiteDtoList(siteDtoList);
                        }
                        List<GetWaybillGoodsDto> waybillGoodsDtos = waybillGoodsMapper.listGoodsByWaybillId(waybillId);
                        if (!CollectionUtils.isEmpty(waybillGoodsDtos)) {
                            for (GetWaybillGoodsDto waybillGoodsDto : waybillGoodsDtos) {
                                if (orders.getGoodsType().equals(GoodsType.CHICKEN) || orders.getGoodsType().equals(GoodsType.FODDER)) {
                                    unloadWeight = unloadWeight.add(waybillGoodsDto.getUnloadWeight());
                                } else {
                                    unloadQuantity = unloadQuantity + waybillGoodsDto.getUnloadQuantity();
                                }
                            }
                        }
                        if (orders.getGoodsType().equals(GoodsType.CHICKEN) || orders.getGoodsType().equals(GoodsType.FODDER)) {

                            calculatePaymentDto.setUnloadWeight(unloadWeight);

                        } else {
                            calculatePaymentDto.setUnloadQuantity(unloadQuantity);
                        }
                    }
                    paymentDtoList.add(calculatePaymentDto);
                }
            }
            return paymentDtoList;
        }
        return null;
    }

    private Integer getTruckTeamContractId(Integer goodsType, Integer truckTeamId) {
        Integer truckTeamContractId = null;
        List<TruckTeamContractReturnDto> truckTeamContracts = truckClientService.getTruckTeamContractByTruckTeamId(truckTeamId);
        if (!CollectionUtils.isEmpty(truckTeamContracts)) {
            for (TruckTeamContractReturnDto truckTeamContractReturnDto : truckTeamContracts) {
                if (truckTeamContractReturnDto.getBussinessType() != null && truckTeamContractReturnDto.getBussinessType().equals(goodsType)) {
                    truckTeamContractId = truckTeamContractReturnDto.getId();
                    break;
                }
            }
        }
        return truckTeamContractId;
    }

    private List<ChickenImportRecordDto> parsingExcel(List<String[]> list, PreImportChickenRecordDto preImportChickenRecordDto) throws ParseException {
        if (!CollectionUtils.isEmpty(list)) {
            List<ChickenImportRecordDto> chickenImportRecordDtoList = new ArrayList<>();
            String[] dayCells = list.get(1);
            // 获取屠宰日期
            String day = "";
            if (dayCells != null && dayCells.length > TRANSPORT_DAY_INDEX) {
                day = dayCells[18];
            }
            log.info("屠宰日期=" + day);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Orders orders = ordersMapper.selectByPrimaryKey(preImportChickenRecordDto.getOrderId());
            // 数据从第四行开始
            for (int i = 3; i < list.size(); i++) {
                String[] cells = list.get(i);
                // 数据列结束跳出循环
                if ("合计".equals(cells[1])) {
                    break;
                }
                ChickenImportRecordDto dto = new ChickenImportRecordDto();
                dto.setCustomerId(preImportChickenRecordDto.getCustomerId())
                        .setCustomerName(preImportChickenRecordDto.getCustomerName())
                        .setLoadSiteId(preImportChickenRecordDto.getLoadSiteId())
                        .setLoadSiteName(preImportChickenRecordDto.getLoadSiteName())
                        .setOrderId(preImportChickenRecordDto.getOrderId())
                        .setVerifyPass(false)
                        .setSerialNumber(StringUtils.isEmpty(cells[1]) ? null : Integer.parseInt(cells[1]));
                // 装货时间(车入鸡场时间)
                Date loadTime = sdf.parse(day + " " + cells[10]);
                dto.setLoadTime(loadTime);
                // 要求送达时间(入屠宰场时间)
                Date requiredTime = sdf.parse(day + " " + cells[16]);
                dto.setRequiredTime(requiredTime);
                // 货物数量(单车筐数)
                String quantity = cells[20];
                if (StringUtils.hasText(quantity)) {
                    Integer goodsQuantity = Integer.parseInt(new DecimalFormat("0").format(Double.valueOf(cells[20])));
                    dto.setGoodsQuantity(goodsQuantity);
                }
                // 装货地对应网点id
                ShowSiteDto showSiteById = customerClientService.getShowSiteById(preImportChickenRecordDto.getLoadSiteId());
                if (showSiteById != null) {
                    dto.setLoadSiteDeptId(showSiteById.getDeptId());
                }
                // 去除车牌号中"大","中","小"
                String truckNumber = cells[8];
                if (truckNumber.endsWith("大") || truckNumber.endsWith("中") || truckNumber.endsWith("小")) {
                    truckNumber = truckNumber.substring(0, truckNumber.length() - 1);
                }
                dto.setTruckNumber(truckNumber);
                BaseResponse<List<ListTruckTypeDto>> response = truckClientService.listTruckTypeByProductName(ProductName.CHICKEN.toString());
                if (response != null && response.getData() != null) {
                    response.getData().forEach(listTruckTypeDto -> {
                        if (listTruckTypeDto.getTruckAmount().equals(dto.getGoodsQuantity() == null ? null : dto.getGoodsQuantity().toString())) {
                            dto.setTruckTypeId(listTruckTypeDto.getId());
                            dto.setTruckTypeName(listTruckTypeDto.getTypeName());
                        }
                    });
                }
                // 校验车牌号合法性
                BaseResponse<GetTruckDto> res = truckClientService.getByTruckNumber(truckNumber);
                if (res != null && res.getData() != null) {
                    GetTruckDto truckDto = res.getData();
                    ListTruckTypeDto truckTypeDto = truckDto.getTruckTypeId();
                    if (truckTypeDto == null) {
                        continue;
                    }
                    boolean checkTruckType = ProductName.CHICKEN.toString().equals(truckTypeDto.getProductName()) && (dto.getGoodsQuantity() != null && dto.getGoodsQuantity().toString().equals(truckTypeDto.getTruckAmount()));
                    if (checkTruckType) {
                        dto.setVerifyPass(true);
                    }
                    dto.setTruckId(truckDto.getId());
                    // 获取车队合同id
                    dto.setTruckTeamContractId(getTruckTeamContractId(orders.getGoodsType(), truckDto.getTruckTeamId()));
                }
                chickenImportRecordDtoList.add(dto);
            }
            return chickenImportRecordDtoList;
        }
        return new ArrayList<>();
    }

    private void judgeOrderForChickenImport(Integer orderId) {
        // 判断订单状态
        Orders orders = ordersMapper.selectByPrimaryKey(orderId);
        if (orders == null) {
            throw new RuntimeException("订单id=" + orderId + "不存在");
        }
        if (!OrderStatus.PLACE_ORDER.equals(orders.getOrderStatus())) {
            throw new RuntimeException("只有已下单状态的订单才可以导入");
        }
        if (!GoodsType.CHICKEN.equals(orders.getGoodsType())) {
            throw new RuntimeException("只有毛鸡订单才可以导入");
        }
    }

    private void putChickenImportRecordToRedis(List<ChickenImportRecordDto> chickenImportRecordDtoList) {
        if (!CollectionUtils.isEmpty(chickenImportRecordDtoList)) {
            HashOperations<String, Object, Object> opsForHash = objectRedisTemplate.opsForHash();
            Integer orderId = chickenImportRecordDtoList.get(0).getOrderId();
            String key = CHICKEN_IMPORT + orderId;
            // 先清空原缓存
            objectRedisTemplate.delete(key);
            Map<String, ChickenImportRecordDto> map = new LinkedHashMap<>();
            chickenImportRecordDtoList.forEach(dto -> map.put(dto.getSerialNumber() == null ? null : dto.getSerialNumber().toString(), dto));
            opsForHash.putAll(key, map);
            objectRedisTemplate.expire(key, 1, TimeUnit.HOURS);
        }
    }

    private List<ChickenImportRecordDto> getChickenImportRecordDtoListFromMap(Map<Object, Object> map) {
        List<ChickenImportRecordDto> chickenImportRecordDtoList = new ArrayList<>();
        Set<Object> ketSet = map.keySet();
        Iterator<Object> iterator = ketSet.iterator();
        iterator.forEachRemaining(element -> chickenImportRecordDtoList.add((ChickenImportRecordDto) map.get(element.toString())));
        Collections.sort(chickenImportRecordDtoList, Comparator.comparingInt(ChickenImportRecordDto::getSerialNumber));
        return chickenImportRecordDtoList;
    }
}