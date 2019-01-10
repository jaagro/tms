package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillEvaluate;

import java.util.List;

/**
 * @author @Gao.
 */
public interface WaybillEvaluateMapperExt extends WaybillEvaluateMapper {

    /**
     * 根据运单id查询运单评价信息
     *
     * @param waybillId
     * @return
     */
    WaybillEvaluate getWaybillEvaluateByWaybillId(Integer waybillId);

}