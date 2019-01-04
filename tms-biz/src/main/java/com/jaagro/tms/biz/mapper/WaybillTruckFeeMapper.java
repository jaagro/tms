package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillTruckFee;

public interface WaybillTruckFeeMapper {
    /**
     *
     * @mbggenerated 2018-12-26
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int insert(WaybillTruckFee record);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int insertSelective(WaybillTruckFee record);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    WaybillTruckFee selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int updateByPrimaryKeySelective(WaybillTruckFee record);

    /**
     *
     * @mbggenerated 2018-12-26
     */
    int updateByPrimaryKey(WaybillTruckFee record);
}