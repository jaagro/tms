package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillGoods;

public interface WaybillGoodsMapper {
    /**
     *
     * @mbggenerated 2018-09-25
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int insert(WaybillGoods record);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int insertSelective(WaybillGoods record);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    WaybillGoods selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int updateByPrimaryKeySelective(WaybillGoods record);

    /**
     *
     * @mbggenerated 2018-09-25
     */
    int updateByPrimaryKey(WaybillGoods record);
}