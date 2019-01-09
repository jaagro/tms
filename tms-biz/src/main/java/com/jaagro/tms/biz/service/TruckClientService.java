package com.jaagro.tms.biz.service;

import com.jaagro.tms.api.dto.truck.*;
import com.jaagro.utils.BaseResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
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

    @PostMapping("/getTruckTeamContractByTruckTeamId/truckTeamId")
    List<TruckTeamContractReturnDto> getTruckTeamContractByTruckTeamId(@PathVariable("truckTeamId") Integer truckTeamId);

    /**
     * 根据车牌号查询车辆
     * @param truckNumber
     * @return
     */
    @GetMapping("/getByTruckNumber")
    BaseResponse<GetTruckDto> getByTruckNumber(@RequestParam("truckNumber") String truckNumber);
}
