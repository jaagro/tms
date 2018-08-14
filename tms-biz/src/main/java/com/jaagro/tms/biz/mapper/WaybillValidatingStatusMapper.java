package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillValidatingStatus;

public interface WaybillValidatingStatusMapper {
    int deleteByPrimaryKey(Long waybillValidatingStatusId);

    int insert(WaybillValidatingStatus record);

    int insertSelective(WaybillValidatingStatus record);

    WaybillValidatingStatus selectByPrimaryKey(Long waybillValidatingStatusId);

    int updateByPrimaryKeySelective(WaybillValidatingStatus record);

    int updateByPrimaryKey(WaybillValidatingStatus record);
}