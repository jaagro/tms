package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.GasolineRecord;

public interface GasolineRecordMapper {
    /**
     *
     * @mbggenerated 2018-12-05
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-05
     */
    int insert(GasolineRecord record);

    /**
     *
     * @mbggenerated 2018-12-05
     */
    int insertSelective(GasolineRecord record);

    /**
     *
     * @mbggenerated 2018-12-05
     */
    GasolineRecord selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-05
     */
    int updateByPrimaryKeySelective(GasolineRecord record);

    /**
     *
     * @mbggenerated 2018-12-05
     */
    int updateByPrimaryKey(GasolineRecord record);
}