package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.base.DepartmentReturnDto;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;
import com.jaagro.tms.api.dto.truck.DriverReturnDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.*;
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
    @Autowired
    private DriverClientService driverClientService;
    @Autowired
    private TruckClientService truckClientService;
    @Autowired
    private GrabWaybillRecordMapperExt grabWaybillRecordMapper;
    @Autowired
    private WaybillItemsMapperExt waybillItemsMapper;


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
        WaybillAnomalyCondition waybillAnomalyCondition = new WaybillAnomalyCondition();
        waybillAnomalyCondition
                .setWaybillId(dto.getWaybillId())
                .setAnomalyTypeId(dto.getAnomalyTypeId());
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyMapper.listWaybillAnomalyByCondition(waybillAnomalyCondition);
        if (!CollectionUtils.isEmpty(waybillAnomalyDtos)) {
        }
        //插入异常表
        WaybillAnomaly waybillAnomaly = new WaybillAnomaly();
        BeanUtils.copyProperties(dto, waybillAnomaly);
        if (waybill.getNetworkId() != null) {
            waybillAnomaly
                    .setNetworkId(waybill.getNetworkId());
        }
        //获取车牌号
        if (waybill != null && waybill.getTruckId() != null) {
            ShowTruckDto truck = truckClientService.getTruckByIdReturnObject(waybill.getTruckId());
            if (truck != null && truck.getTruckNumber() != null) {
                waybillAnomaly.setTruckNo(truck.getTruckNumber());
            }
        }
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
    public List<WaybillAnomalyTypeDto> displayWaybillAnomalyType(Integer waybillId) {

        List<WaybillAnomalyType> waybillAnomalyTypes = waybillAnomalyTypeMapper.listAnomalyType();
        Waybill waybill = waybillMapper.selectByPrimaryKey(waybillId);
        WaybillAnomalyCondition waybillAnomalyCondition = new WaybillAnomalyCondition();
        waybillAnomalyCondition
                .setWaybillId(waybillId)
                .setAnomalyTypeId(CancelAnomalyWaybillType.CANCEL_WAYBILL);
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyMapper.listWaybillAnomalyByWaybillId(waybillAnomalyCondition);

        Iterator<WaybillAnomalyType> iterator = waybillAnomalyTypes.iterator();
        List<WaybillAnomalyTypeDto> waybillAnomalyTypeDtos = new ArrayList<>();
        while (iterator.hasNext()) {
            WaybillAnomalyType waybillAnomalyType = iterator.next();
            boolean flag = ((WaybillStatus.ACCOMPLISH.equals(waybill.getWaybillStatus()) && CancelAnomalyWaybillType.CANCEL_WAYBILL.equals(waybillAnomalyType.getId()))
                    || (!CollectionUtils.isEmpty(waybillAnomalyDtos) && CancelAnomalyWaybillType.CANCEL_WAYBILL.equals(waybillAnomalyType.getId())));
            if (flag) {
                continue;
            }
            WaybillAnomalyTypeDto dto = new WaybillAnomalyTypeDto();
            BeanUtils.copyProperties(waybillAnomalyType, dto);
            waybillAnomalyTypeDtos.add(dto);
        }
        return waybillAnomalyTypeDtos;
    }

    @Override
    public List<WaybillAnomalyTypeDto> displayAnomalyType() {

        List<WaybillAnomalyType> waybillAnomalyTypes = waybillAnomalyTypeMapper.listAnomalyType();
        Iterator<WaybillAnomalyType> iterator = waybillAnomalyTypes.iterator();
        List<WaybillAnomalyTypeDto> waybillAnomalyTypeDtos = new ArrayList<>();
        while (iterator.hasNext()) {
            WaybillAnomalyType waybillAnomalyType = iterator.next();
            WaybillAnomalyTypeDto dto = new WaybillAnomalyTypeDto();
            BeanUtils.copyProperties(waybillAnomalyType, dto);
            waybillAnomalyTypeDtos.add(dto);
        }
        return waybillAnomalyTypeDtos;
    }

    /**
     * 根据运单Id查询客户信息 司机信息
     * Author @Gao.
     *
     * @param waybillId
     * @return
     */
    @Override
    public AnomalyUserProfileDto getAnomalyUserProfileByWaybillId(Integer waybillId) {
        AnomalyUserProfileDto anomalyUserProfileDto = new AnomalyUserProfileDto();
        Waybill waybill = waybillMapper.getWaybillById(waybillId);
        if (null == waybill) {
            return null;
        }
        //根据订单id 查询客户信息
        Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
        ShowCustomerDto customer = customerClientService.getShowCustomerById(orders.getCustomerId());
        if (customer != null && customer.getCustomerName() != null) {
            anomalyUserProfileDto.setCustomerName(customer.getCustomerName());
        }
        //根据运单查询司机相关信息
        Integer driverId = waybill.getDriverId();
        DriverReturnDto driverByIdFeign = null;
        if (driverId != null) {
            driverByIdFeign = driverClientService.getDriverByIdFeign(driverId);
            if (driverByIdFeign != null && driverByIdFeign.getName() != null) {
                anomalyUserProfileDto.setDriverName(driverByIdFeign.getName());
            }
        }
        //查询该司机的车牌号
        ShowTruckDto truckByIdReturnObject = null;
        if (driverByIdFeign != null && driverByIdFeign.getTruckId() != null) {
            truckByIdReturnObject = truckClientService.getTruckByIdReturnObject(driverByIdFeign.getTruckId());

        }
        if (truckByIdReturnObject != null && truckByIdReturnObject.getTruckNumber() != null) {
            anomalyUserProfileDto.setTruckNumber(truckByIdReturnObject.getTruckNumber());
        }
        return anomalyUserProfileDto;
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
        List<Integer> downDepartment = userClientService.getDownDepartment();
        if (!CollectionUtils.isEmpty(downDepartment)) {
            dto.setNetworkIds(downDepartment);
        }
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyMapper.listWaybillAnomalyByCondition(dto);
        if (CollectionUtils.isEmpty(waybillAnomalyDtos)) {
            return null;
        }
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
        if (!CollectionUtils.isEmpty(driverList)) {
            try {
                driverLists = userClientService.listUserInfo(driverList, UserType.DRIVER);
            } catch (Exception e) {
                log.error("获取司机基本信息失败={}", e);
                e.printStackTrace();
                throw new RuntimeException("获取司机基本信息失败");
            }
        }
        List<UserInfo> employeeLists = new ArrayList<>();
        if (!CollectionUtils.isEmpty(employeeList)) {
            try {
                employeeLists = userClientService.listUserInfo(employeeList, UserType.EMPLOYEE);
            } catch (Exception e) {
                log.error("获取员工基本信息失败={}", e);
                e.printStackTrace();
                throw new RuntimeException("获取员工基本信息失败");
            }
        }
        // 获取所有项目部
        List<DepartmentReturnDto> departmentReturnDtos = userClientService.getAllDepartments();
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
                    UserInfo creatorName = null;
                    List<UserInfo> collect = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        creatorName = collect.get(0);
                    }
                    if (creatorName != null && creatorName.getName() != null) {
                        waybillAnomalyDto.setCreatorName(creatorName.getName());
                    }
                }
            }
            if (!CollectionUtils.isEmpty(driverLists) && null != waybillAnomalyDto.getCreateUserId()) {
                if (UserType.DRIVER.equals(waybillAnomalyDto.getCreateUserType())) {
                    UserInfo driverName = null;
                    List<UserInfo> collect = driverLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getCreateUserId())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        driverName = collect.get(0);
                    }
                    if (driverName != null && driverName.getName() != null) {
                        waybillAnomalyDto.setCreatorName(driverName.getName());
                    }
                }
            }
            //处理人
            if (!CollectionUtils.isEmpty(employeeLists) && null != waybillAnomalyDto.getProcessorUserId()) {
                List<UserInfo> collect = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getProcessorUserId())).collect(Collectors.toList());
                UserInfo processUser = null;
                if (!CollectionUtils.isEmpty(collect)) {
                    processUser = collect.get(0);
                }
                if (processUser != null && processUser.getName() != null) {
                    waybillAnomalyDto.setProcessorName(processUser.getName());
                }
            }
            //审核人
            if (!CollectionUtils.isEmpty(employeeLists) && null != waybillAnomalyDto.getAuditUserId()) {
                UserInfo auditName = null;
                List<UserInfo> collect = employeeLists.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getAuditUserId())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(collect)) {
                    auditName = collect.get(0);
                }
                if (auditName != null && auditName.getName() != null) {
                    waybillAnomalyDto.setAuditName(auditName.getName());
                }
            }
            //项目部
            if (waybillAnomalyDto.getNetworkId() == null) {
                waybillAnomalyDto.setProjectDeptName("其他");
            } else {
                if (!CollectionUtils.isEmpty(departmentReturnDtos)) {
                    DepartmentReturnDto departmentReturnDto = null;
                    List<DepartmentReturnDto> collect = departmentReturnDtos.stream().filter(c -> c.getId().equals(waybillAnomalyDto.getNetworkId())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(collect)) {
                        departmentReturnDto = collect.get(0);
                    }
                    if (departmentReturnDto != null && departmentReturnDto.getDepartmentName() != null) {
                        waybillAnomalyDto.setProjectDeptName(departmentReturnDto.getDepartmentName());
                    }
                }
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
            //删除抢单记录表
            grabWaybillRecordMapper.deleteByWaybillId(waybill.getId());
            //批量逻辑删除
            waybillTrackingMapper.deleteWaybillTrackingId(waybillTrackingIds);
            //删除运单轨迹关联图片
            waybillTrackingImagesMapper.deleteByWaybillIdAndImageType(waybillId, null);
            WaybillTracking waybillTracking = new WaybillTracking();
            waybillTracking
                    .setEnabled(true)
                    .setWaybillId(waybillId)
                    .setTrackingType(TrackingType.ANOMALY_WAYBILL_RESET)
                    .setReferUserId(currentUser.getId())
                    .setNewStatus(WaybillStatus.SEND_TRUCK)
                    .setOldStatus(waybill.getWaybillStatus())
                    .setTrackingInfo("异常运单" + waybillId + "申请重置派单已通过审核");
            waybillTrackingMapper.insertSelective(waybillTracking);
            //更改运单状态
            Waybill wb = new Waybill();
            wb.setId(waybillId)
                    .setWaybillStatus(WaybillStatus.SEND_TRUCK)
                    .setDriverId(null)
                    .setTruckId(null);
            waybillMapper.updateCancelWaybillById(wb);
            //更新运单为待签收状态
            waybillItemsMapper.updateWaybillItemsForUnSign(waybillId);
            //更改订单状态
            Orders orderUpdate = new Orders();
            orderUpdate
                    .setId(waybill.getOrderId())
                    .setModifyTime(new Date());
            List<Waybill> waybills = waybillMapper.listWaybillByOrderId(waybill.getOrderId());
            int count = 0;
            if (!CollectionUtils.isEmpty(waybills)) {
                for (Waybill w : waybills) {
                    boolean flag = WaybillStatus.DEPART.equals(w.getWaybillStatus()) || WaybillStatus.ARRIVE_LOAD_SITE.equals(w.getWaybillStatus())
                            || WaybillStatus.LOAD_PRODUCT.equals(w.getWaybillStatus()) || WaybillStatus.DELIVERY.equals(w.getWaybillStatus())
                            || WaybillStatus.RECEIVE.equals(w.getWaybillStatus());
                    if (flag) {
                        orderUpdate
                                .setOrderStatus(OrderStatus.TRANSPORT);
                        ordersMapper.updateByPrimaryKeySelective(orderUpdate);
                    }
                    if (WaybillStatus.SEND_TRUCK.equals(w.getWaybillStatus())) {
                        count++;
                        if (waybills.size() == count) {
                            orderUpdate
                                    .setOrderStatus(OrderStatus.STOWAGE);
                            ordersMapper.updateByPrimaryKeySelective(orderUpdate);
                        }
                    }
                }
            }
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
                    .setDirection(CostType.COMPENSATE.equals(costType) ? Direction.SUBSTRACT : Direction.PLUS)
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
                        .setDirection(CostType.COMPENSATE.equals(costType) ? Direction.SUBSTRACT : Direction.PLUS)
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
                    .setDirection(CostType.COMPENSATE.equals(costType) ? Direction.PLUS : Direction.SUBSTRACT)
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
                        .setDirection(CostType.COMPENSATE.equals(costType) ? Direction.PLUS : Direction.SUBSTRACT)
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
