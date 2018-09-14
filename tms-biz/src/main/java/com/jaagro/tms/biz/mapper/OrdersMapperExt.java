package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.order.ListOrderCriteriaDto;
import com.jaagro.tms.biz.entity.Orders;

import java.util.List;

/**
 * @author tony
 */
public interface OrdersMapperExt extends OrdersMapper{

    /**
     * 查询订单列表
     *
     * @param criteriaDto
     * @return
     */
    List<Orders> listByCriteria(ListOrderCriteriaDto criteriaDto);
}