package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Waybill;

public interface WaybillMapper {
    /**
     *
     * @mbggenerated 2018-09-28
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-28
     */
    int insert(Waybill record);

    /**
     *
     * @mbggenerated 2018-09-28
     */
    int insertSelective(Waybill record);

    /**
     *
     * @mbggenerated 2018-09-28
     */
    Waybill selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-28
     */
    int updateByPrimaryKeySelective(Waybill record);

    /**
     *
     * @mbggenerated 2018-09-28
     */
    int updateByPrimaryKey(Waybill record);
}