package com.jaagro.tms.web.controller;

import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author @gao
 */
@RestController
@Api(description = "接单控制器", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReceiptController {

    @ApiOperation("接单列表")
    @PostMapping("/listWaybillApp")
    public BaseResponse listReceiptApp() {
//        if (StringUtils.isEmpty(dto.getWaybillStatus())) {
//            return BaseResponse.errorInstance("运单状态参数为空");
//        }
//        Map<String, Object> waybill = waybillService.listWaybillByStatus(dto);
        return BaseResponse.service(null);
    }
}

