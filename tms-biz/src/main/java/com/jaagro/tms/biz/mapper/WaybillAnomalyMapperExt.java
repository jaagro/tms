package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyDto;
import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyCondtion;
import com.jaagro.tms.biz.entity.WaybillAnomaly;

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


    /**
     * 批量修改
     * @param records
     * @return
     */
    Integer batchUpdateByPrimaryKeySelective(List<WaybillAnomaly> records);
}