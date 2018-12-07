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
                    .setTruckId(truckByToken.getId())
                    .setTruckTeamId(truckByToken.getTruckTeamId())
                    .setTruckNumber(truckByToken.getTruckNumber());
            BeanUtils.copyProperties(dto, gasolineRecord);
        }
        gasolineRecordMapper.insertSelective(gasolineRecord);
    }

    /**
     * 加油记录表
     *
     * @param driverId
     * @return
     */
    @Override
    public List<CreateGasolineRecordDto> listGasolineRecords(Integer driverId) {

        return null;
    }
}
