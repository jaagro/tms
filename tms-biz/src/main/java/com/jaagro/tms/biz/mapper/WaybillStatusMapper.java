package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillStatus;

public interface WaybillStatusMapper {
    int deleteByPrimaryKey(Long waybillStatusId);

    int insert(WaybillStatus record);

    int insertSelective(WaybillStatus record);

    WaybillStatus selectByPrimaryKey(Long waybillStatusId);

    int updateByPrimaryKeySelective(WaybillStatus record);

    int updateByPrimaryKey(WaybillStatus record);
}