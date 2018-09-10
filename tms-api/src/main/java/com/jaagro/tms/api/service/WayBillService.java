package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.GetWaybillParamDto;

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
}
