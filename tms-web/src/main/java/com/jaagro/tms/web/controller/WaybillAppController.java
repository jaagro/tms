package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.driverapp.GetReceiptParamDto;
import com.jaagro.tms.api.dto.driverapp.GetWaybillParamDto;
import com.jaagro.tms.api.dto.driverapp.GetWaybillTruckingParamDto;
import com.jaagro.tms.api.dto.waybill.GetReceiptMessageParamDto;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import com.jaagro.utils.ServiceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author @Gao.
 */
@Api(description = "App运单管理", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class WaybillAppController {

    @Autowired
    private WaybillService waybillService;

    @ApiOperation("我的运单")
    @PostMapping("/listWaybillApp")
    public BaseResponse listWaybillApp(@RequestBody GetWaybillParamDto dto) {
        if (StringUtils.isEmpty(dto.getWaybillStatus())) {
            return BaseResponse.errorInstance("运单状态参数为空");
        }
        Map<String, Object> waybill = waybillService.listWaybillByStatus(dto);
        return BaseResponse.service(waybill);
    }

    @ApiOperation("运单详情")
    @GetMapping("/ListWayBillDetailsApp/{waybillId}")
    public BaseResponse listWayBillDetailsApp(@PathVariable Integer waybillId) {
        if (waybillId == null) {
            return BaseResponse.errorInstance("运单参数不能为空");
        }
        final Map<String, Object> waybillDetails = waybillService.listWayBillDetails(waybillId);
        return BaseResponse.service(waybillDetails);
    }

    @ApiOperation("运单轨迹")
    @GetMapping("/showWaybillApp/{waybillId}")
    public BaseResponse showWaybillTruckingApp(@PathVariable Integer waybillId) {
        if (waybillId == null) {
            return BaseResponse.errorInstance("运单单参数不能为空");
        }
        return BaseResponse.service(waybillService.showWaybillTrucking(waybillId));
    }

    @ApiOperation("运单轨迹更新")
    @PostMapping("/updateWaybillTruckingApp")
    public BaseResponse updateWaybillTruckingApp(@RequestBody GetWaybillTruckingParamDto dto) {
        if (dto == null) {
            return BaseResponse.errorInstance("运单单参数不能为空");
        }
        return BaseResponse.service(waybillService.upDateWaybillTrucking(dto));
    }

    @ApiOperation("接单列表")
    @PostMapping("/receiptListApp")
    public BaseResponse receiptListApp(@RequestBody GetReceiptParamDto dto) {
        if (dto == null) {
            return BaseResponse.errorInstance("接单参数不能为空");
        }
        return BaseResponse.service(waybillService.receiptList(dto));
    }

    @ApiOperation("接单控制")
    @PostMapping("/upDateReceiptStatusApp")
    public BaseResponse upDateReceiptStatusApp(@RequestBody GetReceiptParamDto dto) {
        if (dto.getWaybillId() == null) {
            return BaseResponse.errorInstance("接单参数不能为空");
        }
        return BaseResponse.service(waybillService.upDateReceiptStatus(dto));
    }

    @ApiOperation("接单消息")
    @PostMapping("/receiptMessageApp")
    public BaseResponse receiptMessageApp(@RequestBody GetReceiptMessageParamDto dto) {
        if (dto == null) {
            return BaseResponse.errorInstance("接单参数不能为空");
        }
        return BaseResponse.service(waybillService.receiptMessage(dto));
    }
    @ApiOperation("显示卸货地")
    @PostMapping("/showUnloadSiteApp/{waybillId}")
    public BaseResponse showUnloadSiteApp(@PathVariable Integer waybillId) {
        if (waybillId == null) {
            return BaseResponse.errorInstance("运单参数不能为空");
        }
        return BaseResponse.service(waybillService.showUnloadSite(waybillId));
    }
}
