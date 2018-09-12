package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.GetWaybillDto;

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

    /**
     * 通过id获取waybill对象
     * @param waybillId
     * @return
     */
    GetWaybillDto getWaybillById(Integer waybillId);
}
