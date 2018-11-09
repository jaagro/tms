package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.constant.AnomalyStatus;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondtion;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.service.OssSignUrlClientService;
import com.jaagro.tms.web.vo.anomaly.AnomalyInformationVo;
import com.jaagro.tms.web.vo.anomaly.AnomalTypeVo;
import com.jaagro.tms.web.vo.anomaly.ChangeAnomalyParamVo;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author @Gao.
 */
@Api(description = "运单异常申报管理", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class WaybillAnomalyController {
    @Autowired
    private WaybillAnomalyService waybillAnomalyService;
    @Autowired
    private OssSignUrlClientService ossSignUrlClientService;

    @ApiOperation("司机运单异常提交")
    @PostMapping("waybillAnomalySubmit")
    public BaseResponse waybillAnomalySubmit(@RequestBody WaybillAnomalyReportDto dto) {
        waybillAnomalyService.waybillAnomalySubmit(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

    @ApiOperation("运单异常类型显示")
    @GetMapping("displayAnomalyType")
    public BaseResponse displayAnomalyType() {
        List<WaybillAnomalyTypeDto> waybillAnomalyTypeDtos = waybillAnomalyService.displayAnomalyType();
        List<AnomalTypeVo> anomalyTypeVos = new ArrayList<>();
        for (WaybillAnomalyTypeDto waybillAnomalyTypeDto : waybillAnomalyTypeDtos) {
            AnomalTypeVo anomalTypeVo = new AnomalTypeVo();
            BeanUtils.copyProperties(waybillAnomalyTypeDto, anomalTypeVo);
            anomalyTypeVos.add(anomalTypeVo);
        }
        return BaseResponse.successInstance(anomalyTypeVos);
    }

    @ApiOperation("根据运单Id显示客户信息")
    @GetMapping("getCustomerByWaybillId/{waybillId}")
    public BaseResponse getCustomerByWaybillId(@PathVariable Integer waybillId) {
        return BaseResponse.successInstance(waybillAnomalyService.getCustomerByWaybillId(waybillId));
    }

    @ApiOperation("异常消息显示")
    @PostMapping("anomalyInformationDisplay")
    public BaseResponse anomalyInformationDisplay(@RequestBody WaybillAnomalyCondition dto) {
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyService.listWaybillAnomalyByCondition(dto);
        AnomalyInformationVo anomalyInformationVo = new AnomalyInformationVo();
        if (!CollectionUtils.isEmpty(waybillAnomalyDtos)) {
            WaybillAnomalyDto waybillAnomalyDto = waybillAnomalyDtos.get(0);
            BeanUtils.copyProperties(waybillAnomalyDto, anomalyInformationVo);
            //客户名称
            ShowCustomerDto customer = waybillAnomalyService.getCustomerByWaybillId(waybillAnomalyDtos.get(0).getWaybillId());
            anomalyInformationVo.setCustomerName(customer.getCustomerName());
            //异常图片
            WaybillAnomalyImageCondition waybillAnomalyImageCondition = new WaybillAnomalyImageCondition();
            waybillAnomalyImageCondition
                    .setAnomalyId(waybillAnomalyDto.getId())
                    .setCreateUserId(waybillAnomalyDto.getCreateUserId());
            List<String> imageUrls = new ArrayList<>();
            List<WaybillAnomalyImageDto> waybillAnomalyImageDtos = waybillAnomalyService.listWaybillAnomalyImageByCondition(waybillAnomalyImageCondition);
            if (!CollectionUtils.isEmpty(waybillAnomalyImageDtos)) {
                for (WaybillAnomalyImageDto waybillAnomalyImageDto : waybillAnomalyImageDtos) {
                    //异常图片替换url地址
                    String[] strArray1 = {waybillAnomalyImageDto.getImageUrl()};
                    List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
                    imageUrls.add(urls.get(0).toString());
                }
                anomalyInformationVo.setImageUrl(imageUrls);
            }
            //如果处理 或 审核已完成 则显示相关信息
            processAuditInformation(waybillAnomalyDto, anomalyInformationVo);
        }
        return BaseResponse.successInstance(anomalyInformationVo);
    }

    @ApiOperation("异常消息处理")
    @PostMapping("anomalyInformationProcess")
    public BaseResponse anomalyInformationProcess(@RequestBody AnomalyInformationProcessDto dto) {
        waybillAnomalyService.anomalyInformationProcess(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

    @ApiOperation("异常管理列表")
    @PostMapping("anomalyManagementList")
    public BaseResponse anomalyManagementList(@RequestBody WaybillAnomalyCondition dto) {
        return BaseResponse.successInstance(waybillAnomalyService.anomalyManagementList(dto));
    }

    @ApiOperation(("异常发送/审核退回"))
    @PostMapping("/changeAnomalyStatus")
    public BaseResponse changeAnomalyStatus(@RequestBody ChangeAnomalyParamVo param){
        return BaseResponse.successInstance(waybillAnomalyService.changeAnomalyStatus(param.getIds(), param.getNowStatus()));
    }

    /**
     * @author @Gao.
     * 异常处理 审核信息 显示
     * @param waybillAnomalyDto
     * @param anomalyInformationVo
     */
    private void processAuditInformation(WaybillAnomalyDto waybillAnomalyDto, AnomalyInformationVo anomalyInformationVo) {
        //处理消息显示
        if (AnomalyStatus.DONE.equals(waybillAnomalyDto.getProcessingStatus())) {
            //是否涉及费用调整
            List<AnomalyDeductCompensationDto> anomalyDeductCompensationDtos = new ArrayList<>();
            //客户侧费用
            WaybillFeeCondtion waybillFeeCondtion = new WaybillFeeCondtion();
            waybillFeeCondtion.
                    setAnomalyId(waybillAnomalyDto.getId());
            List<WaybillCustomerFeeDto> waybillCustomerFeeDtos = waybillAnomalyService.listWaybillCustomerFeeByCondition(waybillFeeCondtion);
            if (!CollectionUtils.isEmpty(waybillCustomerFeeDtos)) {
                AnomalyDeductCompensationDto customerFeeDto = new AnomalyDeductCompensationDto();
                customerFeeDto
                        .setUserType(1)
                        .setMoney(waybillCustomerFeeDtos.get(0).getMoney())
                        .setAdjustType(waybillCustomerFeeDtos.get(0).getAdjustType());
                anomalyDeductCompensationDtos.add(customerFeeDto);
            }
            //运力侧费用
            List<WaybillTruckFeeDto> waybillTruckFeeDtos = waybillAnomalyService.listWaybillTruckFeeByCondition(waybillFeeCondtion);
            if (!CollectionUtils.isEmpty(waybillTruckFeeDtos)) {
                AnomalyDeductCompensationDto truckFeeDto = new AnomalyDeductCompensationDto();
                truckFeeDto
                        .setUserType(2)
                        .setMoney(waybillTruckFeeDtos.get(0).getMoney())
                        .setAdjustType(waybillTruckFeeDtos.get(0).getAdjustType());
                anomalyDeductCompensationDtos.add(truckFeeDto);
            }
            anomalyInformationVo.setAnomalyDeductCompensationDto(anomalyDeductCompensationDtos);
            //处理上传异常图片显示
            WaybillAnomalyImageCondition anomalyImageCondition = new WaybillAnomalyImageCondition();
            anomalyImageCondition
                    .setAnomalyId(waybillAnomalyDto.getId())
                    .setCreateUserId(waybillAnomalyDto.getProcessorUserId());
            List<String> processImageUrl = new ArrayList<>();
            List<WaybillAnomalyImageDto> waybillAnomalyImages = waybillAnomalyService.listWaybillAnomalyImageByCondition(anomalyImageCondition);
            if (!CollectionUtils.isEmpty(waybillAnomalyImages)) {
                for (WaybillAnomalyImageDto waybillAnomalyImage : waybillAnomalyImages) {
                    //异常图片替换
                    String[] strArray1 = {waybillAnomalyImage.getImageUrl()};
                    List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
                    processImageUrl.add(urls.get(0).toString());
                }
                anomalyInformationVo.setProcessImageUrl(processImageUrl);
            }
        }
    }
}
