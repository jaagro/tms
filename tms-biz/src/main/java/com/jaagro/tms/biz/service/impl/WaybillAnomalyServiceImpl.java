package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondtion;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.CustomerClientService;
import com.jaagro.tms.biz.service.UserClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.security.cert.CertSelector;
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
        Waybill waybill = waybillMapper.selectByPrimaryKey(dto.getWaybillId());
        if (null == waybill) {
            throw new RuntimeException("该运单号不存在！");
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
                        .setImageType(AnomalyImageTypeConstant.APPLY)
                        .setAnomalyId(waybillAnomaly.getId())
                        .setWaybillId(dto.getWaybillId())
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
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        if (null == waybill) {
            throw new RuntimeException("该运单号不存在！");
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
        WaybillAnomalyImageDto waybillAnomalyImageDto = new WaybillAnomalyImageDto();
        for (WaybillAnomalyImage waybillAnomalyImage : waybillAnomalyImages) {
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
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyMapper.listWaybillAnomalyByCondition(dto);
        List<AnomalyManagementListDto> anomalyManagementListDtos = new ArrayList<>();
        List<Integer> driverList = new ArrayList<>();
        List<Integer> employeeList = new ArrayList<>();
        for (WaybillAnomalyDto waybillAnomalyDto : waybillAnomalyDtos) {
            if (UserType.EMPLOYEE.equals(waybillAnomalyDto.getCreateUserType())) {
                employeeList.add(waybillAnomalyDto.getCreateUserId());
            }
            //审核人
            employeeList.add(waybillAnomalyDto.getProcessorUserId());
            if (UserType.DRIVER.equals(waybillAnomalyDto.getCreateUserType())) {
                driverList.add(waybillAnomalyDto.getCreateUserId());
            }
        }
        List<UserInfo> driverLists = new ArrayList<>();
        if (null != driverList && driverList.size() != 0) {
            try {
                driverLists = userClientService.listUserInfo(driverList, UserType.DRIVER);
            } catch (Exception e) {
                log.error("获取司机基本信息失败={}", e);
                e.printStackTrace();

            }
        }
        List<UserInfo> employeeLists = new ArrayList<>();
        if (null != employeeList && employeeList.size() != 0) {
            try {
                employeeLists = userClientService.listUserInfo(employeeList, UserType.EMPLOYEE);
            } catch (Exception e) {
                log.error("获取员工基本信息失败={}", e);
                e.printStackTrace();
            }
        }
        for (WaybillAnomalyDto waybillAnomalyDto : waybillAnomalyDtos) {
            AnomalyManagementListDto anomalyManagementListDto = new AnomalyManagementListDto();
            BeanUtils.copyProperties(waybillAnomalyDto, anomalyManagementListDto);
            WaybillFeeCondtion waybillFeeCondtion = new WaybillFeeCondtion();
            waybillFeeCondtion
                    .setAnomalyId(waybillAnomalyDto.getId());
            //客户侧费用
            List<WaybillCustomerFeeDto> waybillCustomerFeeDtos = waybillCustomerFeeMapper.listWaybillCustomerFeeByCondition(waybillFeeCondtion);
            if (!CollectionUtils.isEmpty(waybillCustomerFeeDtos)) {
                WaybillCustomerFeeDto waybillCustomerFeeDto = waybillCustomerFeeDtos.get(0);
                //客户侧赔偿
                if (CostType.COMPENSATE.equals(waybillCustomerFeeDto.getAdjustType())) {
                    anomalyManagementListDto.setCompensateMoney("[客户] " + waybillCustomerFeeDto.getMoney());
                }
                //客户侧扣款
                if (CostType.DEDUCTION.equals(waybillCustomerFeeDto.getAdjustType())) {
                    anomalyManagementListDto.setDeductMoney("[客户] " + "-" + waybillCustomerFeeDto.getMoney());
                }
            }
            //运力侧费用
            List<WaybillTruckFeeDto> waybillTruckFeeDtos = waybillTruckFeeMapper.listWaybillTruckFeeByCondtion(waybillFeeCondtion);
            if (!CollectionUtils.isEmpty(waybillTruckFeeDtos)) {
                WaybillTruckFeeDto waybillTruckFeeDto = waybillTruckFeeDtos.get(0);
                //运力侧赔偿
                if (CostType.COMPENSATE.equals(waybillTruckFeeDto.getAdjustType())) {
                    anomalyManagementListDto.setCompensateMoney("[司机] " + waybillTruckFeeDto.getMoney());
                }
                //运力侧扣款
                if (CostType.DEDUCTION.equals(waybillTruckFeeDto.getAdjustType())) {
                    anomalyManagementListDto.setDeductMoney("[司机] " + "-" + waybillTruckFeeDto.getMoney());
                }
            }
            //登记人
            if (!CollectionUtils.isEmpty(employeeLists) && null != waybillAnomalyDto.getCreateUserId()) {
                if (UserType.EMPLOYEE.equals(waybillAnomalyDto.getCreateUserType())) {
                    UserInfo creatorName = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList()).get(0);
                    anomalyManagementListDto.setCreatorName(creatorName.getName());
                }
            }
            if (!CollectionUtils.isEmpty(driverLists) && null != waybillAnomalyDto.getCreateUserId()) {
                if (UserType.DRIVER.equals(waybillAnomalyDto.getCreateUserType())) {
                    UserInfo driverName = driverLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList()).get(0);
                    anomalyManagementListDto.setCreatorName(driverName.getName());
                }
            }
            //处理人
            if (!CollectionUtils.isEmpty(employeeLists) && null != waybillAnomalyDto.getProcessorUserId()) {
                UserInfo processUser = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getProcessorUserId())).collect(Collectors.toList()).get(0);
                anomalyManagementListDto.setProcessorName(processUser.getName());
            }
            anomalyManagementListDtos.add(anomalyManagementListDto);
        }
        return new PageInfo(anomalyManagementListDtos);
    }

    /**
     * 客户侧费用
     *
     * @param dto
     */
    @Override
    public List<WaybillCustomerFeeDto> listWaybillCustomerFeeByCondition(WaybillFeeCondtion dto) {
        return waybillCustomerFeeMapper.listWaybillCustomerFeeByCondition(dto);
    }

    /**
     * 运力侧费用
     *
     * @param dto
     */
    @Override
    public List<WaybillTruckFeeDto> listWaybillTruckFeeByCondition(WaybillFeeCondtion dto) {
        return waybillTruckFeeMapper.listWaybillTruckFeeByCondtion(dto);
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

        for(int id : ids){
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
                        //更新状态为已结束
                        record.setProcessingStatus(AnomalyStatus.FINISH);
                    } else {
                        //更新状态为待审核
                        record.setAuditStatus(AnomalyStatus.TO_AUDIT);
                        record.setProcessingStatus(AnomalyStatus.TO_AUDIT);
                    }
                    break;
                case AnomalyStatus.TO_AUDIT:
                    record.setProcessingStatus(AnomalyStatus.DONE);
                    break;
                default:
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
                    break;
            }
        }
        waybillAnomalyMapper.batchUpdateByPrimaryKeySelective(waybillAnomalyList);
        waybillAnomalyLogMapperExt.batchInsert(waybillAnomalyLogList);
        return true;
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
            //插入客户费用表
            WaybillCustomerFee waybillCustomerFee = new WaybillCustomerFee();
            waybillCustomerFee
                    .setAnomalyId(dto.getAnomalId())
                    .setMoney(anomalyDeductCompensationDto.getMoney())
                    .setEarningType(CostType.ADDITIONAL)
                    .setWaybillId(dto.getWaybillId())
                    .setCreatedUserId(currentUserId);
            waybillCustomerFeeMapper.insertSelective(waybillCustomerFee);
            //插入费用调整表
            waybillFeeAdjustment
                    .setAdjustType(costType)
                    .setRelevanceId(waybillCustomerFee.getId())
                    .setRelevanceType(DeductCompensationRoleType.CUSTOMER);
        }
        //司机
        if (DeductCompensationRoleType.DRIVER.equals(anomalyDeductCompensationDto.getUserType())) {
            //插入司机费用表
            WaybillTruckFee waybillTruckFee = new WaybillTruckFee();
            waybillTruckFee
                    .setAnomalyId(dto.getAnomalId())
                    .setCostType(costType)
                    .setWaybillId(dto.getWaybillId())
                    .setMoney(anomalyDeductCompensationDto.getMoney())
                    .setCreatedUserId(currentUserId);
            waybillTruckFeeMapper.insertSelective(waybillTruckFee);
            //插入费用调整表
            waybillFeeAdjustment
                    .setAdjustType(costType)
                    .setRelevanceId(waybillTruckFee.getId())
                    .setRelevanceType(DeductCompensationRoleType.DRIVER);
        }
        waybillFeeAdjustmentMapper.insertSelective(waybillFeeAdjustment);
    }
}
