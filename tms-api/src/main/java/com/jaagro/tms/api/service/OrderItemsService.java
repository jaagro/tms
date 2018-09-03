package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.order.CreateOrderItemsDto;

import java.util.Map;

/**
 * @author baiyiran
 */
public interface OrderItemsService {

    /**
     * 新增
     *
     * @param orderItem
     * @return
     */
    Map<String, Object> createOrderItem(CreateOrderItemsDto orderItem);

}
