package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Orders;

public interface OrdersMapper {
    /**
     *
     * @mbggenerated 2018-12-12
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-12
     */
    int insert(Orders record);

    /**
     *
     * @mbggenerated 2018-12-12
     */
    int insertSelective(Orders record);

    /**
     *
     * @mbggenerated 2018-12-12
     */
    Orders selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-12
     */
    int updateByPrimaryKeySelective(Orders record);

    /**
     *
     * @mbggenerated 2018-12-12
     */
    int updateByPrimaryKey(Orders record);
}