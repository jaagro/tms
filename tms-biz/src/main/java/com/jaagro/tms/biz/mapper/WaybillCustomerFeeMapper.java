package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillCustomerFee;

public interface WaybillCustomerFeeMapper {
    /**
     *
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insert(WaybillCustomerFee record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insertSelective(WaybillCustomerFee record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    WaybillCustomerFee selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(WaybillCustomerFee record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(WaybillCustomerFee record);
}