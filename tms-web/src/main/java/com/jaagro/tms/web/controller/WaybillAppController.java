package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.driverapp.GetReceiptParamDto;
import com.jaagro.tms.api.dto.driverapp.GetWaybillParamDto;
import com.jaagro.tms.api.dto.driverapp.GetWaybillTruckingParamDto;
import com.jaagro.tms.api.dto.driverapp.ShowWaybillTrackingDto;
import com.jaagro.tms.api.dto.waybill.GetReceiptMessageParamDto;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.service.impl.WaybillServiceImpl;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    @Autowired
    private WaybillRefactorService waybillRefactorService;
    private static final Logger log = LoggerFactory.getLogger(WaybillServiceImpl.class);

    @ApiOperation("我的运单")
    @PostMapping("/listWaybillApp")
    public BaseResponse listWaybillApp(@RequestBody GetWaybillParamDto dto) {
        if (StringUtils.isEmpty(dto.getWaybillStatus())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "运单状态参数为空");
        }
        PageInfo waybill = waybillRefactorService.listWaybillByStatus(dto);
        return BaseResponse.successInstance(waybill);
    }


    @ApiOperation("运单详情")
    @GetMapping("/ListWayBillDetailsApp/{waybillId}")
    public BaseResponse listWayBillDetailsApp(@PathVariable Integer waybillId) {
        if (waybillId == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "运单参数不能为空");
        }
        return BaseResponse.service(waybillService.listWayBillDetails(waybillId));
    }

    @ApiOperation("运单轨迹")
    @GetMapping("/showWaybillTruckingApp/{waybillId}")
    public BaseResponse showWaybillTruckingApp(@PathVariable Integer waybillId) {
        if (waybillId == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "运单单参数不能为空");
        }
        ShowWaybillTrackingDto showWaybillTrackingDto = waybillService.showWaybillTrucking(waybillId);
        if (showWaybillTrackingDto != null) {
            return BaseResponse.successInstance(showWaybillTrackingDto);
        }
        return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_EMPTY.getCode(), "查询不到有效的运单");
    }

    @ApiOperation("运单轨迹更新")
    @PostMapping("/updateWaybillTruckingApp")
    public BaseResponse updateWaybillTruckingApp(@RequestBody GetWaybillTruckingParamDto dto) {
        if (dto == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "运单单参数不能为空");
        }
        return BaseResponse.service(waybillService.upDateWaybillTrucking(dto));
    }

    @ApiOperation("装货地商品信息")
    @GetMapping("/showGoodsByLoadSiteApp/{waybillId}")
    public BaseResponse showGoodsByLoadSiteApp(@PathVariable Integer waybillId) {
        if (waybillId == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "参数不能为空");
        }
        return BaseResponse.service(waybillService.showGoodsByWaybillId(waybillId));
    }

    @ApiOperation("卸货地商品信息")
    @GetMapping("/showGoodsByUnLoadSiteApp/{waybillItemId}")
    public BaseResponse showGoodsByUnLoadSiteApp(@PathVariable Integer waybillItemId) {
        if (waybillItemId == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "参数不能为空");
        }
        return BaseResponse.service(waybillService.showGoodsByWaybillItemId(waybillItemId));
    }

    @ApiOperation("显示卸货地")
    @PostMapping("/showUnloadSiteApp/{waybillId}")
    public BaseResponse showUnloadSiteApp(@PathVariable Integer waybillId) {
        if (waybillId == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "运单参数不能为空");
        }
        return BaseResponse.service(waybillService.showUnloadSite(waybillId));
    }

    @ApiOperation("接单列表")
    @PostMapping("/receiptListApp")
    public BaseResponse receiptListApp(@RequestBody GetReceiptParamDto dto) {
        if (dto == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "接单参数不能为空");
        }
        return BaseResponse.service(waybillService.receiptList(dto));
    }

    @ApiOperation("接单控制")
    @PostMapping("/upDateReceiptStatusApp")
    public BaseResponse upDateReceiptStatusApp(@RequestBody GetReceiptParamDto dto) {
        log.info("O upDateReceiptStatusApp :{}", dto);
        if (dto.getWaybillId() == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "接单参数不能为空");
        }
        return BaseResponse.service(waybillService.upDateReceiptStatus(dto));
    }

    @ApiOperation("接单消息")
    @PostMapping("/receiptMessageApp")
    public BaseResponse receiptMessageApp(@RequestBody GetReceiptMessageParamDto dto) {
        if (dto == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "接单参数不能为空");
        }
        return BaseResponse.service(waybillService.receiptMessage(dto));
    }

    @ApiOperation("个人中心")
    @GetMapping("personalCenterApp")
    public BaseResponse personalCenterApp() {
        return BaseResponse.successInstance(waybillService.personalCenter());
    }
}
