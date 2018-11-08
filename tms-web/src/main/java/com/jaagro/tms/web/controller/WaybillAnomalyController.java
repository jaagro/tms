package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondtion;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.service.OssSignUrlClientService;
import com.jaagro.tms.web.vo.anomaly.AnomalInformationVo;
import com.jaagro.tms.web.vo.anomaly.AnomalTypeVo;
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
    @GetMapping("displayAnomalType")
    public BaseResponse displayAnormalType() {
        List<WaybillAnomalTypeDto> waybillAnomalTypeDtos = waybillAnomalyService.displayAnormalType();
        List<AnomalTypeVo> anomalTypeVos = new ArrayList<>();
        for (WaybillAnomalTypeDto waybillAnomalTypeDto : waybillAnomalTypeDtos) {
            AnomalTypeVo anomalTypeVo = new AnomalTypeVo();
            BeanUtils.copyProperties(waybillAnomalTypeDto, anomalTypeVo);
            anomalTypeVos.add(anomalTypeVo);
        }
        return BaseResponse.successInstance(anomalTypeVos);
    }

    @ApiOperation("根据运单Id显示客户信息")
    @GetMapping("getCustomerByWaybillId/{waybillId}")
    public BaseResponse getCustomerByWaybillId(@PathVariable Integer waybillId) {
        return BaseResponse.successInstance(waybillAnomalyService.getCustomerByWaybillId(waybillId));
    }

    @ApiOperation("异常消息显示")
    @PostMapping("anomalInformationDisplay")
    public BaseResponse anomalInformationDisplay(@RequestBody WaybillAnomalyCondtion dto) {
        List<WaybillAnomalyDto> waybillAnomalyDtos = waybillAnomalyService.listWaybillAnomalyByCondition(dto);
        AnomalInformationVo anomalInformationVo = new AnomalInformationVo();
        if (!CollectionUtils.isEmpty(waybillAnomalyDtos)) {
            WaybillAnomalyDto waybillAnomalyDto = waybillAnomalyDtos.get(0);
            BeanUtils.copyProperties(waybillAnomalyDto, anomalInformationVo);
            //客户名称
            ShowCustomerDto customer = waybillAnomalyService.getCustomerByWaybillId(waybillAnomalyDtos.get(0).getWaybillId());
            anomalInformationVo.setCustomerName(customer.getCustomerName());
            //异常图片
            WaybillAnomalyImageCondtion waybillAnomalyImageCondtion = new WaybillAnomalyImageCondtion();
            waybillAnomalyImageCondtion
                    .setAnomalyId(waybillAnomalyDto.getId())
                    .setCreateUserId(waybillAnomalyDto.getCreateUserId());
            List<String> imageUrls = new ArrayList<>();
            List<WaybillAnomalyImageDto> waybillAnomalyImageDtos = waybillAnomalyService.listWaybillAnormalyImageByCondtion(waybillAnomalyImageCondtion);
            if (!CollectionUtils.isEmpty(waybillAnomalyImageDtos)) {
                for (WaybillAnomalyImageDto waybillAnomalyImageDto : waybillAnomalyImageDtos) {
                    //异常图片替换url地址
                    String[] strArray1 = {waybillAnomalyImageDto.getImageUrl()};
                    List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
                    imageUrls.add(urls.get(0).toString());
                }
                anomalInformationVo.setImageUrl(imageUrls);
            }
            //如果异常已处理 显示异常处理结果信息
            if ("已处理".equals(waybillAnomalyDto.getProcessingStatus())) {
                //是否涉及费用调整
                List<AnomalDeductCompensationDto> anomalDeductCompensationDtos = new ArrayList<>();
                //客户侧费用
                WaybillFeeCondtion waybillFeeCondtion = new WaybillFeeCondtion();
                waybillFeeCondtion.
                        setAnomalyId(waybillAnomalyDto.getId());
                List<WaybillCustomerFeeDto> waybillCustomerFeeDtos = waybillAnomalyService.listWaybillCustomerFeeByCondtion(waybillFeeCondtion);
                if (!CollectionUtils.isEmpty(waybillCustomerFeeDtos)) {
                    AnomalDeductCompensationDto customerFeeDto = new AnomalDeductCompensationDto();
                    customerFeeDto
                            .setUserType(1)
                            .setMoney(waybillCustomerFeeDtos.get(0).getMoney())
                            .setAdjustType(waybillCustomerFeeDtos.get(0).getAdjustType());
                    anomalDeductCompensationDtos.add(customerFeeDto);
                }
                //运力侧费用
                List<WaybillTruckFeeDto> waybillTruckFeeDtos = waybillAnomalyService.listWaybillTruckFeeByCondtion(waybillFeeCondtion);
                if (!CollectionUtils.isEmpty(waybillTruckFeeDtos)) {
                    AnomalDeductCompensationDto truckFeeDto = new AnomalDeductCompensationDto();
                    truckFeeDto
                            .setUserType(2)
                            .setMoney(waybillTruckFeeDtos.get(0).getMoney())
                            .setAdjustType(waybillTruckFeeDtos.get(0).getAdjustType());
                    anomalDeductCompensationDtos.add(truckFeeDto);
                }
                anomalInformationVo.setAnomalDeductCompensationDto(anomalDeductCompensationDtos);
                //处理上传异常图片显示
                WaybillAnomalyImageCondtion anomalyImageCondtion = new WaybillAnomalyImageCondtion();
                anomalyImageCondtion
                        .setAnomalyId(waybillAnomalyDto.getId())
                        .setCreateUserId(waybillAnomalyDto.getProcessorUserId());
                List<String> processImageUrl = new ArrayList<>();
                List<WaybillAnomalyImageDto> waybillAnomalyImages = waybillAnomalyService.listWaybillAnormalyImageByCondtion(anomalyImageCondtion);
                if (!CollectionUtils.isEmpty(waybillAnomalyImageDtos)) {
                    for (WaybillAnomalyImageDto waybillAnomalyImage : waybillAnomalyImages) {
                        //异常图片替换
                        String[] strArray1 = {waybillAnomalyImage.getImageUrl()};
                        List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
                        processImageUrl.add(urls.get(0).toString());

                    }
                    anomalInformationVo.setProcessImageUrl(processImageUrl);
                }
            }
        }
        return BaseResponse.successInstance(anomalInformationVo);
    }

    @ApiOperation("异常消息处理")
    @PostMapping("anomalInformationProcess")
    public BaseResponse anormalInformationProcess(@RequestBody AnomalInformationProcessDto dto) {
        waybillAnomalyService.anormalInformationProcess(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

    @ApiOperation("异常管理列表")
    @PostMapping("anomalManagementList")
    public BaseResponse anomalManagementList(@RequestBody WaybillAnomalyCondtion dto) {
        return BaseResponse.successInstance(waybillAnomalyService.anomalManagementList(dto));
    }
}
