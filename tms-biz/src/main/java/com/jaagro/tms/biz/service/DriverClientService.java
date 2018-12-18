package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.driverapp.UpdateDriverDto;
import com.jaagro.tms.api.dto.truck.DriverReturnDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author tony
 */
@FeignClient("user")
public interface DriverClientService {

    /**
     * 通过id获取司机对象
     *
     * @param id
     * @return
     */
    @GetMapping("/driverFeign/{id}")
    ShowDriverDto getDriverReturnObject(@PathVariable("id") Integer id);


    /**
     * 通过车辆id获得司机列表
     *
     * @param truckId
     * @return
     */
    @GetMapping("/listDriverByTruckId/{truckId}")
    List<DriverReturnDto> listByTruckId(@PathVariable("truckId") Integer truckId);

    /**
     * 根据id获取司机信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getDriverByIdFeign/{id}")
    DriverReturnDto getDriverByIdFeign(@PathVariable("id") Integer id);

    /**
     * 更改司机
     *
     * @param driver
     * @return
     */
    @PostMapping("/updateDriverFeign")
    BaseResponse updateDriverFeign(@RequestBody UpdateDriverDto driver);
}
