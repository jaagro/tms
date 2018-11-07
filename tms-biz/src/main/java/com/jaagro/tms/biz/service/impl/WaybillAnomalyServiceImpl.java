package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.AnomalyImageTypeConstant;
import com.jaagro.tms.api.constant.CostType;
import com.jaagro.tms.api.constant.DeductCompensationRoleType;
import com.jaagro.tms.api.constant.UserType;
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
                .setProcessingStatus("待处理")
                .setAuditStatus("待审核");
        UserInfo currentUser = currentUserService.getCurrentUser();
        waybillAnomaly.setCreateUserId(currentUser.getId());
        waybillAnomalyMapper.insertSelective(waybillAnomaly);
        //插入异常图片表
        List<String> imageUrl = dto.getImagesUrl() == null ? null : dto.getImagesUrl();
        if (null != imageUrl) {
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
    public List<WaybillAnomalTypeDto> displayAnormalType() {
        List<WaybillAnomalyType> waybillAnomalyTypes = waybillAnomalyTypeMapper.listAnomalyType();
        List<WaybillAnomalTypeDto> waybillAnomalTypeDtos = new ArrayList<>();
        for (WaybillAnomalyType waybillAnomalyType : waybillAnomalyTypes) {
            WaybillAnomalTypeDto dto = new WaybillAnomalTypeDto();
            BeanUtils.copyProperties(waybillAnomalyType, dto);
            waybillAnomalTypeDtos.add(dto);
        }
        return waybillAnomalTypeDtos;
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
    public List<WaybillAnomalyDto> listWaybillAnomalyByCondition(WaybillAnomalyCondtion dto) {
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
    public List<WaybillAnomalyImageDto> listWaybillAnormalyImageByCondtion(WaybillAnomalyImageCondtion dto) {
        List<WaybillAnomalyImage> waybillAnomalyImages = waybillAnomalyImageMapper.listWaybillAnormalyImageByCondtion(dto);
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
    public void anormalInformationProcess(AnomalInformationProcessDto dto) {
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
        waybillAnomalyMapper.updateByPrimaryKeySelective(waybillAnomaly);
        //批量插入图片
        List<String> imageUrl = dto.getImagesUrl() == null ? null : dto.getImagesUrl();
        if (null != imageUrl) {
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
        if (true == dto.getAdjustStatus()) {
            //是否涉及费用调整
            List<AnomalDeductCompensationDto> feeAdjust = dto.getFeeAdjust();
            if (!CollectionUtils.isEmpty(feeAdjust)) {
                for (int i = 0; i < feeAdjust.size(); i++) {
                    AnomalDeductCompensationDto anomalDeductCompensationDto = feeAdjust.get(i);
                    //扣款对象
                    if (0 == i) {
                        feeAdjust(dto, currentUser.getId(), anomalDeductCompensationDto, CostType.DEDUCTION);
                    }
                    //赔偿对象
                    if (1 == i) {
                        feeAdjust(dto, currentUser.getId(), anomalDeductCompensationDto, CostType.COMPENSATE);
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
    public PageInfo anomalManagementList(WaybillAnomalyCondtion dto) {
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyMapper.listWaybillAnomalyByCondition(dto);
        List<AnomalManagementListDto> anomalManagementListDtos = new ArrayList<>();
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
            AnomalManagementListDto anomalManagementListDto = new AnomalManagementListDto();
            BeanUtils.copyProperties(waybillAnomalyDto, anomalManagementListDto);
            WaybillFeeCondtion waybillFeeCondtion = new WaybillFeeCondtion();
            waybillFeeCondtion
                    .setAnomalyId(waybillAnomalyDto.getId());
            //客户侧费用
            try {
                List<WaybillCustomerFeeDto> waybillCustomerFeeDtos = waybillCustomerFeeMapper.listWaybillCustomerFeeByCondtion(waybillFeeCondtion);
                if (null != waybillCustomerFeeDtos.get(0)) {
                    WaybillCustomerFeeDto waybillCustomerFeeDto = waybillCustomerFeeDtos.get(0);
                    //客户侧赔偿
                    if (CostType.COMPENSATE == waybillCustomerFeeDto.getAdjustType()) {
                        anomalManagementListDto.setCompensateMoney("[客户] " + waybillCustomerFeeDto.getMoney());
                    }
                    //客户侧扣款
                    if (CostType.DEDUCTION == waybillCustomerFeeDto.getAdjustType()) {
                        anomalManagementListDto.setDeductMoney("[客户] " + "-" + waybillCustomerFeeDto.getMoney());
                    }
                }
            } catch (Exception e) {
                log.error("当前行取客户侧费用失败={}", e);
                e.printStackTrace();
            }
            //运力侧费用
            try {
                List<WaybillTruckFeeDto> waybillTruckFeeDtos = waybillTruckFeeMapper.listWaybillTruckFeeByCondtion(waybillFeeCondtion);
                if (null != waybillAnomalyDtos.get(0)) {
                    WaybillTruckFeeDto waybillTruckFeeDto = waybillTruckFeeDtos.get(0);
                    //运力侧赔偿
                    if (CostType.COMPENSATE == waybillTruckFeeDto.getAdjustType()) {
                        anomalManagementListDto.setCompensateMoney("[司机] " + waybillTruckFeeDto.getMoney());
                    }
                    //运力侧扣款
                    if (CostType.DEDUCTION == waybillTruckFeeDto.getAdjustType()) {
                        anomalManagementListDto.setDeductMoney("[司机] " + "-" + waybillTruckFeeDto.getMoney());
                    }
                }
            } catch (Exception e) {
                log.error("当前行获取运力侧费用失败={}", e);
                e.printStackTrace();
            }
            //登记人
            if (null != employeeLists && employeeLists.size() != 0) {
                if (UserType.EMPLOYEE.equals(waybillAnomalyDto.getCreateUserType())) {
                    UserInfo creatorName = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList()).get(0);
                    anomalManagementListDto.setCreatorName(creatorName.getName());
                }
            }
            if (null != driverLists && driverLists.size() != 0) {
                if (UserType.DRIVER.equals(waybillAnomalyDto.getCreateUserType())) {
                    UserInfo driverName = driverLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList()).get(0);
                    anomalManagementListDto.setCreatorName(driverName.getName());
                }
            }
            //处理人
            if (null != employeeLists && employeeLists.size() != 0 && null != waybillAnomalyDto.getProcessorUserId()) {
                UserInfo processUser = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getProcessorUserId())).collect(Collectors.toList()).get(0);
                anomalManagementListDto.setProcessorName(processUser.getName());
            }
            anomalManagementListDtos.add(anomalManagementListDto);
        }
        return new PageInfo(anomalManagementListDtos);
    }

    /**
     * 费用是否调整公共方法
     * Author @Gao.
     *
     * @param dto
     * @param currentUserId
     * @param anomalDeductCompensationDto
     * @param costType
     */
    private void feeAdjust(AnomalInformationProcessDto dto, Integer currentUserId,
                           AnomalDeductCompensationDto anomalDeductCompensationDto, Integer costType) {
        WaybillFeeAdjustment waybillFeeAdjustment = new WaybillFeeAdjustment();
        waybillFeeAdjustment
                .setCreatedUserId(currentUserId)
                .setAdjustReason(CostType.DAMAGE_FEE)
                .setWaybillId(dto.getWaybillId());
        //客户
        if (DeductCompensationRoleType.CUSTOMER == anomalDeductCompensationDto.getUserType()) {
            //插入客户费用表
            WaybillCustomerFee waybillCustomerFee = new WaybillCustomerFee();
            waybillCustomerFee
                    .setAnomalyId(dto.getAnomalId())
                    .setMoney(anomalDeductCompensationDto.getMoney())
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
        if (DeductCompensationRoleType.DRIVER == anomalDeductCompensationDto.getUserType()) {
            //插入司机费用表
            WaybillTruckFee waybillTruckFee = new WaybillTruckFee();
            waybillTruckFee
                    .setAnomalyId(dto.getAnomalId())
                    .setCostType(costType)
                    .setWaybillId(dto.getWaybillId())
                    .setMoney(anomalDeductCompensationDto.getMoney())
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
