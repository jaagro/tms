package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.driverapp.GetWaybillParamDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDto;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillRefactorService {

    /**
     * 根据状态查询我的运单信息
     *
     * @param dto
     * @return
     * @author @Gao.
     */
    PageInfo listWaybillByStatus(GetWaybillParamDto dto);

    /**
     * 根据订单号获取运单列表
     *
     * @param orderId
     * @return
     */
    List<GetWaybillDto> listWaybillByOrderId(Integer orderId);
}