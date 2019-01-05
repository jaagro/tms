package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.driverapp.ShowTrackingDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.SmsClientService;
import com.jaagro.tms.biz.service.UserClientService;
import com.jaagro.utils.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author @Gao.
 */
@Service
@Slf4j
public class WaybillAnomalyServiceImpl implements WaybillAnomalyService {

    @Autowired
    private WaybillAnomalyMapperExt waybillAnomalyMapper;
    @Autowired
    private WaybillAnomalyTypeMapperExt waybillAnomalyTypeMapper;
    @Autowired
    private WaybillAnomalyImageMapperExt waybillAnomalyImageMapper;
    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private WaybillCustomerFeeMapperExt waybillCustomerFeeMapper;
    @Autowired
    private WaybillTruckFeeMapperExt waybillTruckFeeMapper;
    @Autowired
    private WaybillFeeAdjustmentMapperExt waybillFeeAdjustmentMapper;
    @Autowired
    private UserClientService userClientService;
    @Autowired
    private WaybillAnomalyLogMapperExt waybillAnomalyLogMapperExt;
    @Autowired
    private SmsClientService smsClientService;
    @Autowired
    private WaybillTrackingMapperExt waybillTrackingMapper;
    @Autowired
    private WaybillTrackingImagesMapperExt waybillTrackingImagesMapper;


    /**
     * 司机运单异常申报及新增
     * Author @Gao.
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void waybillAnomalySubmit(WaybillAnomalyReportDto dto) {
        if (null == dto.getWaybillId()) {
            throw new RuntimeException("运单号不能为空！");
        }
        Waybill waybill = waybillMapper.getWaybillById(dto.getWaybillId());
        if (null == waybill) {
            throw new RuntimeException("该运单号删除或不存在！");
        }
        //插入异常表
        WaybillAnomaly waybillAnomaly = new WaybillAnomaly();
        BeanUtils.copyProperties(dto, waybillAnomaly);
        waybillAnomaly
                .setProcessingStatus(AnomalyStatus.TO_DO);
        UserInfo currentUser = currentUserService.getCurrentUser();
        waybillAnomaly.setCreateUserId(currentUser.getId());
        waybillAnomalyMapper.insertSelective(waybillAnomaly);
        //插入异常图片表
        List<String> imageUrl = dto.getImagesUrl() == null ? null : dto.getImagesUrl();
        if (null != imageUrl && imageUrl.size() != 0) {
            for (String url : imageUrl) {
                WaybillAnomalyImage waybillAnomalyImage = new WaybillAnomalyImage();
                waybillAnomalyImage
                        .setCreateUserId(currentUser.getId())
                        .setAnomalyId(waybillAnomaly.getId())
                        .setWaybillId(dto.getWaybillId())
                        .setImageType(AnomalyImageTypeConstant.ADD)
                        .setImageUrl(url);
                waybillAnomalyImageMapper.insertSelective(waybillAnomalyImage);
            }
        }
    }

    /**
     * 运单异常类型显示
     * Author @Gao.
     */
    @Override
    public List<WaybillAnomalyTypeDto> displayAnomalyType() {
        List<WaybillAnomalyType> waybillAnomalyTypes = waybillAnomalyTypeMapper.listAnomalyType();
        List<WaybillAnomalyTypeDto> waybillAnomalyTypeDtos = new ArrayList<>();
        for (WaybillAnomalyType waybillAnomalyType : waybillAnomalyTypes) {
            WaybillAnomalyTypeDto dto = new WaybillAnomalyTypeDto();
            BeanUtils.copyProperties(waybillAnomalyType, dto);
            waybillAnomalyTypeDtos.add(dto);
        }
        return waybillAnomalyTypeDtos;
    }

