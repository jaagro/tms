package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.CatchChickenTime;

public interface CatchChickenTimeMapper {
    /**
     *
     * @mbggenerated 2018-09-27
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-27
     */
    int insert(CatchChickenTime record);

    /**
     *
     * @mbggenerated 2018-09-27
     */
    int insertSelective(CatchChickenTime record);

    /**
     *
     * @mbggenerated 2018-09-27
     */
    CatchChickenTime selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-27
     */
    int updateByPrimaryKeySelective(CatchChickenTime record);

    /**
     *
     * @mbggenerated 2018-09-27
     */
    int updateByPrimaryKey(CatchChickenTime record);
}