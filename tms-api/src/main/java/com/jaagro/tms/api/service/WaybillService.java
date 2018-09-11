package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.*;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface WaybillService {

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
     * 创建订单计划
     * @param waybillDto
     * @return
     */
    List<ListWaybillPlanDto> createWaybillPlan(CreateWaybillPlanDto waybillDto);

    /**
     * 根据orderId获取订单计划
     * @param orderId
     * @return
     */
    Map<String, Object> getWaybillPlanByOrderId(Integer orderId);

    /**
     * 修改订单计划
     * @param waybillPlanDto
     * @return
     */
    Map<String, Object> updateWaybillPlan(CreateWaybillPlanDto waybillPlanDto);

    /**
     * 创建运单
     * @param waybillDto
     * @return
     */
    Map<String, Object> createWaybill(List<CreateWaybillDto> waybillDto);

    /**
     * 根据订单号获取运单列表
     * @param orderId
     * @return
     */
    List<GetWaybillDto> listWaybillByOrderId(Integer orderId);

    /**
     * 根据id获取waybill对象
     * @param id
     * @return
     */
    GetWaybillDto getWaybillById(Integer id);
}
