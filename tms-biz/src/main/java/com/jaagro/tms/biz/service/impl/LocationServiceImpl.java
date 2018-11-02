package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.service.LocationService;
import com.jaagro.tms.biz.mapper.LocationMapperExt;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        long start = System.currentTimeMillis();
        int count = locationMapper.insertBatch(locationList);
        long end = System.currentTimeMillis();
        log.info("-----耗时----------" + (start - end) + "---------------");
        return count;
    }
}
