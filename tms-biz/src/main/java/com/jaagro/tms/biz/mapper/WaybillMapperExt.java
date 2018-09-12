package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.GetWaybillDto;
import com.jaagro.tms.biz.entity.Waybill;

/**
 * @author tony
 */
public interface WaybillMapperExt extends WaybillMapper {

    /**
     * 从订单计划中移除waybill
     * @param waybillId
     * @return
     */
    Integer removeWaybillById(Integer waybillId);


}
