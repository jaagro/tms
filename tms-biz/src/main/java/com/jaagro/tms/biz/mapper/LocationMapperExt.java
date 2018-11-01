package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.LocationDto;

import java.util.List;

public interface LocationMapperExt extends LocationMapper {
    /**
     * 批量新增司机定位数据
     *
     * @param locationList
     * @return
     */
    int insertBatch(List<LocationDto> locationList);
}