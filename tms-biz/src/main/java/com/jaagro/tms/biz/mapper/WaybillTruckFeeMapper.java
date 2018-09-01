package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillTruckFee;

public interface WaybillTruckFeeMapper {
    /**
     *
     * @mbggenerated 2018-08-31
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insert(WaybillTruckFee record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int insertSelective(WaybillTruckFee record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    WaybillTruckFee selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKeySelective(WaybillTruckFee record);

    /**
     *
     * @mbggenerated 2018-08-31
     */
    int updateByPrimaryKey(WaybillTruckFee record);
}