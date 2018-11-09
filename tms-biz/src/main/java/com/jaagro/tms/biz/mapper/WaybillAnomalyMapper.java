package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillAnomaly;

public interface WaybillAnomalyMapper {
    /**
     *
     * @mbggenerated 2018-10-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insert(WaybillAnomaly record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insertSelective(WaybillAnomaly record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    WaybillAnomaly selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKeySelective(WaybillAnomaly record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKey(WaybillAnomaly record);
}