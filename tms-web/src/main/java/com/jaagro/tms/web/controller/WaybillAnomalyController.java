package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
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
        AnomalTypeVo anomalTypeVo = new AnomalTypeVo();
        for (WaybillAnomalTypeDto waybillAnomalTypeDto : waybillAnomalTypeDtos) {
            BeanUtils.copyProperties(waybillAnomalTypeDto, anomalTypeVo);
        }
        return BaseResponse.successInstance(waybillAnomalyService.displayAnormalType());
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
        BeanUtils.copyProperties(waybillAnomalyDtos.get(0), anomalInformationVo);
        //客户名称
        ShowCustomerDto customer = waybillAnomalyService.getCustomerByWaybillId(waybillAnomalyDtos.get(0).getWaybillId());
        anomalInformationVo.setCustomerName(customer.getCustomerName());
        //异常图片
        WaybillAnomalyImageCondtion waybillAnomalyImageCondtion = new WaybillAnomalyImageCondtion();
        waybillAnomalyImageCondtion
                .setAnomalyId(waybillAnomalyDtos.get(0).getId())
                .setCreateUserId(waybillAnomalyDtos.get(0).getCreateUserId());
        List<String> imageUrls = new ArrayList<>();
        List<WaybillAnomalyImageDto> waybillAnomalyImageDtos = waybillAnomalyService.listWaybillAnormalyImageByCondtion(waybillAnomalyImageCondtion);
        for (WaybillAnomalyImageDto waybillAnomalyImageDto : waybillAnomalyImageDtos) {
            //异常图片替换url地址
            String[] strArray1 = {waybillAnomalyImageDto.getImageUrl()};
            List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
            imageUrls.add(urls.get(0).toString());
        }
        anomalInformationVo.setImageUrl(imageUrls);
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
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

}
