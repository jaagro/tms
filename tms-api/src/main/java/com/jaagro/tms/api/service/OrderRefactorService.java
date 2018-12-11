package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.order.CreateOrderDto;
import com.jaagro.tms.api.dto.order.GetOrderDto;
import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;

import java.util.Map;

/**
 * @author baiyiran
 */
public interface OrderRefactorService {

    /**
     * 创建订单
     *
     * @param orderDto 入参json
     * @return
     */
    Map<String, Object> createOrder(CreateOrderDto orderDto);

    /**
     * 获取单条订单
     *
     * @param id 订单id
     * @return order对象
     */
    GetOrderDto getOrderById(Integer id);

    /**
     * 微信小程序 分页获取订单列表
     *
     * @param criteriaDto 查询条件 json
     * @return 订单列表
     */
    PageInfo listWeChatOrderByCriteria(ListOrderCriteriaDto criteriaDto);


}
