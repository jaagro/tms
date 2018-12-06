package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.peripheral.GasolineRecordCondtion;
import com.jaagro.tms.biz.entity.GasolineRecord;

import java.util.List;

public interface GasolineRecordMapperExt extends GasolineRecordMapper {

    List<GasolineRecord> listGasolineRecordByCondition(GasolineRecordCondtion condition);
}
