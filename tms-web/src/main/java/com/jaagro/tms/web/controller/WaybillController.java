package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.truck.TruckDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillPlanService;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
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
@Api(description = "运单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class WaybillController {

    @Autowired
    private WaybillService waybillService;
    @Autowired
    private WaybillPlanService waybillPlanService;
    @Autowired
    private WaybillRefactorService waybillRefactorService;

    /**
     * 创建运单计划
     *
     * @param waybillDto
     * @return
     */
    @ApiOperation("创建运单计划")
    @PostMapping("/waybillPlan")
    public BaseResponse createWaybillPlan(@RequestBody CreateWaybillPlanDto waybillDto) {
        Integer orderId = waybillDto.getOrderId();
        if (StringUtils.isEmpty(orderId)) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "订单id不能为空");
        }
        List<TruckDto> truckDtos = waybillDto.getTrucks();
        if (CollectionUtils.isEmpty(truckDtos)) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "车辆为空");
        }
        for (TruckDto truckDto : truckDtos) {
            if (truckDto.getNumber() == null || truckDto.getNumber() <= 0) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "车辆数量为空");
            }
        }
        List<CreateWaybillItemsPlanDto> waybillItemsDtos = waybillDto.getWaybillItems();
        if (CollectionUtils.isEmpty(waybillItemsDtos)) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "送货地址为空");
        }
        for (CreateWaybillItemsPlanDto waybillItemsDto : waybillItemsDtos) {
            List<CreateWaybillGoodsPlanDto> goods = waybillItemsDto.getGoods();
            if (CollectionUtils.isEmpty(goods)) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "计划配送物品为空");
            }
        }
        try {
            List<ListWaybillPlanDto> result = waybillPlanService.createWaybillPlan(waybillDto);
            return BaseResponse.successInstance(result);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.SERVER_ERROR.getCode(), e.getMessage());

        }
    }

    @ApiOperation("从配载计划中移除运单")
    @DeleteMapping("/waybillPlan/{waybillId}")
    public BaseResponse removeWaybillFromPlan(@PathVariable("waybillId") Integer waybillId) {
        return BaseResponse.service(waybillPlanService.removeWaybillFromPlan(waybillId));
    }

//            ---------------------------------------运单---------------------------------------

    @ApiOperation("通过id获取运单对象")
    @GetMapping("/waybill/{id}")
    public BaseResponse getWaybillById(@PathVariable("id") Integer id) {
        GetWaybillDto result;
        try {
            result = waybillService.getWaybillById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(e.getMessage());
        }
        return BaseResponse.successInstance(result);
    }

    @ApiOperation("通过orderId获取订单和运单")
    @GetMapping("/orderAndWaybill/{orderId}")
    public BaseResponse getOrderAndWaybillByOrderId(@PathVariable("orderId") Integer orderId) {
        GetWaybillPlanDto result;
        try {
            result = waybillService.getOrderAndWaybill(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(e.getMessage());
        }
        return BaseResponse.successInstance(result);
    }

    /**
     * 创建运单
     *
     * @param waybillDtoList
     * @return
     * @Author gavin
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
            return BaseResponse.errorInstance(ResponseStatusCode.SERVER_ERROR.getCode(), "创建运单失败：" + e.getMessage());
        }
    }

    @ApiOperation("运单派给车辆")
    @PostMapping("/assignWaybillToTruck/{waybillId}/{truckId}")
    public BaseResponse assignWaybillToTruck(@PathVariable Integer waybillId, @PathVariable Integer truckId) {
        try {
            return BaseResponse.service(waybillService.assignWaybillToTruck(waybillId, truckId));
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.SERVER_ERROR.getCode(), "派单失败:" + e.getMessage());
        }
    }

    /**
     * 运单分页查询管理
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("运单分页查询管理")
    @PostMapping("/listWaybillByCriteria")
    public BaseResponse listWaybillByCriteria(@RequestBody ListWaybillCriteriaDto criteriaDto) {
        if (StringUtils.isEmpty(criteriaDto.getPageNum())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为空");
        }
        if (StringUtils.isEmpty(criteriaDto.getPageSize())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为空");
        }
        return BaseResponse.service(waybillService.listWaybillByCriteria(criteriaDto));
    }

    /**
     * @Author Gavin
     *
     * @param orderId
     * @return
     */
    @ApiOperation("根据订单号查询运单详情列表")
    @PostMapping("/listWaybillDetailByOrderId/{orderId}")
    public BaseResponse listWaybillDetailByOrderId(@PathVariable Integer orderId) {
        try {
            List<GetWaybillDetailDto> waybillDetailDtos = waybillRefactorService.listWaybillDetailByOrderId(orderId);
            return BaseResponse.successInstance(waybillDetailDtos);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.SERVER_ERROR.getCode(), "查询运单详情列表失败:" + e.getMessage());
        }
    }

    /**
     * 根据id查询运单相关的所有对象
     * @Author Gavin
     * @param waybillId
     * @return
     */
    @ApiOperation("根据运单号查询运单详情")
    @PostMapping("/getWaybillDetailById/{waybillId}")
    public BaseResponse getWaybillDetailById(@PathVariable Integer waybillId) {
        try {
            GetWaybillDetailDto waybillDetailDto = waybillRefactorService.getWaybillDetailById(waybillId);
            return BaseResponse.successInstance(waybillDetailDto);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.SERVER_ERROR.getCode(), "查询运单详情失败:" + e.getMessage());
        }
    }
}