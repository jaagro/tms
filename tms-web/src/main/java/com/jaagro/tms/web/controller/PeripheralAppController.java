package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.driverapp.CreateGasolineRecordDto;
import com.jaagro.tms.api.service.GasolinePlusService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @Gao.
 */
@Log4j
@RestController
@Api(description = "周边服务管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeripheralAppController {
    @Autowired
    private GasolinePlusService gasolinePlusService;

    @ApiOperation("我要加油")
    @GetMapping("gasolineApplyApp")
    public BaseResponse gasolineApplyApp(@RequestBody CreateGasolineRecordDto dto) {
        gasolinePlusService.gasolineApply(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

}
