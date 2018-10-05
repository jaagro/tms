package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Orders;

public interface OrdersMapper {
    /**
     *
     * @mbggenerated 2018-10-05
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-05
     */
    int insert(Orders record);

    /**
     *
     * @mbggenerated 2018-10-05
     */
    int insertSelective(Orders record);

    /**
     *
     * @mbggenerated 2018-10-05
     */
    Orders selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-05
     */
    int updateByPrimaryKeySelective(Orders record);

    /**
     *
     * @mbggenerated 2018-10-05
     */
    int updateByPrimaryKey(Orders record);
}