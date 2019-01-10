package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.GrabWaybillRecord;

public interface GrabWaybillRecordMapper {
    /**
     *
     * @mbggenerated 2019-01-10
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-01-10
     */
    int insert(GrabWaybillRecord record);

    /**
     *
     * @mbggenerated 2019-01-10
     */
    int insertSelective(GrabWaybillRecord record);

    /**
     *
     * @mbggenerated 2019-01-10
     */
    GrabWaybillRecord selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-01-10
     */
    int updateByPrimaryKeySelective(GrabWaybillRecord record);

    /**
     *
     * @mbggenerated 2019-01-10
     */
    int updateByPrimaryKey(GrabWaybillRecord record);
}