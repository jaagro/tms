package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyReportDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author @Gao.
 */
@Api(description = "运单异常申报管理", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class WaybillAnomalyController {
    @Autowired
    private WaybillAnomalyService waybillAnomalyService;

    @ApiOperation("司机运单异常申报")
    @PostMapping("waybillAnomalyReportApp")
    public BaseResponse waybillAnomalyReportApp(@RequestBody WaybillAnomalyReportDto dto) {
        waybillAnomalyService.waybillAnomalyReport(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }
    @ApiOperation("运单异常类型显示")
    @GetMapping("displayAbnormalTypeApp")
    public BaseResponse displayAbnormalTypeApp() {
        return BaseResponse.successInstance(waybillAnomalyService.displayAbnormalType());
    }
}
