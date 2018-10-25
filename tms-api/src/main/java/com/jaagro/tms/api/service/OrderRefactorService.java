package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.order.GetOrderDto;

/**
 * @author tony
 */
public interface OrderRefactorService {

    /**
     * 获取单条订单
     *
     * @param id 订单id
     * @return order对象
     */
    GetOrderDto getOrderById(Integer id);



}
