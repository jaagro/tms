package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.dto.waybill.ShowLocationDto;
import com.jaagro.tms.api.service.LocationService;
import com.jaagro.tms.biz.mapper.LocationMapperExt;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(keyGenerator = "wiselyKeyGenerator", cacheNames = "location")
@Service
@Log4j
public class LocationServiceImpl implements LocationService {

    @Autowired
    LocationMapperExt locationMapper;

    /**
     * 批量新增司机定位数据
     *
     * @param locationList
     * @return
     */
    @Override
    public int insertBatch(List<LocationDto> locationList) {

        int count = locationMapper.insertBatch(locationList);

        return count;
    }

    /**
     * 根据运单Id查询轨迹
     *
     * @param waybillId
     * @return
     */
    @Override
    public List<ShowLocationDto> locationsByWaybillId(Integer waybillId) {


        return locationMapper.listLocationsByWaybillId(waybillId);

    }


}
