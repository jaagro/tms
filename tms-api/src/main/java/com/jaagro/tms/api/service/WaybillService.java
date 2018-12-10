package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.driverapp.*;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillGoodsDto;
import com.jaagro.tms.api.dto.receipt.UploadReceiptImageDto;
import com.jaagro.tms.api.dto.waybill.*;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface WaybillService {

    /**
     * 根据状态查询我的运单信息
     *
     * @param dto
     * @return
     */
    Map<String, Object> listWaybillByStatus(GetWaybillParamDto dto);

    /**
     * 查询订单详情页
     *
     * @param waybillId
     * @return
     */
    Map<String, Object> listWayBillDetails(Integer waybillId);

    /**
     * 运单轨迹展示
     *
     * @param waybillId
     * @returne
     */
    ShowWaybillTrackingDto showWaybillTrucking(Integer waybillId);

    /**
     * 更新运单轨迹
     *
     * @param dto
     * @return
     */
    Map<String, Object> upDateWaybillTrucking(GetWaybillTruckingParamDto dto);


    /**
     * 创建运单
     * Author gavin
     *
     * @param waybillDto
     * @return
     */
    Map<String, Object> createWaybill(List<CreateWaybillDto> waybillDto);

    /**
     * 根据id获取waybill对象
     *
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
     * @param dto
     * @return
     */
    Map<String, Object> receiptList(GetReceiptParamDto dto);

    /**
     * 接单消息列表显示
     *
     * @param dto
     * @return
     */
    Map<String, Object> receiptMessage(GetReceiptMessageParamDto dto);

    /**
     * 根据orderId获取order和waybill信息
     *
     * @param orderId
     * @return
     */
    GetWaybillPlanDto getOrderAndWaybill(Integer orderId);

    /**
     * Author gavin
     *
     * @param waybillId
     * @param truckId
     * @return
     */
    Map<String, Object> assignWaybillToTruck(Integer waybillId, Integer truckId);

    /**
     * 显示运单卸货
     *
     * @param waybillId
     * @return Author @Gao.
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
     *
     * @param
     * @return
     */
    Map<String, Object> showGoodsByWaybillItemId(Integer waybillId);


    Map<String, Object> showGoodsByWaybillId(Integer waybillItemId);

    /**
     * 个人中心
     *
     * @return
     */
    ShowPersonalCenter personalCenter();

    /**
     * 撤回待接单的运单
     *
     * @param waybillId
     * @return
     * @Author gavin
     */
    boolean withdrawWaybill(Integer waybillId);

    /**
     * 根据订单id获取运单
     *
     * @param orderId
     * @return
     */
    List<ListWaybillDto> listWaybillByOrderId(Integer orderId);

    /**
     * 根据订单id获取运单分页
     *
     * @param criteriaDto
     * @return
     */
    PageInfo listWaybillByCriteriaForWechat(ListWaybillCriteriaDto criteriaDto);

    /**
     * 根据订单id查询 已派单的运单
     *
     * @param id
     * @return
     */
    List<ListWaybillDto> listWaybillWaitByOrderId(Integer id);

    /**
     * 根据订单id查询已拒单的个数
     *
     * @param orderId
     * @return
     */
    Integer listRejectWaybillByOrderId(Integer orderId);

    /**
     * 回单修改提货信息
     *
     * @param updateWaybillGoodsDtoList
     * @return
     * @author yj
     */
    boolean updateLoadGoodsReceipt(List<UpdateWaybillGoodsDto> updateWaybillGoodsDtoList);

    /**
     * 回单修改卸货信息
     *
     * @param updateWaybillGoodsDtoList
     * @return
     * @author yj
     */
    boolean updateUnLoadGoodsReceipt(List<UpdateWaybillGoodsDto> updateWaybillGoodsDtoList);

    /**
     * 上传回单图片
     *
     * @param uploadReceiptImageDto
     * @return
     * @author yj
     */
    @Deprecated
    boolean uploadReceiptImage(UploadReceiptImageDto uploadReceiptImageDto);

    /**
     * 运单作废
     * 20181116
     *
     * @param waybillId
     * @return
     * @Author gavin
     */
    boolean abandonWaybill(Integer waybillId);

    /**
     * 根据订单id查询已完成的运单
     *
     * @param id
     * @return
     */
    Integer listFinishWaybillByOrderId(Integer id);

    /**
     * 根据订单id查询待派单的运单
     *
     * @param id
     * @return
     */
    Integer listWaitWaybillByOrderId(Integer id);
}
