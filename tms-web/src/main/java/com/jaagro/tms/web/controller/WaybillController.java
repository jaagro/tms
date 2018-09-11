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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author gavin
 */
@RestController
@Api(description = "运单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class WaybillController {

    @Autowired
    private WayBillService waybillService;

    /**
     * 创建运单计划
     * @param waybillDto
     * @return
     */
    @ApiOperation("创建运单计划")
    @PostMapping("/waybillPlan")
    public BaseResponse createWaybillPlan(@RequestBody CreateWaybillPlanDto waybillDto) {
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

    /**
     * 创建运单
     * @param waybillDtoList
     * @return
     */
    @ApiOperation("创建运单")
    @PostMapping("/createWaybill")
    public BaseResponse createWaybill(@RequestBody List<CreateWaybillDto> waybillDtoList) {
        Map<String, Object> result;
        try {
             result = waybillService.createWaybill(waybillDtoList);
            return BaseResponse.successInstance(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException("创建运单失败:"+e.getMessage());
            //return BaseResponse.errorInstance(e.getMessage());
        }
    }
}