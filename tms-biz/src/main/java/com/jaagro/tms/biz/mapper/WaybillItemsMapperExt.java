package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillItems;

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
    List<WaybillItems> listWaybillItemsByWaybillId(Integer waybillId);
}