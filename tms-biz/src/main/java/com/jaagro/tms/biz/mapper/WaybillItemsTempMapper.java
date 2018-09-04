package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillItemsTemp;

public interface WaybillItemsTempMapper {
    /**
     *
     * @mbggenerated 2018-09-04
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insert(WaybillItemsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insertSelective(WaybillItemsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    WaybillItemsTemp selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKeySelective(WaybillItemsTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKey(WaybillItemsTemp record);
}