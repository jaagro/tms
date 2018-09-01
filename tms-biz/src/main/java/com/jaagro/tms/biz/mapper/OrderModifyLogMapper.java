package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.OrderModifyLog;

public interface OrderModifyLogMapper {
    /**
     *
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insert(OrderModifyLog record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insertSelective(OrderModifyLog record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    OrderModifyLog selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(OrderModifyLog record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(OrderModifyLog record);
}