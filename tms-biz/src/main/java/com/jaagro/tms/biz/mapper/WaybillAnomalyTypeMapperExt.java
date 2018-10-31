package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.anomaly.WaybillAbnomalTypeDto;
import java.util.List;

/**
 * @author tony
 */
public interface WaybillAnomalyTypeMapperExt extends WaybillAnomalyTypeMapper {

    List<WaybillAbnomalTypeDto> listAnomalyType();

}