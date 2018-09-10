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
     * @param dto
     * @return
     */
    Map<String, Object> listWaybillByStatus(GetWaybillParamDto dto);

    /**
     * 查询订单详情页
     * @param waybillId
     * @return
     */
    Map<String, Object> listWayBillDetails(Integer waybillId);

    /**
     * 运单轨迹展示
     * @param waybillId
     * @return
     */
    Map<String, Object> showWaybill(Integer waybillId);

    /**
     *
     * @param waybillDto
     * @return
     */
    List<ListWaybillPlanDto> createWaybillPlan(CreateWaybillPlanDto waybillDto);

    /**
     * 创建运单
     * @param waybillDto
     * @return
     */
    Map<String, Object> createWaybill(List<CreateWaybillDto> waybillDto);
}
