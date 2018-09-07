package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.base.ShowTruckTypeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author tony
 */
@FeignClient("crm")
public interface TruckTypeClientService {

    /**
     * 车型list
     * @return
     */
    @GetMapping("/listTruckTypeToFeign")
    List<ListTruckTypeDto> listTruckTypeReturnDto();

    /**
     * 获取单个车型
     * @param id
     * @return
     */
    @GetMapping("/getTruckTypeById/{id}")
    ListTruckTypeDto getTruckTypeById(@PathVariable("id") Integer id);
}
