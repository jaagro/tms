package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.AnomalyImageTypeConstant;
import com.jaagro.tms.api.constant.CostType;
import com.jaagro.tms.api.constant.DeductCompensationRoleType;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.entity.*;
import com.jaagro.tms.biz.mapper.*;
import com.jaagro.tms.biz.service.CustomerClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author @Gao.
 */
@Service
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
        waybillAnomaly.setProcessingStatus("待处理");
        UserInfo currentUser = currentUserService.getCurrentUser();
        waybillAnomaly.setCreateUserId(currentUser.getId());
        waybillAnomalyMapper.insertSelective(waybillAnomaly);
        //插入异常图片表
        WaybillAnomalyImage waybillAnomalyImage = new WaybillAnomalyImage();
        waybillAnomalyImage
                .setAnomalyId(waybillAnomaly.getId())
                .setCreateUserId(currentUser.getId())
                .setWaybillId(dto.getWaybillId())
                .setImageUrl(dto.getImageUrl())
                .setImageType(AnomalyImageTypeConstant.APPLY);
        waybillAnomalyImageMapper.insertSelective(waybillAnomalyImage);
    }

    /**
     * 运单异常类型显示
     * Author @Gao.
     */
    @Override
    public List<WaybillAnomalTypeDto> displayAnormalType() {
        List<WaybillAnomalyType> waybillAnomalyTypes = waybillAnomalyTypeMapper.listAnomalyType();
        List<WaybillAnomalTypeDto> waybillAnomalTypeDtos = new ArrayList<>();
        WaybillAnomalTypeDto dto = new WaybillAnomalTypeDto();
        for (WaybillAnomalyType waybillAnomalyType : waybillAnomalyTypes) {
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
        List<WaybillAnomalyDto> waybillAnomaly = waybillAnomalyMapper.listWaybillAnomalyByCondition(dto);
        return waybillAnomaly;
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
                .setProcessorTime(new Date())
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
        List<String> imageUrl = dto.getImageUrl();
        for (String url : imageUrl) {
            WaybillAnomalyImage waybillAnomalyImage = new WaybillAnomalyImage();
            waybillAnomalyImage
                    .setCreateUserId(currentUser.getId())
                    .setImageType(AnomalyImageTypeConstant.PROCESS)
                    .setAnomalyId(dto.getAnomalId())
                    .setWaybillId(dto.getWaybillId()).setImageUrl(url);
            waybillAnomalyImageMapper.insertSelective(waybillAnomalyImage);
        }
        if (true == dto.getAdjustStatus()) {
            //是否涉及费用调整
            List<AnomalDeductCompensationDto> feeAdjust = dto.getFeeAdjust();
            for (int i = 0; i < feeAdjust.size(); i++) {
                AnomalDeductCompensationDto anomalDeductCompensationDto = feeAdjust.get(i);
                //扣款对象
                if (0 == i) {
                   feeAdjust(dto,currentUser.getId(),anomalDeductCompensationDto,CostType.DEDUCTION);
                }
                //赔偿对象
                if (1 == i) {
                   feeAdjust(dto,currentUser.getId(),anomalDeductCompensationDto,CostType.COMPENSATE);
                }
            }
        }

    }

    /**
     * 费用是否调整公共方法
     * Author @Gao.
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