    /**
     * 根据运单Id查询客户信息
     * Author @Gao.
     *
     * @param waybillId
     * @return
     */
    @Override
    public ShowCustomerDto getCustomerByWaybillId(Integer waybillId) {
        Waybill waybill = waybillMapper.getWaybillById(waybillId);
        if (null == waybill) {
            return null;
        }
        //根据订单id 查询客户信息
        Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        ShowCustomerDto customer = customerClientService.getShowCustomerById(orders.getCustomerId());
        return customer;
    }

    /**
     * 根据条件查询异常信息
     * Author @Gao.
     *
     * @param
     * @return
     */
    @Override
    public List<WaybillAnomalyDto> listWaybillAnomalyByCondition(WaybillAnomalyCondition dto) {
        return waybillAnomalyMapper.listWaybillAnomalyByCondition(dto);
    }

    /**
     * 根据条件查询图片信息
     * Author @Gao.
     *
     * @param
     * @return
     */
    @Override
    public List<WaybillAnomalyImageDto> listWaybillAnomalyImageByCondition(WaybillAnomalyImageCondition dto) {
        List<WaybillAnomalyImage> waybillAnomalyImages = waybillAnomalyImageMapper.listWaybillAnomalyImageByCondition(dto);
        List<WaybillAnomalyImageDto> waybillAnomalyImageDtos = new ArrayList<>();
        for (WaybillAnomalyImage waybillAnomalyImage : waybillAnomalyImages) {
            WaybillAnomalyImageDto waybillAnomalyImageDto = new WaybillAnomalyImageDto();
            BeanUtils.copyProperties(waybillAnomalyImage, waybillAnomalyImageDto);
            waybillAnomalyImageDtos.add(waybillAnomalyImageDto);
        }
        return waybillAnomalyImageDtos;
    }

