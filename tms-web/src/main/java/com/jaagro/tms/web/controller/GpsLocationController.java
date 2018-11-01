package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.service.LocationService;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gavin
 */
@RestController
@Api(description = "司机定位管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class GpsLocationController {

    @Autowired
    private LocationService locationService;

    /**
     * 批量新增司机定位数据
     *
     * @param locationDtos
     * @return
     */
    @ApiOperation("新增司机定位")
    @PostMapping("/insertBatch")
    public BaseResponse insertBatch(@RequestBody List<LocationDto> locationDtos) {
        int count = locationService.insertBatch(locationDtos);
        if (0 == count) {
            return BaseResponse.errorInstance("定位失败");
        }
        return BaseResponse.successInstance("定位成功");
    }
}