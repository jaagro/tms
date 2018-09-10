package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.truck.TruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WayBillService;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author gavin
 */
@RestController
@Api(description = "配载计划", produces = MediaType.APPLICATION_JSON_VALUE)
public class WaybillController {

    @Autowired
    private WayBillService waybillService;

    /**
     * 配送计划保存到临时表
     *
     * @param waybillDto
     * @return
     */
    @ApiOperation("配送计划保存到临时表")
    @PostMapping("/waybillTemp")
    public BaseResponse createWaybillTemp(@RequestBody CreateWaybillPlanDto waybillDto) {
        Integer orderId = waybillDto.getOrderId();
        if (StringUtils.isEmpty(orderId)) {
            throw new NullPointerException("订单为空");
        }
        List<TruckDto> truckDtos = waybillDto.getTrucks();
        if (CollectionUtils.isEmpty(truckDtos)) {
            throw new NullPointerException("车辆为空");
        }
        for (TruckDto truckDto : truckDtos) {
            if (truckDto.getNumber() == null || truckDto.getNumber() <= 0) {
                throw new NullPointerException("车辆数量为空");
            }
        }
        List<CreateWaybillItemsPlanDto> waybillItemsDtos = waybillDto.getWaybillItems();
        if (CollectionUtils.isEmpty(waybillItemsDtos)) {
            throw new NullPointerException("送货地址为空");
        }
        for (CreateWaybillItemsPlanDto waybillItemsDto : waybillItemsDtos) {
            List<CreateWaybillGoodsPlanDto> goods = waybillItemsDto.getGoods();
            if (CollectionUtils.isEmpty(goods)) {
                throw new NullPointerException("计划配送物品为空");
            }
        }
        try {
            List<ListWaybillPlanDto> result = waybillService.createWaybillPlan(waybillDto);
            return BaseResponse.successInstance(result);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(e.getMessage());
        }
    }
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
                return BaseResponse.errorInstance("订单参数不能为空");
            }
            final Map<String, Object> waybillDetails = waybillService.listWayBillDetails(waybillId);
            return BaseResponse.service(waybillDetails);
        }

        @ApiOperation("运单轨迹")
        @GetMapping("/showWaybillApp/{waybillId}")
        public BaseResponse showWaybillApp(@PathVariable Integer waybillId) {
            if (waybillId == null) {
                return BaseResponse.errorInstance("订单参数不能为空");
            }
            return BaseResponse.service(waybillService.showWaybill(waybillId));
        }
    }