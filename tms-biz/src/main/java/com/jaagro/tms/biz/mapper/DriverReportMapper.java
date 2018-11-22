package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.DriverReport;

import java.util.List;


public interface DriverReportMapper {
    /**
     *
     * @mbggenerated 2018-11-22
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-11-22
     */
    int insert(DriverReport record);

    /**
     *
     * @mbggenerated 2018-11-22
     */
    int insertSelective(DriverReport record);

    /**
     *
     * @mbggenerated 2018-11-22
     */
    DriverReport selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-11-22
     */
    int updateByPrimaryKeySelective(DriverReport record);

    /**
     *
     * @mbggenerated 2018-11-22
     */
    int updateByPrimaryKey(DriverReport record);
}