package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.driverapp.GetWaybillParamDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDetailDto;
import com.jaagro.tms.api.dto.waybill.ListWebChatWaybillCriteriaDto;

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
     * @Author @Gao.
     */
    PageInfo listWaybillByStatus(GetWaybillParamDto dto);

    /**
     * 根据订单号获取运单详情列表
     *
     * @param orderId
     * @return
     * @Author Gavin
     */
    List<GetWaybillDetailDto> listWaybillDetailByOrderId(Integer orderId);

    /**
     * 根据id查询运单相关的所有对象
     *
     * @param waybillId
     * @return
     * @Author Gavin
     */
    GetWaybillDetailDto getWaybillDetailById(Integer waybillId);

    /**
     * 我的运单列表【装卸货端】
     *
     * @param criteriaDto
     * @return
     */
    PageInfo listWebChatWaybillByCriteria(ListWebChatWaybillCriteriaDto criteriaDto);
}