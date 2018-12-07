package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WashTruckRecord;

public interface WashTruckRecordMapper {
    /**
     *
     * @mbggenerated 2018-12-07
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int insert(WashTruckRecord record);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int insertSelective(WashTruckRecord record);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    WashTruckRecord selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int updateByPrimaryKeySelective(WashTruckRecord record);

    /**
     *
     * @mbggenerated 2018-12-07
     */
    int updateByPrimaryKey(WashTruckRecord record);
}