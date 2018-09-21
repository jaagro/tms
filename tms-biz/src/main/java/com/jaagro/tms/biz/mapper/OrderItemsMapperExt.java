package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.order.GetOrderItemsDto;
import com.jaagro.tms.biz.entity.OrderItems;

import java.util.List;

/**
 * @author tony
 */
public interface OrderItemsMapperExt extends OrderItemsMapper {

    /**
     * 根据订单id查询列表
     *
     * @param id
     * @return
     */
    List<OrderItems> listByOrderId(Integer id);

    /**
     * 提供给订单详情
     *
     * @param id
     * @return
     */
    List<GetOrderItemsDto> listItemsByOrderId(Integer id);

    /**
     * 根据订单逻辑删除
     *
     * @param orderId
     * @return
     */
    int disableByOrderId(Integer orderId);


}