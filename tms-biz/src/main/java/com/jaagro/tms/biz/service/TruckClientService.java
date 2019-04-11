package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.base.ListTruckTypeDto;
import com.jaagro.tms.api.dto.truck.*;
import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tony
 */
@FeignClient(value = "${feignClient.application.crm}")
public interface TruckClientService {

    /**
     * 获取车辆对象
     * @param truckId
     * @return
     */
    @GetMapping("/truckToFeign/{truckId}")
    ShowTruckDto getTruckByIdReturnObject(@PathVariable("truckId") Integer truckId);

    /**
     * 获取当前司机对应的车辆
     * @return
     */
    @GetMapping("/getTruckByToken")
    ShowTruckDto getTruckByToken();

    /**
     * 通过车队id获取换车列表
     *
     * @param truckTeamId
     * @return
     */
    @GetMapping("/listTruckByTruckTeamId/{truckTeamId}")
    BaseResponse<List<ChangeTruckDto>> listTruckByTruckTeamId(@PathVariable("truckTeamId") Integer truckTeamId);

    /**
     * 获取司机资质
     *
     * @param driverId
     * @return
     */
    @PostMapping("/listQualificationByDriverId/{driverId}")
    BaseResponse<List<ListTruckQualificationDto>> listQualificationByDriverId(@PathVariable("driverId") Integer driverId);

    /**
     * 修改资质
     *
     * @param qualification
     * @return
     */
    @PutMapping("/truckQualificationToFeign")
    BaseResponse truckQualificationToFeign(@RequestBody TruckQualification qualification);

    /**
     * 根据车队id获取已经审核通过的车队合同列表（运力合同列表）
     * @param truckTeamId
     * @return
     */
    @PostMapping("/getTruckTeamContractByTruckTeamId/{truckTeamId}")
    List<TruckTeamContractReturnDto> getTruckTeamContractByTruckTeamId(@PathVariable("truckTeamId") Integer truckTeamId);

    /**
     * 根据车牌号查询车辆
     * @param truckNumber
     * @return
     */
    @GetMapping("/getByTruckNumber")
    BaseResponse<GetTruckDto> getByTruckNumber(@RequestParam("truckNumber") String truckNumber);

    /**
     * 根据拉货类型查询车型列表
     * @param productName
     * @return
     */
    @GetMapping("/listTruckType/{productName}")
    BaseResponse<List<ListTruckTypeDto>> listTruckTypeByProductName(@PathVariable(value = "productName") String productName);

    /**
     *
     * 根据货物类型、客户id、运单完成时间获取客户合同和运力合同
     * @param contractDto
     * @return
     */
    @PostMapping("/getContractByContractDto")
    ContractDto getContractByContractDto(@RequestBody @Validated ContractDto contractDto);

    /**
     *根据车队合同id查询车队合同
     * @param id
     * @return
     */
    @GetMapping("getTruckTeamContractById/{id}")
    TruckTeamContractReturnDto getTruckTeamContractById(@PathVariable Integer id);
}
