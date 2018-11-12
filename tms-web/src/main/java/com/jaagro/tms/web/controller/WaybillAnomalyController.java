package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.constant.AnomalyImageTypeConstant;
import com.jaagro.tms.api.dto.anomaly.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.fee.WaybillCustomerFeeDto;
import com.jaagro.tms.api.dto.fee.WaybillFeeCondition;
import com.jaagro.tms.api.dto.fee.WaybillTruckFeeDto;
import com.jaagro.tms.api.service.WaybillAnomalyService;
import com.jaagro.tms.biz.service.OssSignUrlClientService;
import com.jaagro.tms.web.vo.anomaly.AnomalyAuditManagementListVo;
import com.jaagro.tms.web.vo.anomaly.AnomalyInformationVo;
import com.jaagro.tms.web.vo.anomaly.AnomalyTypeVo;
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
import java.util.Collections;
import java.util.Comparator;
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
        List<AnomalyTypeVo> anomalyTypeVos = new ArrayList<>();
        for (WaybillAnomalyTypeDto waybillAnomalyTypeDto : waybillAnomalyTypeDtos) {
            AnomalyTypeVo anomalyTypeVo = new AnomalyTypeVo();
            BeanUtils.copyProperties(waybillAnomalyTypeDto, anomalyTypeVo);
            anomalyTypeVos.add(anomalyTypeVo);
        }
        return BaseResponse.successInstance(anomalyTypeVos);
    }

    @ApiOperation("根据运单Id显示客户信息")
    @GetMapping("getCustomerByWaybillId/{waybillId}")
    public BaseResponse getCustomerByWaybillId(@PathVariable Integer waybillId) {
        return BaseResponse.successInstance(waybillAnomalyService.getCustomerByWaybillId(waybillId));
    }

    @ApiOperation("异常信息显示")
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
                    .setAnomalyImageType(AnomalyImageTypeConstant.ADD);
            List<AnomalyImageUrlDto> anomalyImageUrlDtos = new ArrayList<>();
            List<WaybillAnomalyImageDto> waybillAnomalyImageDtos = waybillAnomalyService.listWaybillAnomalyImageByCondition(waybillAnomalyImageCondition);
            if (!CollectionUtils.isEmpty(waybillAnomalyImageDtos)) {
                for (WaybillAnomalyImageDto waybillAnomalyImageDto : waybillAnomalyImageDtos) {
                    List<ImageUrlDto> imageUrlDtos = new ArrayList<>();
                    AnomalyImageUrlDto anomalyImageUrlDto = new AnomalyImageUrlDto();
                    //异常图片替换url地址
                    String[] strArray1 = {waybillAnomalyImageDto.getImageUrl()};
                    List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
                    //相对路径
                    imageUrlDtos.add(new ImageUrlDto()
                            .setImagesUrl(waybillAnomalyImageDto.getImageUrl())
                            .setKey(1));
                    //绝对路径
                    imageUrlDtos.add(new ImageUrlDto()
                            .setImagesUrl(urls.get(0).toString())
                            .setKey(2));
                    anomalyImageUrlDto
                            .setImageType(waybillAnomalyImageDto.getImageType())
                            .setAnomalyId(waybillAnomalyImageDto.getAnomalyId())
                            .setAnomalyImageId(waybillAnomalyImageDto.getId())
                            .setImageUrlDtos(imageUrlDtos);
                    anomalyImageUrlDtos.add(anomalyImageUrlDto);
                }
                anomalyInformationVo.setCreateAnomalyImageUrlDtos(anomalyImageUrlDtos);
            }
            //显示处理 审核信息
            processAuditInformation(waybillAnomalyDto, anomalyInformationVo);
        }
        return BaseResponse.successInstance(anomalyInformationVo);
    }

    @ApiOperation("异常信息处理")
    @PostMapping("anomalyInformationProcess")
    public BaseResponse anomalyInformationProcess(@RequestBody AnomalyInformationProcessDto dto) {
        waybillAnomalyService.anomalyInformationProcess(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

    @ApiOperation("异常处理管理列表")
    @PostMapping("anomalyManagementList")
    public BaseResponse anomalyManagementList(@RequestBody WaybillAnomalyCondition dto) {
        if (null != dto.getPageNum() && null != dto.getPageSize()) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        }
        return BaseResponse.successInstance(new PageInfo<>(waybillAnomalyService.anomalyManagementList(dto)));
    }

    @ApiOperation("异常审核管理列表")
    @PostMapping("anomalyAuditManagementList")
    public BaseResponse anomalyAuditManagementList(@RequestBody WaybillAnomalyCondition dto) {
        if (null != dto.getPageNum() && null != dto.getPageSize()) {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        }
        List<AnomalyManagementListDto> anomalyManagementListDtos = waybillAnomalyService.anomalyManagementList(dto);
        AnomalyAuditManagementListVo anomalyAuditManagementListVo = new AnomalyAuditManagementListVo();
        List<AnomalyAuditManagementListVo> anomalyAuditManagementListVos = new ArrayList<>();
        for (AnomalyManagementListDto anomalyManagementListDto : anomalyManagementListDtos) {
            BeanUtils.copyProperties(anomalyManagementListDto, anomalyAuditManagementListVo);
            anomalyAuditManagementListVos.add(anomalyAuditManagementListVo);
        }
        return BaseResponse.successInstance(new PageInfo<>(anomalyAuditManagementListVos));
    }

    @ApiOperation("异常信息审核")
    @PostMapping("anomalyInformationAudit")
    public BaseResponse anomalyInformationAudit(@RequestBody AnomalyInformationAuditDto dto) {
        waybillAnomalyService.anomalyInformationAudit(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

    @ApiOperation(("异常发送/审核退回"))
    @PostMapping("/changeAnomalyStatus")
    public BaseResponse changeAnomalyStatus(@RequestBody ChangeAnomalyParamVo param) {
        return BaseResponse.successInstance(waybillAnomalyService.changeAnomalyStatus(param.getIds(), param.getNowStatus()));
    }

    /**
     * @param waybillAnomalyDto
     * @param anomalyInformationVo
     * @author @Gao.
     * 异常处理 审核信息 显示
     */
    private void processAuditInformation(WaybillAnomalyDto waybillAnomalyDto, AnomalyInformationVo anomalyInformationVo) {
        //是否涉及费用调整
        List<AnomalyDeductCompensationDto> anomalyDeductCompensationDtos = new ArrayList<>();
        //客户侧费用
        WaybillFeeCondition waybillFeeCondition = new WaybillFeeCondition();
        waybillFeeCondition
                .setAnomalyId(waybillAnomalyDto.getId());
        List<WaybillCustomerFeeDto> waybillCustomerFeeDtos = waybillAnomalyService.listWaybillCustomerFeeByCondition(waybillFeeCondition);
        if (!CollectionUtils.isEmpty(waybillCustomerFeeDtos)) {
            AnomalyDeductCompensationDto customerFeeDto = new AnomalyDeductCompensationDto();
            customerFeeDto
                    .setUserType(1)
                    .setMoney(waybillCustomerFeeDtos.get(0).getMoney())
                    .setAdjustType(waybillCustomerFeeDtos.get(0).getAdjustType());
            anomalyDeductCompensationDtos.add(customerFeeDto);
        }
        //运力侧费用
        List<WaybillTruckFeeDto> waybillTruckFeeDtos = waybillAnomalyService.listWaybillTruckFeeByCondition(waybillFeeCondition);
        if (!CollectionUtils.isEmpty(waybillTruckFeeDtos)) {
            AnomalyDeductCompensationDto truckFeeDto = new AnomalyDeductCompensationDto();
            truckFeeDto
                    .setUserType(2)
                    .setMoney(waybillTruckFeeDtos.get(0).getMoney())
                    .setAdjustType(waybillTruckFeeDtos.get(0).getAdjustType());
            anomalyDeductCompensationDtos.add(truckFeeDto);
        }
        Collections.sort(anomalyDeductCompensationDtos, new Comparator<AnomalyDeductCompensationDto>() {
            @Override
            public int compare(AnomalyDeductCompensationDto o1, AnomalyDeductCompensationDto o2) {
                //降序
                return o2.getAdjustType().compareTo(o1.getAdjustType());
            }
        });
        anomalyInformationVo.setAnomalyDeductCompensationDto(anomalyDeductCompensationDtos);
        //处理上传异常图片显示
        WaybillAnomalyImageCondition anomalyImageCondition = new WaybillAnomalyImageCondition();
        anomalyImageCondition
                .setAnomalyImageType(AnomalyImageTypeConstant.PROCESS)
                .setAnomalyId(waybillAnomalyDto.getId());
        List<AnomalyImageUrlDto> anomalyImageUrlDtos = new ArrayList<>();
        List<WaybillAnomalyImageDto> waybillAnomalyImages = waybillAnomalyService.listWaybillAnomalyImageByCondition(anomalyImageCondition);
        if (!CollectionUtils.isEmpty(waybillAnomalyImages)) {
            for (WaybillAnomalyImageDto waybillAnomalyImage : waybillAnomalyImages) {
                List<ImageUrlDto> imageUrlDtos = new ArrayList<>();
                AnomalyImageUrlDto anomalyImageUrlDto = new AnomalyImageUrlDto();
                //异常图片替换
                String[] strArray1 = {waybillAnomalyImage.getImageUrl()};
                List<URL> urls = ossSignUrlClientService.listSignedUrl(strArray1);
                //相对路径
                imageUrlDtos.add(new ImageUrlDto()
                        .setImagesUrl(waybillAnomalyImage.getImageUrl())
                        .setKey(1));
                //绝对路径
                imageUrlDtos.add(new ImageUrlDto()
                        .setImagesUrl(urls.get(0).toString())
                        .setKey(2));
                Collections.sort(imageUrlDtos, new Comparator<ImageUrlDto>() {
                    @Override
                    public int compare(ImageUrlDto o1, ImageUrlDto o2) {
                        //降序
                        return o2.getKey().compareTo(o1.getKey());
                    }
                });
                anomalyImageUrlDto
                        .setAnomalyId(waybillAnomalyImage.getAnomalyId())
                        .setImageType(waybillAnomalyImage.getImageType())
                        .setAnomalyImageId(waybillAnomalyImage.getId())
                        .setImageUrlDtos(imageUrlDtos);
                anomalyImageUrlDtos.add(anomalyImageUrlDto);
            }
            anomalyInformationVo.setProcessAnomalyImageUrlDtos(anomalyImageUrlDtos);
        }
    }
}
