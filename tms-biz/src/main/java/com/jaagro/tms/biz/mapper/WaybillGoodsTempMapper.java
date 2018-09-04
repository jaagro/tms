package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillGoodsTemp;

public interface WaybillGoodsTempMapper {
    /**
     *
     * @mbggenerated 2018-09-04
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insert(WaybillGoodsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insertSelective(WaybillGoodsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    WaybillGoodsTemp selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKeySelective(WaybillGoodsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKey(WaybillGoodsTemp record);
}