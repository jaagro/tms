package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Waybill;

public interface WaybillMapper {
    /**
     *
     * @mbggenerated 2019-03-14
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-03-14
     */
    int insert(Waybill record);

    /**
     *
     * @mbggenerated 2019-03-14
     */
    int insertSelective(Waybill record);

    /**
     *
     * @mbggenerated 2019-03-14
     */
    Waybill selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-03-14
     */
    int updateByPrimaryKeySelective(Waybill record);

    /**
     *
     * @mbggenerated 2019-03-14
     */
    int updateByPrimaryKey(Waybill record);
}