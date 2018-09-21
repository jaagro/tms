package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Message;

public interface MessageMapper {
    /**
     *
     * @mbggenerated 2018-09-14
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-14
     */
    int insert(Message record);

    /**
     *
     * @mbggenerated 2018-09-14
     */
    int insertSelective(Message record);

    /**
     *
     * @mbggenerated 2018-09-14
     */
    Message selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-14
     */
    int updateByPrimaryKeySelective(Message record);

    /**
     *
     * @mbggenerated 2018-09-14
     */
    int updateByPrimaryKey(Message record);
}