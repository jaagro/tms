package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

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
}
