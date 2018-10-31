package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillAnomalyType;

public interface WaybillAnomalyTypeMapper {
    /**
     *
     * @mbggenerated 2018-10-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insert(WaybillAnomalyType record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insertSelective(WaybillAnomalyType record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    WaybillAnomalyType selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKeySelective(WaybillAnomalyType record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKey(WaybillAnomalyType record);
}