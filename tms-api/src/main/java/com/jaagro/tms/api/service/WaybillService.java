package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.CreateWaybillDto;
import com.jaagro.tms.api.dto.waybill.CreateWaybillPlanDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillParamDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillPlanDto;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface WayBillService {

    /**
     * 根据状态查询我的运单信息
     *
     * @return
     */
    Map<String, Object> listWaybillByStatus(GetWaybillParamDto dto);

    /**
     * 查询订单详情页
     *
     * @return
     */
    Map<String, Object> ListWayBillDetails(Integer waybillId);

    /**
     * 运单轨迹展示
     * @return
     */
    Map<String, Object> showWaybill(Integer waybillId);

    /**
     * 创建运单
     * @param waybillDtos
     * @return
     */
    Map<String,Object> createWaybill(List<CreateWaybillDto> waybillDtos);

    /**
     * 创建运单计划
     * @param waybillDto
     * @return
     */
    List<ListWaybillPlanDto> createWaybillPlan(CreateWaybillPlanDto waybillDto);
}
