package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.driverapp.ShowTruckInfoDto;
import com.jaagro.tms.api.dto.peripheral.*;
import com.jaagro.tms.api.entity.RepairRecord;
import com.jaagro.tms.api.enums.GasolineCompanyNameEnum;
import com.jaagro.tms.api.enums.GasolineTypeEnum;
import com.jaagro.tms.api.enums.PaymentMethodEnum;
import com.jaagro.tms.api.service.GasolinePlusService;
import com.jaagro.tms.api.service.RepairRecordService;
import com.jaagro.tms.api.service.WashTruckService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.WashTruckRecord;
import com.jaagro.tms.web.vo.peripheral.GasolineRecordListVo;
import com.jaagro.tms.web.vo.peripheral.WashTruckImageVo;
import com.jaagro.tms.web.vo.peripheral.WashTruckRecordDetailVo;
import com.jaagro.tms.web.vo.peripheral.WashTruckRecordVo;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author @Gao.
 */
@Slf4j
@RestController
@Api(description = "周边服务管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class PeripheralAppController {

    @Autowired
    private RepairRecordService repairRecordService;
    @Autowired
    private GasolinePlusService gasolinePlusService;
    @Autowired
    private WashTruckService washTruckService;
    @Autowired
    private WaybillService waybillService;


    /**
     * 新增维续记录
     *
     * @param source
     * @return
     * @Author Gavin
     */
    @ApiOperation("新增维修记录")
    @PostMapping("/createRepairRecord")
    public BaseResponse createRepairRecord(@RequestBody @Validated RepairRecordDto source) {
        log.info("O createRepairRecord param={}", source);
        try {
            RepairRecord record = new RepairRecord();
            BeanUtils.copyProperties(source, record);
            repairRecordService.createRepairRecord(record);
        } catch (Exception ex) {
            log.error("O-createRepairRecord,param: " + source, ex);
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
    @GetMapping("/getRepairRecord/{id}")
    public BaseResponse getRepairRecordById(@PathVariable("id") Integer id) {
        log.info("O getRepairRecord id={}", id);
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
        log.info("O listRepairRecords criteriaDto={}", criteriaDto);
        PageInfo<RepairRecord> pageInfo = null;
        try {
            pageInfo = repairRecordService.listRepairRecordByCriteria(criteriaDto);
        } catch (Exception ex) {
            log.error("O listRepairRecords,param: " + criteriaDto, ex);
            return BaseResponse.errorInstance("查询失败");
        }

        return BaseResponse.successInstance(pageInfo);
    }

    @ApiOperation("加油申请")
    @PostMapping("/gasolineApply")
    public BaseResponse gasolineApply(@RequestBody CreateGasolineRecordDto dto) {
        gasolinePlusService.gasolineApply(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

    @ApiOperation("加油记录列表")
    @PostMapping("/listGasolineRecords")
    public BaseResponse listGasolineRecords(@RequestBody GasolineRecordParam param) {
        if (null == param.getPageNum() || null == param.getPageSize()) {
            throw new RuntimeException("参数不能为空");
        }
        PageInfo gasolineRecordDtos = gasolinePlusService.listGasolineRecords(param);
        List<CreateGasolineRecordDto> createGasolineRecordDtos = gasolineRecordDtos.getList();
        if (!CollectionUtils.isEmpty(createGasolineRecordDtos)) {
            for (CreateGasolineRecordDto createGasolineRecordDto : createGasolineRecordDtos) {
                if (null != createGasolineRecordDto.getGasolineCompany()) {
                    createGasolineRecordDto
                            .setGasolineCompany(GasolineCompanyNameEnum.getTypeByDesc(createGasolineRecordDto.getGasolineCompany()));
                }
                if (null != createGasolineRecordDto.getGasolineType()) {
                    createGasolineRecordDto
                            .setGasolineType(GasolineTypeEnum.getTypeByDesc(createGasolineRecordDto.getGasolineType()));
                }
                if (null != createGasolineRecordDto.getPaymentMethod()) {
                    createGasolineRecordDto.setPaymentMethod(PaymentMethodEnum.getTypeByDesc(createGasolineRecordDto.getPaymentMethod()));
                }
            }
        }
        return BaseResponse.successInstance(gasolineRecordDtos);
    }

//    @ApiOperation("加油详情")
//    @GetMapping("/gasolineDetails/{gasolineId}")
//    public BaseResponse gasolineDetails(@PathVariable("gasolineId") Integer gasolineId) {
//        return BaseResponse.successInstance(gasolinePlusService.gasolineDetails(gasolineId));
//    }

    @ApiOperation("加油管理")
    @PostMapping("/gasolineManagement")
    public BaseResponse gasolineManagement(@RequestBody GasolineRecordParam param) {
        return BaseResponse.successInstance(gasolinePlusService.gasolineManagement(param));
    }

    @ApiOperation("提交洗车记录")
    @PostMapping("/createWashTruckRecord")
    public BaseResponse createWashTruckRecord(@RequestBody @Validated CreateWashTruckRecordDto createWashTruckRecordDto) {
        log.info("O createWashTruckRecord {}", createWashTruckRecordDto);
        washTruckService.createWashTruckRecord(createWashTruckRecordDto);
        return BaseResponse.successInstance("提交洗车记录成功");
    }

    @ApiOperation("洗车记录列表")
    @PostMapping("/listWashTruckRecordByCriteria")
    public BaseResponse listWashTruckRecordByCriteria(@RequestBody @Validated ListWashTruckRecordCriteria criteria) {
        log.info("O listWashTruckRecordByCriteria {}", criteria);
        PageInfo pageInfo = washTruckService.listWashTruckRecordByCriteria(criteria);
        List<WashTruckRecord> recordList = pageInfo.getList();
        if (!recordList.isEmpty()) {
            List<WashTruckRecordVo> washTruckRecordVoList = new ArrayList<>();
            recordList.forEach(record -> {
                WashTruckRecordVo vo = new WashTruckRecordVo();
                BeanUtils.copyProperties(record, vo);
                washTruckRecordVoList.add(vo);
            });
            pageInfo.setList(washTruckRecordVoList);
        }
        return BaseResponse.successInstance(pageInfo);
    }

    @ApiOperation("洗车详情")
    @PostMapping("/getWashTruckRecordDetailById/{id}")
    public BaseResponse<WashTruckRecordDetailVo> getWashTruckRecordDetailById(@PathVariable("id") Integer id) {
        log.info("O getWashTruckRecordDetailById id={}", id);
        WashTruckRecordDto recordDto = washTruckService.getById(id);
        if (recordDto != null) {
            WashTruckRecordDetailVo detailVo = new WashTruckRecordDetailVo();
            BeanUtils.copyProperties(recordDto, detailVo);
            List<WashTruckImageDto> washTruckImageDtoList = recordDto.getWashTruckImageDtoList();
            if (!CollectionUtils.isEmpty(washTruckImageDtoList)) {
                List<WashTruckImageVo> imageVoList = new ArrayList<>();
                washTruckImageDtoList.forEach(imageDto -> {
                    WashTruckImageVo imageVo = new WashTruckImageVo();
                    BeanUtils.copyProperties(imageDto, imageVo);
                    imageVoList.add(imageVo);
                });
                detailVo.setImageList(imageVoList);
            }
            return BaseResponse.successInstance(detailVo);
        }
        return BaseResponse.queryDataEmpty();
    }

    @ApiOperation("获取车辆信息")
    @GetMapping("/getTruckInfo")
    public BaseResponse<ShowTruckInfoDto> getTruckInfo() {
        ShowTruckInfoDto truckInfo = waybillService.getTruckInfo();
        if (truckInfo != null) {
            return BaseResponse.successInstance(truckInfo);
        }
        return BaseResponse.queryDataEmpty();
    }
}
