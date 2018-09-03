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

}
