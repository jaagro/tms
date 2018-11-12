package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyDto;
import com.jaagro.tms.biz.entity.WaybillAnomaly;
import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillAnomalyMapperExt extends WaybillAnomalyMapper {
    /**
     * 根据运单号Id 创建人id 查询异常信息
     * @param dto
     * @return
     */
   List<WaybillAnomalyDto> listWaybillAnomalyByCondition(WaybillAnomalyCondition dto);


    /**
     * 批量修改
     * @param records
     * @return
     */
    Integer batchUpdateByPrimaryKeySelective(@Param("records") List<WaybillAnomaly> records);
}