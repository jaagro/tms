package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyDto;
import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyCondtion;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillAnomalyMapperExt extends WaybillAnomalyMapper {
    /**
     * 根据运单号Id 创建人id 查询异常信息
     * @param
     * @return
     */
   List<WaybillAnomalyDto> listWaybillAnomalyByCondition(WaybillAnomalyCondtion dto);

}