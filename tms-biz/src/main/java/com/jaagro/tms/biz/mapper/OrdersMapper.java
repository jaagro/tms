package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Orders;

public interface OrdersMapper {
    /**
     *
     * @mbggenerated 2018-12-20
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-20
     */
    int insert(Orders record);

    /**
     *
     * @mbggenerated 2018-12-20
     */
    int insertSelective(Orders record);

    /**
     *
     * @mbggenerated 2018-12-20
     */
    Orders selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-20
     */
    int updateByPrimaryKeySelective(Orders record);

    /**
     *
     * @mbggenerated 2018-12-20
     */
    int updateByPrimaryKey(Orders record);
}