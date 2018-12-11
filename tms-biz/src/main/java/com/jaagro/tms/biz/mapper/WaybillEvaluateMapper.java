package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillEvaluate;
import com.jaagro.tms.biz.entity.WaybillEvaluateExample;
import java.util.List;

public interface WaybillEvaluateMapper {
    /**
     *
     * @mbggenerated 2018-12-11
     */
    int countByExample(WaybillEvaluateExample example);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int insert(WaybillEvaluate record);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int insertSelective(WaybillEvaluate record);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    List<WaybillEvaluate> selectByExample(WaybillEvaluateExample example);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    WaybillEvaluate selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int updateByPrimaryKeySelective(WaybillEvaluate record);

    /**
     *
     * @mbggenerated 2018-12-11
     */
    int updateByPrimaryKey(WaybillEvaluate record);
}