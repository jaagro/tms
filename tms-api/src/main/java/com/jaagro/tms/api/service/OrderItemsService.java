package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.order.CreateOrderItemsDto;
import com.jaagro.tms.api.dto.order.GetOrderItemsDto;

import java.util.List;
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

    /**
     * 修改
     *
     * @param itemsDto
     * @return
     */
    Map<String, Object> updateItems(CreateOrderItemsDto itemsDto);

    /**
     * 逻辑删除
     *
     * @param id
     */
    Map<String, Object> disableById(Integer id);

    /**
     * 为订单详情页面提供
     *
     * @param orderId
     * @return
     */
    List<GetOrderItemsDto> listByOrderId(Integer orderId);
}
