package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillItems;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface WaybillItemsMapperExt extends WaybillItemsMapper {
    /**
     * 通过waybillId获取运单明细
     *
     * @param waybillId
     * @return
     */
    List<WaybillItems> listWaybillItemsByWaybillId(Integer waybillId);

    /**
     * 根据waybillId 签收状态 获取卸货地id
     * @param waybillItems
     * @return
     */
    List<Map<String,Long>> listWaybillIdIdAndSignStatus(WaybillItems waybillItems);
}