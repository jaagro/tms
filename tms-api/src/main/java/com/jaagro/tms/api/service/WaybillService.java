package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.driverapp.GetWaybillTruckingParamDto;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillGoodsReceiptDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.dto.driverapp.GetReceiptParamDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDto;
import com.jaagro.tms.api.dto.driverapp.GetWaybillParamDto;

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
     * 根据waybillId 卸货地Id 查询货物信息
     * @param
     * @return
     */
    Map<String, Object> showGoodsByWaybillItemId(Integer waybillId);


    Map<String, Object> showGoodsByWaybillId(Integer waybillItemId);

    /**
     * 个人中心
     * @return
     */
    Map<String, Object> personalCenter();

    /**
     * 撤回待接单的运单
     * @Author gavin
     * @param waybillId
     * @return
     */
    boolean withdrawWaybill(Integer waybillId);

    /**
     * 回单修改运单货物信息新增运单轨迹(回单补录)
     * @author yj
     * @param updateWaybillGoodsReceiptDto
     * @return
     */
    boolean updateWaybillGoodsReceipt(UpdateWaybillGoodsReceiptDto updateWaybillGoodsReceiptDto);

    /**
     * 上传回单图片
     * @author yj
     * @param waybillId
     * @param imageUrl
     * @return
     */
    boolean uploadReceiptImage(Integer waybillId, String imageUrl);
}
