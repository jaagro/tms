package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillTemp;
import org.springframework.stereotype.Repository;

@Repository
public interface WaybillTempMapper {
    /**
     *
     * @mbggenerated 2018-09-04
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insert(WaybillTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int insertSelective(WaybillTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    WaybillTemp selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKeySelective(WaybillTemp record);

    /**
     *
     * @mbggenerated 2018-09-04
     */
    int updateByPrimaryKey(WaybillTemp record);

    WaybillTemp getByOrderIdAndTruckId(Integer orderId, Integer truckId);
}