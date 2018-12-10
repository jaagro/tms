package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.peripheral.CreateGasolineRecordDto;
import com.jaagro.tms.api.dto.peripheral.GasolineRecordCondition;

import java.util.List;

public interface GasolineRecordMapperExt extends GasolineRecordMapper {
    /**
     * 根据条件查询加油列表
     *
     * @param condition
     * @return
     */
    List<CreateGasolineRecordDto> listGasolineRecordByCondition(GasolineRecordCondition condition);
}
