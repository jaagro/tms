package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillAnomalyLog;

import java.util.List;

/**
 * @author yj
 */
public interface WaybillAnomalyLogMapper {
    /**
     *
     * @mbggenerated 2018-11-08
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-11-08
     */
    int insert(WaybillAnomalyLog record);

    /**
     *
     * @mbggenerated 2018-11-08
     */
    int insertSelective(WaybillAnomalyLog record);
    /**
     *
     * @mbggenerated 2018-11-08
     */
    WaybillAnomalyLog selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-11-08
     */
    int updateByPrimaryKeySelective(WaybillAnomalyLog record);

    /**
     *
     * @mbggenerated 2018-11-08
     */
    int updateByPrimaryKey(WaybillAnomalyLog record);
}