package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.OrderGoods;

import java.util.List;

public interface OrderGoodsMapper {
    /**
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int insert(OrderGoods record);

    /**
     * @mbggenerated 2018-08-31
     */
    int insertSelective(OrderGoods record);

    /**
     * @mbggenerated 2018-08-31
     */
    OrderGoods selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(OrderGoods record);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(OrderGoods record);

    /**
     * 根据订单需求id查询列表
     *
     * @param id
     * @return
     */
    List<OrderGoods> listByItemsId(Integer id);
}