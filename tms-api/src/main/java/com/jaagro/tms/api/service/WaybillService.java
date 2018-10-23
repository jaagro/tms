package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.driverapp.GetReceiptParamDto;
import com.jaagro.tms.api.dto.driverapp.GetWaybillTruckingParamDto;
import com.jaagro.tms.api.dto.waybill.*;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface WaybillService {

    /**
     * 查询订单详情页
     * @param waybillId
     * @return
     */
    Map<String, Object> listWayBillDetails(Integer waybillId);

    /**
     * 运单轨迹展示
     * @param waybillId
     * @returne
     */
    Map<String, Object> showWaybillTrucking(Integer waybillId);

    /**
     * 更新运单轨迹
     * @param dto
     * @return
     */
    Map<String, Object> upDateWaybillTrucking(GetWaybillTruckingParamDto dto);


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
     * @param dto
     * @return
     */
    Map<String, Object> receiptList(GetReceiptParamDto dto);

    /**
     * 接单消息列表显示
     * @param dto
     * @return
     */
    Map<String, Object> receiptMessage(GetReceiptMessageParamDto dto);

    /**
     * 根据orderId获取order和waybill信息
     * @param orderId
     * @return
     */
    GetWaybillPlanDto getOrderAndWaybill(Integer orderId);


    /**
     * Author gavin
     * @param waybillId
     * @param truckId
     * @return
     */
    Map<String, Object> assignWaybillToTruck(Integer waybillId,Integer truckId);

    /**
     *显示运单卸货
     * @param waybillId
     * @return
     * Author @Gao.
     */
    Map<String, Object> showUnloadSite(Integer waybillId);

    /**
     * 分页查询运单管理
     *
     * @param criteriaDto
     * @return
     */
    Map<String, Object> listWaybillByCriteria(ListWaybillCriteriaDto criteriaDto);

    /**
     * 根据waybillItemId 查询卸货地货物信息
     *
     * @param
     * @return
     * @Author @Gao.
     */
    Map<String, Object> showGoodsByWaybillItemId(Integer waybillId);

    /**
     * 根据waybillId 查询装货地货物信息
     *
     * @param
     * @return
     * @Author @Gao.
     */
    Map<String, Object> showGoodsByWaybillId(Integer waybillItemId);

    /**
     * 个人中心
     * @return
     */
    Map<String, Object> personalCenter();
}
