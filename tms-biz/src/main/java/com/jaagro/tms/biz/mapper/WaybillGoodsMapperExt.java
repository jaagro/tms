package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.GetWaybillGoodsDto;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillGoodsMapperExt extends WaybillGoodsMapper {

    /**
     * 根据运单id获取所有goods列表
     * @param waybillId
     * @return
     */
    List<GetWaybillGoodsDto> listGoodsByWaybillId(Integer waybillId);
}