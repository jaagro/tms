package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.EvaluateType;

import java.util.List;

public interface EvaluateTypeMapper  {

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int insert(EvaluateType record);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int insertSelective(EvaluateType record);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    EvaluateType selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int updateByPrimaryKeySelective(EvaluateType record);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int updateByPrimaryKey(EvaluateType record);
}