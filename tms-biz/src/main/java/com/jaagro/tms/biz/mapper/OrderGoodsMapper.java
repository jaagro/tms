package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.OrderGoods;

public interface OrderGoodsMapper {
    /**
     *
     * @mbggenerated 2018-09-25
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int insert(OrderGoods record);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int insertSelective(OrderGoods record);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    OrderGoods selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int updateByPrimaryKeySelective(OrderGoods record);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int updateByPrimaryKey(OrderGoods record);
}