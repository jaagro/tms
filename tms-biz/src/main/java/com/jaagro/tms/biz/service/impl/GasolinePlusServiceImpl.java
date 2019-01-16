package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.peripheral.CreateGasolineRecordDto;
import com.jaagro.tms.api.dto.peripheral.GasolineRecordCondition;
import com.jaagro.tms.api.dto.peripheral.GasolineRecordParam;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.enums.GasolineCompanyNameEnum;
import com.jaagro.tms.api.enums.GasolineTypeEnum;
import com.jaagro.tms.api.enums.PaymentMethodEnum;
import com.jaagro.tms.api.service.GasolinePlusService;
import com.jaagro.tms.biz.entity.GasolineRecord;
import com.jaagro.tms.biz.mapper.GasolineRecordMapperExt;
import com.jaagro.tms.biz.service.TruckClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author @Gao.
 */
@Service
public class GasolinePlusServiceImpl implements GasolinePlusService {
    @Autowired
    private GasolineRecordMapperExt gasolineRecordMapper;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private TruckClientService truckClientService;


    /**
     * 我要加油服务
     *
     * @param dto
     * @author @Gao.
     */
    @Override
    public void gasolineApply(CreateGasolineRecordDto dto) {
        GasolineRecord gasolineRecord = new GasolineRecord();
        UserInfo currentUser = currentUserService.getCurrentUser();
        ShowTruckDto truckByToken = truckClientService.getTruckByToken();
        if (null != currentUser && null != truckByToken) {
            dto
                    .setDriverId(currentUser.getId())
                    .setDriverName(currentUser.getName())
                    .setTruckId(truckByToken.getId())
                    .setTruckTeamId(truckByToken.getTruckTeamId())
                    .setTruckNumber(truckByToken.getTruckNumber())
                    .setCreateUserId(currentUser.getId());
            BeanUtils.copyProperties(dto, gasolineRecord);
        }
        gasolineRecordMapper.insertSelective(gasolineRecord);
    }

    /**
     * 加油记录表
     *
     * @return
     */
    @Override
    public PageInfo<CreateGasolineRecordDto> listGasolineRecords(GasolineRecordParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        UserInfo currentUser = currentUserService.getCurrentUser();
        GasolineRecordCondition gasolineRecordCondition = new GasolineRecordCondition();
        if (null != currentUser) {
            gasolineRecordCondition.setDriverId(currentUser.getId());
        }
        List<CreateGasolineRecordDto> gasolineRecordDtos = gasolineRecordMapper.listGasolineRecordByCondition(gasolineRecordCondition);
        return new PageInfo(gasolineRecordDtos);
    }

    /**
     * 加油记录详情
     *
     * @param gasolineId
     * @return
     */
//    @Override
//    public List<CreateGasolineRecordDto> gasolineDetails(Integer gasolineId) {
//        GasolineRecordCondition gasolineRecordCondition = new GasolineRecordCondition();
//        gasolineRecordCondition.setId(gasolineId);
//        List<CreateGasolineRecordDto> gasolineRecordDtos = gasolineRecordMapper.listGasolineRecordByCondition(gasolineRecordCondition);
//        for (CreateGasolineRecordDto gasolineRecordDto : gasolineRecordDtos) {
//            if (null != gasolineRecordDto.getGasolineCompany()) {
//                gasolineRecordDto
//                        .setGasolineCompany(GasolineCompanyNameEnum.getTypeByDesc(gasolineRecordDto.getGasolineCompany()));
//            }
//            if (null != gasolineRecordDto.getGasolineType()) {
//                gasolineRecordDto
//                        .setGasolineType(GasolineTypeEnum.getTypeByDesc(gasolineRecordDto.getGasolineType()));
//            }
//            if (null != gasolineRecordDto.getPaymentMethod()) {
//                gasolineRecordDto.setPaymentMethod(PaymentMethodEnum.getTypeByDesc(gasolineRecordDto.getPaymentMethod()));
//            }
//        }
//        return gasolineRecordDtos;
//    }

    /**
     * 加油管理
     *
     * @param param
     * @return
     */
    @Override
    public PageInfo<CreateGasolineRecordDto> gasolineManagement(GasolineRecordParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        GasolineRecordCondition gasolineRecordCondition = new GasolineRecordCondition();
        //去除车牌号中首尾空格
        gasolineRecordCondition.setTruckNumber(StringUtils.hasText(param.getTruckNumber()) ? param.getTruckNumber().trim() : param.getTruckNumber());
        List<CreateGasolineRecordDto> gasolineRecordDtos = gasolineRecordMapper.listGasolineRecordByCondition(gasolineRecordCondition);
        for (CreateGasolineRecordDto gasolineRecordDto : gasolineRecordDtos) {
            if (null != gasolineRecordDto.getGasolineCompany()) {
                gasolineRecordDto
                        .setGasolineCompany(GasolineCompanyNameEnum.getTypeByDesc(gasolineRecordDto.getGasolineCompany()));
            }
        }
        return new PageInfo<>(gasolineRecordDtos);
    }
}
