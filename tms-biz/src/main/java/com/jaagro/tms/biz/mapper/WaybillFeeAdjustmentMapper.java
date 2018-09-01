package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillFeeAdjustment;

public interface WaybillFeeAdjustmentMapper {
    /**
     *
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insert(WaybillFeeAdjustment record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insertSelective(WaybillFeeAdjustment record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    WaybillFeeAdjustment selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(WaybillFeeAdjustment record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(WaybillFeeAdjustment record);
}