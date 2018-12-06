package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.peripheral.CreateGasolineRecordDto;
import com.jaagro.tms.api.dto.peripheral.ListRepairRecordCriteriaDto;
import com.jaagro.tms.api.entity.RepairRecord;
import com.jaagro.tms.api.service.GasolinePlusService;
import com.jaagro.tms.api.service.RepairRecordService;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * @author @Gao.
 */
@Log4j
@RestController
@Api(description = "周边服务管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeripheralAppController {

    @Autowired
    private RepairRecordService repairRecordService;
    @Autowired
    private GasolinePlusService gasolinePlusService;

    /**
     * 新增维续记录
     *
     * @param record
     * @return
     * @Author Gavin
     */
    @ApiOperation("新增维修记录")
    @PostMapping("/createRepairRecord")
    public BaseResponse createRepairRecord(RepairRecord record) {
        Assert.notNull(record.getTruckNumber(), "车牌号码不能为空");
        Assert.notNull(record.getDriverId(), "司机ID不能为空");
        try {
            repairRecordService.createRepairRecord(record);
        } catch (Exception ex) {
            log.error("O-createRepairRecord,param: " + record, ex);
            return BaseResponse.errorInstance("失败");
        }
        return BaseResponse.successInstance("成功");
    }

    /**
     * 获取单个维修记录
     *
     * @param id
     * @return
     * @Author Gavin
     */
    @ApiOperation("获取单个维修记录")
    @RequestMapping("/getRepairRecord/{id}")
    public BaseResponse getRepairRecord(@PathVariable("id") Integer id) {
        RepairRecord repairRecord;
        try {
            repairRecord = repairRecordService.getRepairRecordById(id);
        } catch (Exception ex) {
            log.error("O-getRepairRecord,param: " + id, ex);
            return BaseResponse.errorInstance("获取失败");
        }
        return BaseResponse.successInstance(repairRecord);
    }

    /**
     * 维修记录列表分页
     *
     * @param criteriaDto
     * @return
     * @Author Gavin
     */
    @ApiOperation("维修记录列表分页")
    @PostMapping("/listRepairRecords")
    public BaseResponse listRepairRecords(@RequestBody ListRepairRecordCriteriaDto criteriaDto) {
        PageInfo pageInfo = null;

        try {
            pageInfo = repairRecordService.listRepairRecordByCriteria(criteriaDto);
        } catch (Exception ex) {
            log.error("O-listRepairRecords,param: " + criteriaDto, ex);
            return BaseResponse.errorInstance("查询失败");
        }

        return BaseResponse.successInstance(pageInfo);
    }

    @ApiOperation("加油记录")
    @GetMapping("/gasolineApplyApp")
    public BaseResponse gasolineApplyApp(@RequestBody CreateGasolineRecordDto dto) {
        gasolinePlusService.gasolineApply(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }
}
