package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.GetWaybillItemsDto;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillItemsMapperExt extends WaybillItemsMapper {
    /**
     * 通过waybillId获取运单明细
     * @param waybillId
     * @return
     */
    List<GetWaybillItemsDto> listWaybillItemsByWaybillId(Integer waybillId);
}