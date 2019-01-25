package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.truck.DriverReturnDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.dto.waybill.GraWaybillConditionDto;
import com.jaagro.tms.api.dto.waybill.GrabWaybillParamDto;
import com.jaagro.tms.api.service.GrabWaybillService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.jpush.JpushClientUtil;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.DriverClientService;
import com.jaagro.tms.biz.service.TruckClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 派单到多个车辆 抢单服务
 *
 * @author @Gao.
 */
@Service
@Slf4j
public class GrabWaybillServiceImpl implements GrabWaybillService {
    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private WaybillTrackingMapperExt waybillTrackingMapper;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private GrabWaybillRecordMapperExt grabWaybillRecordMapper;
    @Autowired
    private TruckClientService truckClientService;
    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private WaybillItemsMapperExt waybillItemsMapper;
    @Autowired
    private MessageMapperExt messageMapper;

    /**
     * 进行抢单模式派单
     *
     * @param dto
     * @author @Gao.
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grabWaybillToTrucks(GrabWaybillParamDto dto) {
        Waybill waybill = waybillMapper.selectByPrimaryKey(dto.getWaybillId());
        if (null == waybill) {
            throw new RuntimeException("当前运单" + waybill.getId() + "不存在!");
        }
        String waybillOldStatus = waybill.getWaybillStatus();
        String waybillNewStatus = WaybillStatus.RECEIVE;
        List<ShowTruckDto> truckDtos = new ArrayList<>();
        for (Integer truckId : dto.getTruckIds()) {
            ShowTruckDto trucks = truckClientService.getTruckByIdReturnObject(truckId);
            if (trucks == null) {
                log.info("O grabWaybillToTrucks ={}", truckId);
                continue;
            }
            truckDtos.add(trucks);
        }
        if (CollectionUtils.isEmpty(truckDtos)) {
            throw new RuntimeException("当前选择的车辆无效请重新选择车辆！");
        }
        //如果重新派单 删除抢单记录
        grabWaybillRecordMapper.deleteByPrimaryKey(waybill.getId());
        //更新订单状态：从已配载(STOWAGE)改为运输中(TRANSPORT)
        Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        orders.setId(waybill.getOrderId());
        orders.setOrderStatus(OrderStatus.TRANSPORT);
        orders.setModifyTime(new Date());
        orders.setModifyUserId(getUserId());
        ordersMapper.updateByPrimaryKeySelective(orders);
        //更新运单
        waybill
                .setModifyTime(new Date())
                .setSendTime(new Date())
                .setModifyUserId(getUserId())
                .setWaybillStatus(waybillNewStatus);
        waybillMapper.updateByPrimaryKeySelective(waybill);
        //更新运单轨迹
        WaybillTracking waybillTracking = new WaybillTracking();
        waybillTracking
                .setEnabled(true)
                .setWaybillId(waybill.getId())
                .setCreateTime(new Date())
                .setOldStatus(waybillOldStatus)
                .setNewStatus(waybillNewStatus)
                .setTrackingInfo("已派单 ，运单号为【" + waybill.getId() + "】")
                .setReferUserId(getUserId());
        waybillTrackingMapper.insertSelective(waybillTracking);
        //向司机推送jpush消息
        //装货地
        ShowSiteDto loadSite = customerClientService.getShowSiteById(orders.getLoadSiteId());
        String loadSiteName = loadSite.getSiteName();
        List<WaybillItems> waybillItems = waybillItemsMapper.listWaybillItemsByWaybillId(waybill.getId());
        StringBuffer unLoadSiteNames = new StringBuffer();
        for (WaybillItems waybillItem : waybillItems) {
            //卸货地
            ShowSiteDto unLoadSite = customerClientService.getShowSiteById(waybillItem.getUnloadSiteId());
            unLoadSiteNames.append(unLoadSite.getSiteName() + "、");
        }
        String unloadSiteName = unLoadSiteNames.substring(0, unLoadSiteNames.length() - 1);
        String alias = "";
        String msgTitle = "派单消息";
        String msgContent;
        List<GrabWaybillRecord> grabWaybillRecords = new ArrayList<>();
        for (ShowTruckDto truckDto : truckDtos) {
            List<ShowDriverDto> drivers = truckDto.getDrivers();
            if (!CollectionUtils.isEmpty(drivers)) {
                for (ShowDriverDto driver : drivers) {
                    //插入抢单模式记录表
                    GrabWaybillRecord grabWaybillRecord = new GrabWaybillRecord();
                    grabWaybillRecord
                            .setWaybillId(waybill.getId())
                            .setCreateTime(new Date())
                            .setCreateUserId(getUserId())
                            .setTruckId(truckDto.getId())
                            .setDriverId(driver.getId())
                            .setStatus(GrabWaybillStatusType.NOT_ROB);
                    grabWaybillRecords.add(grabWaybillRecord);
                    //推送jpush消息
                    Map<String, String> extraParam = new HashMap<>();
                    extraParam.put("needVoice", "y");
                    extraParam.put("waybillId", waybill.getId().toString());
                    extraParam.put("driverId", driver.getId().toString());
                    msgContent = "您有新的运单信息待接单，从" + loadSiteName + "到" + unloadSiteName + "的运单。";
                    if (driver.getRegistrationId() != null) {
                        JpushClientUtil.sendPush(alias, msgTitle, msgContent, driver.getRegistrationId(), extraParam);
                    }
                    //插入消息
                    Message appMessage = new Message();
                    appMessage.setReferId(dto.getWaybillId());
                    // 消息类型：1-系统通知 2-运单相关 3-账务相关
                    appMessage.setMsgType(MsgType.WAYBILL);
                    //消息来源:1-APP,2-小程序,3-站内
                    appMessage.setMsgSource(MsgSource.APP);
                    appMessage.setMsgStatus(MsgStatusConstant.UNREAD);
                    appMessage.setHeader(WaybillConstant.NEW__WAYBILL_FOR_RECEIVE);
                    appMessage.setBody("您有新的运单信息待接单,从" + loadSiteName + "到" + unloadSiteName + "的运单。");
                    appMessage.setCreateTime(new Date());
                    appMessage.setCreateUserId(getUserId());
                    appMessage.setFromUserId(getUserId());
                    appMessage.setToUserId(driver.getId());
                    messageMapper.insertSelective(appMessage);
                }
            }
        }
        if (!CollectionUtils.isEmpty(grabWaybillRecords)) {
            grabWaybillRecordMapper.batchInsert(grabWaybillRecords);
        }
    }

    /**
     * 撤销抢单
     *
     * @param waybillId
     * @author @Gao.
     */
    @Override
    public void withdrawGrabWaybill(Integer waybillId) {
        if (null == waybillId) {
            throw new RuntimeException("运单Id不能为空");
        }
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        if (null == waybill) {
            throw new RuntimeException("运单不存在");
        }
        if (!WaybillStatus.RECEIVE.equals(waybill.getWaybillStatus())) {
            throw new RuntimeException("只有待接单的运单才可以撤回以便重新派单");
        }
        try {
            Integer userId = getUserId();
            //1.把所派车辆置空、状态改为带派单
            waybill.setTruckId(null);
            waybill.setWaybillStatus(WaybillStatus.SEND_TRUCK);
            waybill.setModifyTime(new Date());
            waybill.setModifyUserId(userId);
            waybillMapper.updateByPrimaryKey(waybill);
            //2.删除司机的短信
            GraWaybillConditionDto graWaybillConditionDto = new GraWaybillConditionDto();
            graWaybillConditionDto.setWaybillId(waybillId);
            List<GrabWaybillRecord> grabWaybillRecords = grabWaybillRecordMapper.listGrabWaybillByCondition(graWaybillConditionDto);
            Set<Integer> driverIdSet = new HashSet<>();
            for (GrabWaybillRecord grabWaybillRecord : grabWaybillRecords) {
                driverIdSet.add(grabWaybillRecord.getDriverId());
            }
            List<Integer> driverIds = new ArrayList<Integer>(driverIdSet);
            if (!CollectionUtils.isEmpty(driverIds)) {
                messageMapper.deleteMessage(waybillId, driverIds);
            }
            //3.删除抢单记录表
            grabWaybillRecordMapper.deleteByPrimaryKey(waybillId);
        } catch (Exception ex) {
            log.error("删除司机短信失败,运单id:{},原因{}", waybillId, ex.getMessage());
            throw ex;
        }

    }

    /**
     * 获得当前用户的id
     *
     * @return
     */
    private Integer getUserId() {
        UserInfo userInfo = null;
        try {
            userInfo = currentUserService.getCurrentUser();
        } catch (Exception ex) {
            log.error("获取当前用户失败：currentUserService.getCurrentUser()");
            return 1;
        }
        if (null == userInfo) {
            return 1;
        } else {
            return userInfo.getId();
        }
    }
}
