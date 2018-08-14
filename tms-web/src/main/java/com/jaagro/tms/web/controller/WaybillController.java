package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.CreateWaybillByExcelDto;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.mapper.WaybillMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utils.ServiceResult;

import java.util.Map;

/**
 * @author tony
 */
@Api(value = "waybill", description = "运单管理",produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping("/waybill")
public class WaybillController {

    @Autowired
    private WaybillService waybillService;
    @Autowired
    private WaybillMapper waybillMapper;

    @ApiOperation("excel运单导入")
    @PostMapping("/createWaybillByExcel")
    public Map<String, Object> createWaybillByExcel(@RequestBody CreateWaybillByExcelDto dto){
        Map<String, Object> map = null;
        try {
            map = waybillService.createWaybillByExcel(dto);
        } catch (Exception e) {
            e.printStackTrace();
            map = ServiceResult.error(e.getMessage());
        }
        return map;
    }
}
