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
     * 创建运单
     * Author gavin
     * @param waybillDto
     * @return
     */
    Map<String, Object> createWaybill(List<CreateWaybillDto> waybillDto);

    /**
     * 根据id获取waybill对象
     * @param id
     * @return
     */
    GetWaybillDto getWaybillById(Integer id);

    /**
     * 接单状态控制
     *
     * @param dto
     * @return
     */
    Map<String, Object> upDateReceiptStatus(GetReceiptParamDto dto);

    /**
     * 接单详情列表
     *
     * @return
     */
    Map<String, Object> receiptList(GetReceiptParamDto dto);

    /**
     * 接单消息列表显示
     * @return
     */
    Map<String, Object> receiptMessage(GetReceiptParamDto dto);

    /**
     * 根据orderId获取order和waybill信息
     * @param orderId
     * @return
     */
    GetWaybillPlanDto getOrderAndWaybill(Integer orderId);

    /**
     * 根据订单号获取运单列表
     *
     * @param orderId
     * @return
     */
    List<GetWaybillDto> listWaybillByOrderId(Integer orderId);

    /**
     * Author gavin
     * @param waybillId
     * @param truckId
     * @return
     */
    Map<String, Object> assignWaybillToTruck(Integer waybillId,Integer truckId);
}
