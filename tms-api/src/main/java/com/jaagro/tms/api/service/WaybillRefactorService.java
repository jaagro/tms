package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.driverapp.GetWaybillParamDto;
import com.jaagro.tms.api.dto.fee.ListTruckFeeCriteria;
import com.jaagro.tms.api.dto.fee.ListTruckFeeDto;
import com.jaagro.tms.api.dto.ocr.WaybillOcrDto;
import com.jaagro.tms.api.dto.waybill.GetWaybillDetailDto;
import com.jaagro.tms.api.dto.waybill.ListWebChatWaybillCriteriaDto;
import com.jaagro.tms.api.dto.waybill.WaybillImageChangeParamDto;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据ocr自动补录牧原运单
     *
     * @param map
     */
    void waybillSupplementByOcr(Map<String, String> map);

    /**
     * 更改运单详情图片
     *
     * @param dto
     */
    void waybillImageChange(WaybillImageChangeParamDto dto);

    /**
     * 运力费用列表
     * @param criteria
     * @author yj
     * @return
     */
    PageInfo<ListTruckFeeDto> listTruckFeeByCriteria(ListTruckFeeCriteria criteria);
}