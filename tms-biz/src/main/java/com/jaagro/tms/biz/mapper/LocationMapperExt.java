package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.Location;

import java.util.List;

public interface LocationMapperExt extends LocationMapper{
    int insertBatch(List<Location> locationList);
}