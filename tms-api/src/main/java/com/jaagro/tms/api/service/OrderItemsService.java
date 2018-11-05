package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.order.CreateOrderItemsDto;
import com.jaagro.tms.api.dto.order.GetOrderItemsDto;
import com.jaagro.tms.api.dto.order.ListOrderItemsDto;

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
     * 根据订单id删除
     *
     * @param orderId
     */
    Map<String, Object> disableByOrderId(Integer orderId);

    /**
     * 为订单详情页面提供
     *
     * @param orderId
     * @return
     */
    List<GetOrderItemsDto> listByOrderId(Integer orderId);

    /**
     * 根据订单id获得订单需求列表
     *
     * @param orderId
     * @return
     */
    List<ListOrderItemsDto> listItemsByOrderId(Integer orderId);
}
