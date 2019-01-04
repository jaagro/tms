package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillCustomerFee;

public interface WaybillCustomerFeeMapper {
    /**
     *
     * @mbggenerated 2018-12-26
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int insert(WaybillCustomerFee record);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int insertSelective(WaybillCustomerFee record);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    WaybillCustomerFee selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int updateByPrimaryKeySelective(WaybillCustomerFee record);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int updateByPrimaryKey(WaybillCustomerFee record);
}