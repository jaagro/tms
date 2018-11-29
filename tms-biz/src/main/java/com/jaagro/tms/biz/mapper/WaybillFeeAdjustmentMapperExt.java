package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillFeeAdjustment;
import org.apache.ibatis.annotations.Param;

/**
 * @author tony
 */
public interface WaybillFeeAdjustmentMapperExt extends WaybillFeeAdjustmentMapper {
    /**
     * 根据 relevanceId 更新费用调整表
     * @param waybillFeeAdjustment
     * @return
     */
    void updateByRelevanceId(WaybillFeeAdjustment waybillFeeAdjustment);
}