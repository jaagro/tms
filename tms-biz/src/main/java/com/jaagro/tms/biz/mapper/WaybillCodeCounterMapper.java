package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillCodeCounter;

public interface WaybillCodeCounterMapper {

    int deleteByPrimaryKey(Long waybillCodeCounterId);

    int insert(WaybillCodeCounter record);

    int insertSelective(WaybillCodeCounter record);

    WaybillCodeCounter selectByPrimaryKey(Long waybillCodeCounterId);

    int updateByPrimaryKeySelective(WaybillCodeCounter record);

    int updateByPrimaryKey(WaybillCodeCounter record);

    Long getMaxCode();
}