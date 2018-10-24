package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.driverapp.GetWaybillParamDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDetailDto;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillRefactorService {

    /**
     * 根据状态查询我的运单信息
     * @Author @Gao.
     * @param dto
     * @return
     */
    PageInfo listWaybillByStatus(GetWaybillParamDto dto);

    /**
     * 根据订单号获取运单详情列表
     * @Author Gavin
     * @param orderId
     * @return
     */
    List<GetWaybillDetailDto> listWaybillDetailByOrderId(Integer orderId);

    /**
     * 根据id查询运单相关的所有对象
     * @Author Gavin
     * @param waybillId
     * @return
     */
    GetWaybillDetailDto getWaybillDetailById(Integer waybillId);
}