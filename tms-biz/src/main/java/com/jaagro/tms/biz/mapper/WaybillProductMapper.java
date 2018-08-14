package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillProduct;

public interface WaybillProductMapper {
    int deleteByPrimaryKey(Long waybillProductId);

    int insert(WaybillProduct record);

    int insertSelective(WaybillProduct record);

    WaybillProduct selectByPrimaryKey(Long waybillProductId);

    int updateByPrimaryKeySelective(WaybillProduct record);

    int updateByPrimaryKey(WaybillProduct record);
}