    /**
     * 异常信息处理
     * Author @Gao.
     *
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void anomalyInformationProcess(AnomalyInformationProcessDto dto) {
        UserInfo currentUser = currentUserService.getCurrentUser();
        //插入异常审核信息
        WaybillAnomaly waybillAnomaly = new WaybillAnomaly();
        waybillAnomaly
                .setId(dto.getAnomalId())
                .setWaybillId(dto.getWaybillId())
                .setProcessorTime(new Date())
                .setProcessorUserId(currentUser.getId())
                .setProcessingDesc(dto.getProcessingDesc())
                .setProcessingStatus(dto.getProcessingStatus())
                .setVerifiedStatus(dto.getVerifiedStatus())
                .setAdjustStatus(dto.getAdjustStatus());
        //是否属实 是否涉及费用调整 为空 设置默认值
        if (null == dto.getVerifiedStatus() || null == dto.getAdjustStatus()) {
            waybillAnomaly
                    .setVerifiedStatus(false)
                    .setAdjustStatus(false);
        }
        waybillAnomalyMapper.updateByPrimaryKeySelective(waybillAnomaly);
        //批量插入图片
        List<String> imageUrl = dto.getImagesUrl() == null ? null : dto.getImagesUrl();
        if (null != imageUrl && imageUrl.size() != 0) {
            WaybillAnomalyImageCondition waybillAnomalyImageCondition = new WaybillAnomalyImageCondition();
            waybillAnomalyImageCondition
                    .setAnomalyId(dto.getAnomalId())
                    .setAnomalyImageType(AnomalyImageTypeConstant.PROCESS);
            waybillAnomalyImageMapper.deleteAnomalyImageByCondition(waybillAnomalyImageCondition);
            for (String url : imageUrl) {
                WaybillAnomalyImage waybillAnomalyImage = new WaybillAnomalyImage();
                waybillAnomalyImage
                        .setCreateUserId(currentUser.getId())
                        .setImageType(AnomalyImageTypeConstant.PROCESS)
                        .setAnomalyId(dto.getAnomalId())
                        .setWaybillId(dto.getWaybillId())
                        .setImageUrl(url);
                waybillAnomalyImageMapper.insertSelective(waybillAnomalyImage);
            }
        }
        //如果情况不属实 则返回
        if (false == dto.getVerifiedStatus()) {
            waybillAnomaly.setProcessingStatus(AnomalyStatus.INVALID);
            waybillAnomalyMapper.updateByPrimaryKeySelective(waybillAnomaly);
            return;
        }

        if (true == dto.getAdjustStatus() && true == dto.getVerifiedStatus()) {
            //是否涉及费用调整
            List<AnomalyDeductCompensationDto> feeAdjust = dto.getFeeAdjust();
            if (!CollectionUtils.isEmpty(feeAdjust)) {
                for (int i = 0; i < feeAdjust.size(); i++) {
                    AnomalyDeductCompensationDto anomalyDeductCompensationDto = feeAdjust.get(i);
                    //扣款对象
                    if (CostType.DEDUCTION.equals(anomalyDeductCompensationDto.getAdjustType())) {
                        feeAdjust(dto, currentUser.getId(), anomalyDeductCompensationDto, CostType.DEDUCTION);
                    }
                    //赔偿对象
                    if (CostType.COMPENSATE.equals(anomalyDeductCompensationDto.getAdjustType())) {
                        feeAdjust(dto, currentUser.getId(), anomalyDeductCompensationDto, CostType.COMPENSATE);
                    }
                }
            }
        }
        if (false == dto.getAdjustStatus()) {
            WaybillCustomerFee waybillCustomerFee = waybillCustomerFeeMapper.selectByAnomalyId(dto.getAnomalId());
            if (null != waybillCustomerFee) {
                waybillCustomerFee.setEnabled(false);
                waybillCustomerFeeMapper.updateByPrimaryKeySelective(waybillCustomerFee);
                WaybillTruckFee waybillTruckFee = waybillTruckFeeMapper.selectByAnomalyId(dto.getAnomalId());
                WaybillFeeAdjustment waybillTruckFeeAdjustment = new WaybillFeeAdjustment();
                waybillTruckFeeAdjustment
                        .setRelevanceId(waybillTruckFee.getId())
                        .setRelevanceType(DeductCompensationRoleType.DRIVER)
                        .setEnabled(false);
                waybillFeeAdjustmentMapper.updateByRelevanceId(waybillTruckFeeAdjustment);
                waybillTruckFee.setEnabled(false);
                waybillTruckFeeMapper.updateByPrimaryKeySelective(waybillTruckFee);
                WaybillFeeAdjustment waybillCustomerFeeAdjustment = new WaybillFeeAdjustment();
                waybillCustomerFeeAdjustment
                        .setRelevanceType(DeductCompensationRoleType.CUSTOMER)
                        .setRelevanceId(waybillCustomerFee.getId())
                        .setEnabled(false);
                waybillFeeAdjustmentMapper.updateByRelevanceId(waybillCustomerFeeAdjustment);
            }
        }
    }

    /**
     * 异常管理列表
     * Author @Gao.
     *
     * @param dto
     * @return
     */
    @Override
    public PageInfo anomalyManagementList(WaybillAnomalyCondition dto) {
        if (null == dto.getAudit()) {
            dto.setAudit(2);
        }
        if (null != dto.getPageNum() && null != dto.getPageSize()) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        }
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyMapper.listWaybillAnomalyByCondition(dto);
        List<Integer> driverList = new ArrayList<>();
        List<Integer> employeeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(waybillAnomalyDtos)) {
            for (WaybillAnomalyDto waybillAnomalyDto : waybillAnomalyDtos) {
                if (UserType.EMPLOYEE.equals(waybillAnomalyDto.getCreateUserType())) {
                    employeeList.add(waybillAnomalyDto.getCreateUserId());
                }
                if (UserType.DRIVER.equals(waybillAnomalyDto.getCreateUserType())) {
                    driverList.add(waybillAnomalyDto.getCreateUserId());
                }
                if (null != waybillAnomalyDto.getProcessorUserId()) {
                    employeeList.add(waybillAnomalyDto.getProcessorUserId());
                }
                if (null != waybillAnomalyDto.getAuditUserId()) {
                    employeeList.add(waybillAnomalyDto.getAuditUserId());
                }
            }
        }
        List<UserInfo> driverLists = new ArrayList<>();
        if (null != driverList && driverList.size() != 0) {
            try {
                driverLists = userClientService.listUserInfo(driverList, UserType.DRIVER);
            } catch (Exception e) {
                log.error("获取司机基本信息失败={}", e);
                e.printStackTrace();
                throw new RuntimeException("获取司机基本信息失败");
            }
        }
        List<UserInfo> employeeLists = new ArrayList<>();
        if (null != employeeList && employeeList.size() != 0) {
            try {
                employeeLists = userClientService.listUserInfo(employeeList, UserType.EMPLOYEE);
            } catch (Exception e) {
                log.error("获取员工基本信息失败={}", e);
                e.printStackTrace();
                throw new RuntimeException("获取员工基本信息失败");
            }
        }
        for (WaybillAnomalyDto waybillAnomalyDto : waybillAnomalyDtos) {
            WaybillFeeCondition waybillFeeCondition = new WaybillFeeCondition();
            waybillFeeCondition
                    .setAnomalyId(waybillAnomalyDto.getId());
            //客户侧费用
            List<WaybillCustomerFeeDto> waybillCustomerFeeDtos = waybillCustomerFeeMapper.listWaybillCustomerFeeByCondition(waybillFeeCondition);
            if (!CollectionUtils.isEmpty(waybillCustomerFeeDtos)) {
                WaybillCustomerFeeDto waybillCustomerFeeDto = waybillCustomerFeeDtos.get(0);
                if (CostType.COMPENSATE.equals(waybillCustomerFeeDto.getAdjustType())) {
                    waybillAnomalyDto.setCompensateMoney("[客户] " + waybillCustomerFeeDto.getMoney());
                }
                if (CostType.DEDUCTION.equals(waybillCustomerFeeDto.getAdjustType())) {
                    waybillAnomalyDto.setDeductMoney("[客户] " + "-" + waybillCustomerFeeDto.getMoney());
                }
            }
            //运力侧费用
            List<WaybillTruckFeeDto> waybillTruckFeeDtos = waybillTruckFeeMapper.listWaybillTruckFeeByCondition(waybillFeeCondition);
            if (!CollectionUtils.isEmpty(waybillTruckFeeDtos)) {
                WaybillTruckFeeDto waybillTruckFeeDto = waybillTruckFeeDtos.get(0);
                if (CostType.COMPENSATE.equals(waybillTruckFeeDto.getAdjustType())) {
                    waybillAnomalyDto.setCompensateMoney("[司机] " + waybillTruckFeeDto.getMoney());
                }
                if (CostType.DEDUCTION.equals(waybillTruckFeeDto.getAdjustType())) {
                    waybillAnomalyDto.setDeductMoney("[司机] " + "-" + waybillTruckFeeDto.getMoney());
                }
            }
            //登记人
            if (!CollectionUtils.isEmpty(employeeLists) && null != waybillAnomalyDto.getCreateUserId()) {
                if (UserType.EMPLOYEE.equals(waybillAnomalyDto.getCreateUserType())) {
                    UserInfo creatorName = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList()).get(0);
                    waybillAnomalyDto.setCreatorName(creatorName.getName());
                }
            }
            if (!CollectionUtils.isEmpty(driverLists) && null != waybillAnomalyDto.getCreateUserId()) {
                if (UserType.DRIVER.equals(waybillAnomalyDto.getCreateUserType())) {
                    UserInfo driverName = driverLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList()).get(0);
                    waybillAnomalyDto.setCreatorName(driverName.getName());
                }
            }
            //处理人
            if (!CollectionUtils.isEmpty(employeeLists) && null != waybillAnomalyDto.getProcessorUserId()) {
                UserInfo processUser = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getProcessorUserId())).collect(Collectors.toList()).get(0);
                waybillAnomalyDto.setProcessorName(processUser.getName());
            }
            //审核人
            if (!CollectionUtils.isEmpty(employeeLists) && null != waybillAnomalyDto.getAuditUserId()) {
                UserInfo auditName = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getAuditUserId())).collect(Collectors.toList()).get(0);
                waybillAnomalyDto.setAuditName(auditName.getName());
            }
        }
        return new PageInfo(waybillAnomalyDtos);
    }

    /**
     * 客户侧费用
     *
     * @param dto
     */
    @Override
    public List<WaybillCustomerFeeDto> listWaybillCustomerFeeByCondition(WaybillFeeCondition dto) {
        return waybillCustomerFeeMapper.listWaybillCustomerFeeByCondition(dto);
    }

    /**
     * 运力侧费用
     *
     * @param dto
     */
    @Override
    public List<WaybillTruckFeeDto> listWaybillTruckFeeByCondition(WaybillFeeCondition dto) {
        return waybillTruckFeeMapper.listWaybillTruckFeeByCondition(dto);
    }

    /**
     * 改变异常状态:入参为已处理则将状态改为待审核，入参为待审核则将状态改为已处理
     *
     * @param nowStatus 当前状态
     * @return
     * @author tony
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean changeAnomalyStatus(int[] ids, String nowStatus) {

        UserInfo currentUser = currentUserService.getCurrentUser();
        List<WaybillAnomaly> waybillAnomalyList = new LinkedList<>();
        List<WaybillAnomalyLog> waybillAnomalyLogList = new LinkedList<>();
        for (int id : ids) {
            WaybillAnomaly waybillAnomaly = waybillAnomalyMapper.selectByPrimaryKey(id);
            if (null == waybillAnomaly) {
                throw new NullPointerException(id + ":记录不存在");
            }
            if (!nowStatus.equals(waybillAnomaly.getProcessingStatus())) {
                throw new NullPointerException("状态不满足条件");
            }
            WaybillAnomaly record = new WaybillAnomaly();
            record.setId(id);
            switch (nowStatus) {
                case AnomalyStatus.DONE:
                    //判断是否需要审核
                    if (!waybillAnomaly.getAdjustStatus()) {
                        //当前异常为该类型 则可以撤派单并可以进入审核流程
                        if (CancelAnomalyWaybillType.CANCEL_WAYBILL.equals(waybillAnomaly.getAnomalyTypeId())) {
                            cancelWaybill(waybillAnomaly, currentUser);
                        }
                        // 更新状态为已结束
                        record.setProcessingStatus(AnomalyStatus.FINISH);
                    } else {
                        //更新状态为待审核
                        record.setAuditStatus(AnomalyStatus.TO_AUDIT);
                        record.setProcessingStatus(AnomalyStatus.AUDIT);
                    }
                    break;
                case AnomalyStatus.TO_AUDIT:
                    record.setAuditStatus(AnomalyStatus.REFUSE);
                    record.setProcessingStatus(AnomalyStatus.DONE);
                    break;
                default:
                    throw new NullPointerException("不满足操作条件");
            }
            //插入日志
            WaybillAnomalyLog waybillAnomalyLog = new WaybillAnomalyLog();
            waybillAnomalyLog.setCreateUserId(currentUser.getId());
            waybillAnomalyLog.setOldStatus(nowStatus);
            waybillAnomalyLog.setWaybillAnomalyId(record.getId());
            if (AnomalyStatus.DONE.equals(nowStatus)) {
                waybillAnomalyLog.setNewStatus(AnomalyStatus.TO_AUDIT);
                waybillAnomalyLog.setLogInfo("异常单被【" + currentUser.getName() + "】发送至【待审核】");
            }
            if (AnomalyStatus.TO_AUDIT.equals(nowStatus)) {
                waybillAnomalyLog.setNewStatus(AnomalyStatus.DONE);
                waybillAnomalyLog.setLogInfo("异常单被【" + currentUser.getName() + "】退回至【已处理】");
            }
            if (!waybillAnomaly.getAdjustStatus()) {
                waybillAnomalyLog.setNewStatus(AnomalyStatus.FINISH);
                waybillAnomalyLog.setLogInfo("异常单被【" + currentUser.getName() + "】发送至【已结束】");
            }
            waybillAnomalyList.add(record);
            waybillAnomalyLogList.add(waybillAnomalyLog);
        }
        waybillAnomalyMapper.batchUpdateByPrimaryKeySelective(waybillAnomalyList);
        waybillAnomalyLogMapperExt.batchInsert(waybillAnomalyLogList);
        return true;
    }

    /**
     * 异常审核
     * Author @Gao.
     *
     * @param dto
     * @return
     */
    @Override
    public void anomalyInformationAudit(AnomalyInformationAuditDto dto) {
        UserInfo currentUser = currentUserService.getCurrentUser();
        WaybillAnomaly waybillAnomaly = waybillAnomalyMapper.selectByPrimaryKey(dto.getId());
        waybillAnomaly
                .setAuditUserId(currentUser.getId())
                .setAuditTime(new Date());
        //审核通过
        if (AnomalyStatus.OK.equals(dto.getAuditStatus())) {
            waybillAnomaly.setAuditStatus(AnomalyStatus.AUDIT_APPROVAL);
            waybillAnomaly.setProcessingStatus(AnomalyStatus.FINISH);
        }
        //审核拒绝
        if (AnomalyStatus.NO.equals(dto.getAuditStatus())) {
            waybillAnomaly.setAuditStatus(AnomalyStatus.AUDIT_REFUSEL);
            waybillAnomaly.setProcessingStatus(AnomalyStatus.DONE);
        }
        waybillAnomalyMapper.updateByPrimaryKeySelective(waybillAnomaly);
    }

    /**
     * 特殊运单异常重置派单
     * Author @Gao.
     *
     * @param waybillAnomaly
     */
    private void cancelWaybill(WaybillAnomaly waybillAnomaly, UserInfo currentUser) {
        Map<String, Object> templateMap = new HashMap<>();
        if (waybillAnomaly.getWaybillId() != null) {
            Integer waybillId = waybillAnomaly.getWaybillId();
            Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
            //发送短信
            if (waybill.getDriverId() != null) {
                BaseResponse<UserInfo> globalUser = userClientService.getGlobalUser(waybill.getDriverId());
                if (globalUser.getData() != null) {
                    UserInfo driver = globalUser.getData();
                    templateMap.put("driver", driver.getName());
                    templateMap.put("waybillId", waybill.getId());
                    smsClientService.sendSMS(driver.getPhoneNumber(), "SMS_154586744", templateMap);
                }
            }
            List<Integer> waybillTrackingIds = waybillTrackingMapper.listWaybillTrackingIdByWaybillId(waybillId);
            //批量逻辑删除
            waybillTrackingMapper.deleteWaybillTrackingId(waybillTrackingIds);
            //删除运单轨迹关联图片
            waybillTrackingImagesMapper.deleteByWaybillIdAndImageType(waybillId, null);
            WaybillTracking waybillTracking = new WaybillTracking();
            waybillTracking
                    .setWaybillId(waybillId)
                    .setTrackingType(TrackingType.ANOMALY_WAYBILL_RESET)
                    .setReferUserId(currentUser.getId())
                    .setNewStatus(WaybillStatus.SEND_TRUCK)
                    .setOldStatus(waybill.getWaybillStatus())
                    .setTrackingInfo("运单号" + waybillId + "出现运单异常已重置派单");
            waybillTrackingMapper.insertSelective(waybillTracking);
            Waybill wb = new Waybill();
            wb.setId(waybillId)
                    .setWaybillStatus(WaybillStatus.SEND_TRUCK)
                    .setDriverId(null)
                    .setTruckId(null);
            waybillMapper.updateCancelWaybillById(wb);
        }
    }

    /**
     * 费用是否调整公共方法
     * Author @Gao.
     *
     * @param dto
     * @param currentUserId
     * @param anomalyDeductCompensationDto
     * @param costType
     */
    private void feeAdjust(AnomalyInformationProcessDto dto, Integer currentUserId,
                           AnomalyDeductCompensationDto anomalyDeductCompensationDto, Integer costType) {
        WaybillFeeAdjustment waybillFeeAdjustment = new WaybillFeeAdjustment();
        waybillFeeAdjustment
                .setCreatedUserId(currentUserId)
                .setAdjustReason(CostType.DAMAGE_FEE)
                .setWaybillId(dto.getWaybillId());
        //客户
        if (DeductCompensationRoleType.CUSTOMER.equals(anomalyDeductCompensationDto.getUserType())) {
            WaybillCustomerFee waybillCustomerFees = waybillCustomerFeeMapper.selectByAnomalyId(dto.getAnomalId());
            WaybillCustomerFee waybillCustomerFee = new WaybillCustomerFee();
            waybillCustomerFee
                    .setAnomalyId(dto.getAnomalId())
                    .setMoney(anomalyDeductCompensationDto.getMoney())
                    .setEarningType(CostType.ADDITIONAL)
                    .setWaybillId(dto.getWaybillId())
                    .setCreatedUserId(currentUserId);
            if (null == waybillCustomerFees) {
                //插入客户费用表
                waybillCustomerFeeMapper.insertSelective(waybillCustomerFee);
                waybillFeeAdjustment
                        .setAdjustType(costType)
                        .setRelevanceId(waybillCustomerFee.getId())
                        .setRelevanceType(DeductCompensationRoleType.CUSTOMER);
                //插入费用调整表
                waybillFeeAdjustmentMapper.insertSelective(waybillFeeAdjustment);
            } else {
                waybillCustomerFees
                        .setMoney(anomalyDeductCompensationDto.getMoney())
                        .setEnabled(true);
                waybillCustomerFeeMapper.updateByPrimaryKeySelective(waybillCustomerFees);
                waybillFeeAdjustment
                        .setAdjustType(costType)
                        .setRelevanceId(waybillCustomerFees.getId())
                        .setRelevanceType(DeductCompensationRoleType.CUSTOMER)
                        .setEnabled(true);
                waybillFeeAdjustmentMapper.updateByRelevanceId(waybillFeeAdjustment);
            }
        }
        //司机
        if (DeductCompensationRoleType.DRIVER.equals(anomalyDeductCompensationDto.getUserType())) {
            WaybillTruckFee waybillTruckFees = waybillTruckFeeMapper.selectByAnomalyId(dto.getAnomalId());
            WaybillTruckFee waybillTruckFee = new WaybillTruckFee();
            waybillTruckFee
                    .setAnomalyId(dto.getAnomalId())
                    .setCostType(CostType.ADDITIONAL)
                    .setWaybillId(dto.getWaybillId())
                    .setMoney(anomalyDeductCompensationDto.getMoney())
                    .setCreatedUserId(currentUserId);
            if (null == waybillTruckFees) {
                //插入司机费用表
                waybillTruckFeeMapper.insertSelective(waybillTruckFee);
                waybillFeeAdjustment
                        .setAdjustType(costType)
                        .setRelevanceId(waybillTruckFee.getId())
                        .setRelevanceType(DeductCompensationRoleType.DRIVER);
                //插入费用调整表
                waybillFeeAdjustmentMapper.insertSelective(waybillFeeAdjustment);
            } else {
                waybillTruckFees
                        .setMoney(anomalyDeductCompensationDto.getMoney())
                        .setEnabled(true);
                waybillTruckFeeMapper.updateByPrimaryKeySelective(waybillTruckFees);
                waybillFeeAdjustment
                        .setAdjustType(costType)
                        .setRelevanceId(waybillTruckFees.getId())
                        .setRelevanceType(DeductCompensationRoleType.DRIVER)
                        .setEnabled(true);
                waybillFeeAdjustmentMapper.updateByRelevanceId(waybillFeeAdjustment);
            }
        }
    }
}
