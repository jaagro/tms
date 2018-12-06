package com.jaagro.tms.biz.service.impl;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.dto.driverapp.CreateGasolineRecordDto;
import com.jaagro.tms.api.service.GasolinePlusService;
import com.jaagro.tms.biz.entity.GasolineRecord;
import com.jaagro.tms.biz.mapper.GasolineRecordMapperExt;
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
        gasolineRecord
                .setDriverId(currentUser.getId());
        BeanUtils.copyProperties(dto, gasolineRecord);
        gasolineRecordMapper.insert(gasolineRecord);
    }
}
