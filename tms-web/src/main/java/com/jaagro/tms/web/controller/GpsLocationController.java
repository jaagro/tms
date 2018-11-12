package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.dto.waybill.ShowLocationDto;
import com.jaagro.tms.api.service.LocationService;
import com.jaagro.tms.biz.service.impl.GpsLocationAsync;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author gavin
 */
@Log4j
@RestController
@Api(description = "司机定位管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class GpsLocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private GpsLocationAsync asyncTask;

    /**
     * 批量新增司机定位数据
     *
     * @param locationDtos
     * @return
     */
    @ApiOperation("新增司机定位")
    @PostMapping("/insertBatch")
    public BaseResponse insertBatch(@RequestBody List<LocationDto> locationDtos) {
        long start = System.currentTimeMillis();
        int count = locationService.insertBatch(locationDtos);
        long end = System.currentTimeMillis();
        log.info("-----同步耗时----------" + (start - end) + "---------------");
        if (0 == count) {
            return BaseResponse.errorInstance("定位失败");
        }
        return BaseResponse.successInstance("定位成功");
    }


    @ApiOperation("异步新增司机定位")
    @PostMapping("/asyncBatchInsert")
    public BaseResponse asyncBatchInsert(@RequestBody List<LocationDto> locationDtos) {
        long start = System.currentTimeMillis();
        List<LocationDto> listA = locationDtos.subList(0, 100);
        List<LocationDto> listB = locationDtos.subList(100, 200);
        List<LocationDto> listC = locationDtos.subList(200, locationDtos.size());
        Future<Boolean> taskA = asyncTask.batchInsertOne(listA);
        Future<Boolean> taskB = asyncTask.batchInsertTwo(listB);
        Future<Boolean> taskC = asyncTask.batchInsertThree(listC);

        while (!taskA.isDone() || !taskB.isDone() || !taskC.isDone()) {
            if (taskA.isDone() && taskB.isDone() && taskC.isDone()) {
                break;
            }
        }
        long end = System.currentTimeMillis();
        log.info("----异步耗时----------" + (start - end) + "---------------");
        return BaseResponse.successInstance("定位成功");
    }

    @ApiOperation("运单轨迹定位数据")
    @PostMapping("/listLocationsByWaybillId/{waybillId}")
    public BaseResponse listLocationsByWaybillId(@PathVariable Integer waybillId,@PathVariable Integer interval) {
        long start = System.currentTimeMillis();
        List<ShowLocationDto> result = locationService.locationsByWaybillId(waybillId,interval);
        long end = System.currentTimeMillis();
        log.info("----耗时----------" + (start - end) + "---------------");
        return BaseResponse.successInstance(result);
    }
}