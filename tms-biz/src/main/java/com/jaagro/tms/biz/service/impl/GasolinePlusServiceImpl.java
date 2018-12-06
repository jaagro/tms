package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.peripheral.CreateGasolineRecordDto;
import com.jaagro.tms.api.dto.truck.ShowTruckDto;
import com.jaagro.tms.api.service.GasolinePlusService;
import com.jaagro.tms.biz.entity.GasolineRecord;
import com.jaagro.tms.biz.mapper.GasolineRecordMapperExt;
import com.jaagro.tms.biz.service.TruckClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        gasolineRecord
                .setDriverId(currentUser.getId() == null ? null : currentUser.getId())
                .setTruckId(truckByToken.getId() == null ? null : truckByToken.getId())
                .setTruckTeamId(truckByToken.getTruckTeamId() == null ? null : truckByToken.getTruckTeamId());
        BeanUtils.copyProperties(dto, gasolineRecord);
        gasolineRecordMapper.insert(gasolineRecord);
    }
}
