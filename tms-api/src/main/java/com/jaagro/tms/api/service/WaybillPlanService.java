package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.CreateWaybillPlanDto;

import java.util.Map;

/**
 * @author tony
 */
public interface WaybillPlanService {
    /**
     * 创建订单计划
     *
     * @param waybillDto
     * @return
     */
    //List<ListWaybillPlanDto> createWaybillPlan(CreateWaybillPlanDto waybillDto);
    Map<String,Object> createWaybillPlan(CreateWaybillPlanDto waybillDto);


    /**
     * 根据orderId获取订单计划
     *
     * @param orderId
     * @return
     */
    Map<String, Object> getWaybillPlanByOrderId(Integer orderId);

    /**
     * 从配载计划中移除运单【逻辑删除】
     *
     * @param waybillId
     * @return
     */
    Map<String, Object> removeWaybillFromPlan(Integer waybillId);
}
