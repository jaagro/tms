package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.order.*;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderDto 入参json
     * @return
     */
    Map<String, Object> createOrder(CreateOrderDto orderDto);

    /**
     * 修改订单
     *
     * @param orderDto 入参json
     * @return 修改后的order对象
     */
    GetOrderDto updateOrder(UpdateOrderDto orderDto);

    /**
     * 获取单条订单
     *
     * @param id 订单id
     * @return order对象
     */
    GetOrderDto getOrderById(Integer id);

    /**
     * 分页获取订单列表
     *
     * @param criteriaDto 查询条件 json
     * @return 订单列表
     */
    Map<String, Object> listOrderByCriteria(ListOrderCriteriaDto criteriaDto);

    /**
     * 【逻辑】删除订单
     *
     * @param id 订单id
     * @return
     */
    Map<String, Object> deleteOrderById(Integer id);

    /**
     * 取消订单
     *
     * @param orderId
     * @param detailInfo
     * @return
     */
    Map<String, Object> cancelOrders(Integer orderId, String detailInfo);

    /**
     * 根据客户id查询订单id数组
     *
     * @param customerId
     * @return
     */
    List<Integer> getOrderIdsByCustomerId(Integer customerId);

}
