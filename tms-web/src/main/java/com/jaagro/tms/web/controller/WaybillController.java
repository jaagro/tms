package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.waybill.CreateWaybillDto;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author gavin
 */
@RestController
@Api(description = "配载计划", produces = MediaType.APPLICATION_JSON_VALUE)
public class WaybillController {

    @Autowired
    private WaybillService waybillService;


    /**
     * 配送计划保存到临时表
     *
     * @param waybillDto
     * @return
     */
    @ApiOperation("配送计划保存到临时表")
    @PostMapping("/waybillTemp")
    public BaseResponse createWaybillTemp(@RequestBody CreateWaybillDto waybillDto) {
        try {
            Map<String,Object> result= waybillService.createWaybill(waybillDto);
            return BaseResponse.successInstance(result);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(e.getMessage());
        }
    }

}
