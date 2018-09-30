package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.OrderItems;

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

}