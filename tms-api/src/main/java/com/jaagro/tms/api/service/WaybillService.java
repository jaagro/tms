package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.CreateWaybillDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillPlanDto;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillService {

    /**
     * 创建配载计划
     * @param waybillPlanDto
     * @return
     */
    List<CreateWaybillDto> createWaybillPlan(CreateWaybillPlanDto waybillPlanDto);
}
