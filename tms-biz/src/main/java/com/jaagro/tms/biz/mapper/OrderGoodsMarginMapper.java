package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.OrderGoodsMargin;

public interface OrderGoodsMarginMapper {
    /**
     *
     * @mbggenerated 2018-09-11
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-11
     */
    int insert(OrderGoodsMargin record);

    /**
     *
     * @mbggenerated 2018-09-11
     */
    int insertSelective(OrderGoodsMargin record);

    /**
     *
     * @mbggenerated 2018-09-11
     */
    OrderGoodsMargin selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-11
     */
    int updateByPrimaryKeySelective(OrderGoodsMargin record);

    /**
     *
     * @mbggenerated 2018-09-11
     */
    int updateByPrimaryKey(OrderGoodsMargin record);
}