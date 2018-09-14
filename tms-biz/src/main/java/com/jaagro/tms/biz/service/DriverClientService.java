package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.truck.DriverReturnDto;
import com.jaagro.tms.api.dto.truck.ShowDriverDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author tony
 */
@FeignClient("user")
public interface DriverClientService {

    /**
     * 通过id获取司机对象
     * @param id
     * @return
     */
    @GetMapping("/driverFeign/{id}")
    ShowDriverDto getDriverReturnObject(@PathVariable("id") Integer id);


    @GetMapping("/listDriverByTruckId/{truckId}")
    public List<DriverReturnDto> listByTruckId(@PathVariable("truckId") Integer truckId);
}
