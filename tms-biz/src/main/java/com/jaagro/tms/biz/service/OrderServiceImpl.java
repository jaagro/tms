package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.order.*;
import com.jaagro.tms.api.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author tony
 */
@Service
public class OrderServiceImpl implements OrderService {
    /**
     * 创建订单
     *
     * @param orderDto 入参json
     * @return
     */
    @Override
    public Map<String, Object> createOrder(CreateOrderDto orderDto) {
        return null;
    }

    /**
     * 修改订单
     *
     * @param orderDto 入参json
     * @return 修改后的order对象
     */
    @Override
    public GetOrderDto updateOrder(UpdateOrderDto orderDto) {
        return null;
    }

    /**
     * 获取单条订单
     *
     * @param id 订单id
     * @return order对象
     */
    @Override
    public GetOrderDto getOrderById(Integer id) {
        return null;
    }

    /**
     * 分页获取订单列表
     *
     * @param criteriaDto 查询条件 json
     * @return 订单列表
     */
    @Override
    public List<ListOrderDto> listOrderByCriteria(ListOrderCriteriaDto criteriaDto) {
        return null;
    }

    /**
     * 【逻辑】删除订单
     *
     * @param id 订单id
     * @return
     */
    @Override
    public Map<String, Object> deleteOrderById(Integer id) {
        return null;
    }
}
