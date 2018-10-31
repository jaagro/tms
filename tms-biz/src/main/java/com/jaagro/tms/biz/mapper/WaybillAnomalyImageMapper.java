package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillAnomalyImage;

public interface WaybillAnomalyImageMapper {
    /**
     *
     * @mbggenerated 2018-10-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insert(WaybillAnomalyImage record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int insertSelective(WaybillAnomalyImage record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    WaybillAnomalyImage selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKeySelective(WaybillAnomalyImage record);

    /**
     *
     * @mbggenerated 2018-10-31
     */
    int updateByPrimaryKey(WaybillAnomalyImage record);
}