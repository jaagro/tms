package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.order.CreateOrderGoodsDto;
import com.jaagro.tms.api.dto.order.GetOrderGoodsDto;

import java.util.List;
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

    /**
     * 根据订单需求id获得订单需求明细列表
     *
     * @param id
     * @return
     */
    List<GetOrderGoodsDto> listGoodsDtoByItemId(Integer id);
}
