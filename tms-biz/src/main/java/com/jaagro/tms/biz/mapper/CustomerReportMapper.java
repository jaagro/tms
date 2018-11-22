package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.CustomerReport;

/**
 * @author baiyiran
 */
public interface CustomerReportMapper {

    /**
     * @mbggenerated 2018-11-22
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-11-22
     */
    int insert(CustomerReport record);

    /**
     * @mbggenerated 2018-11-22
     */
    int insertSelective(CustomerReport record);

    /**
     * @mbggenerated 2018-11-22
     */
    CustomerReport selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-11-22
     */
    int updateByPrimaryKeySelective(CustomerReport record);

    /**
     * @mbggenerated 2018-11-22
     */
    int updateByPrimaryKey(CustomerReport record);
}