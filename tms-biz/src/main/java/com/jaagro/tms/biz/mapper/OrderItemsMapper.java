package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.order.GetOrderGoodsDto;
import com.jaagro.tms.api.dto.order.GetOrderItemsDto;
import com.jaagro.tms.biz.entity.OrderItems;

import java.util.List;

public interface OrderItemsMapper {
    /**
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int insert(OrderItems record);

    /**
     * @mbggenerated 2018-08-31
     */
    int insertSelective(OrderItems record);

    /**
     * @mbggenerated 2018-08-31
     */
    OrderItems selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(OrderItems record);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(OrderItems record);

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

}