package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillTracking;

public interface WaybillTrackingMapper {
    /**
     *
     * @mbggenerated 2018-10-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insert(WaybillTracking record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insertSelective(WaybillTracking record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    WaybillTracking selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKeySelective(WaybillTracking record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKey(WaybillTracking record);
}