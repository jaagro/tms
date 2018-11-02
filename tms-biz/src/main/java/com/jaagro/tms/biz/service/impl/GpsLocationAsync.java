package com.jaagro.tms.biz.service.impl;


import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.service.LocationService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Future;

@Component
@Log4j
public class GpsLocationAsync {

    @Autowired
    private LocationService locationService;

    @Async
    public Future<Boolean> batchInsertOne(List<LocationDto> locationDtos){
        int count = locationService.insertBatch(locationDtos);
        return new AsyncResult<>(true);
    }

    @Async
    public Future<Boolean> batchInsertTwo(List<LocationDto> locationDtos){
        int count = locationService.insertBatch(locationDtos);
        return new AsyncResult<>(true);
    }
    @Async
    public Future<Boolean> batchInsertThree(List<LocationDto> locationDtos){
        int count = locationService.insertBatch(locationDtos);
        return new AsyncResult<>(true);
    }
    @Async
    public Future<Boolean> batchInsertFour(List<LocationDto> locationDtos){
        int count = locationService.insertBatch(locationDtos);
        return new AsyncResult<>(true);
    }
}
