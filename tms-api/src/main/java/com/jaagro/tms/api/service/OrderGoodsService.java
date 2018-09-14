package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.order.CreateOrderGoodsDto;

import java.util.Map;

/**
 * @author baiyiran
 */
public interface OrderGoodsService {

    /**
     * 新增
     *
     * @param orderGoodsDto
     * @return
     */
    Map<String, Object> createOrderGood(CreateOrderGoodsDto orderGoodsDto);

    /**
     * 修改
     *
     * @param goodsDto
     * @return
     */
    Map<String, Object> updateGoods(CreateOrderGoodsDto goodsDto);

    /**
     * 根据itemId删除
     *
     * @param id
     */
    Map<String, Object> disableByItemsId(Integer itemId);
}
