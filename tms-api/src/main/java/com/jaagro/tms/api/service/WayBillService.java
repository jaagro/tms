package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.GetWaybillParamDto;

import java.util.Map;

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
}
