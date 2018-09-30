package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Waybill;

/**
 * @author tony
 */
public interface WaybillMapper {
    /**
     * 删除
     *
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int insert(Waybill record);

    /**
     * @mbggenerated 2018-08-31
     */
    int insertSelective(Waybill record);

    /**
     * @mbggenerated 2018-08-31
     */
    Waybill selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(Waybill record);

    /**
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(Waybill record);
}