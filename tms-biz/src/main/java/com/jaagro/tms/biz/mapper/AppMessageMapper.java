package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.AppMessage;

import java.util.List;

public interface AppMessageMapper {
    /**
     * @mbggenerated 2018-09-11
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-09-11
     */
    int insertSelective(AppMessage record);

    /**
     * @mbggenerated 2018-09-11
     */
    AppMessage selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-09-11
     */
    int updateByPrimaryKeySelective(AppMessage record);

    /**
     * @mbggenerated 2018-09-11
     */
    int updateByPrimaryKey(AppMessage record);

}