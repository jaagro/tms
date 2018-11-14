package com.jaagro.tms.web.controller;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.TrackingType;
import com.jaagro.tms.api.constant.WaybillStatus;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillGoodsDto;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillGoodsReceiptDto;
import com.jaagro.tms.api.dto.receipt.UploadReceiptImageDto;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import com.jaagro.tms.web.vo.receipt.WayBillReceiptsVo;
import com.jaagro.tms.web.vo.receipt.WayBillTrackingVo;
import com.jaagro.tms.web.vo.receipt.WaybillGoodsVo;
import com.jaagro.tms.web.vo.receipt.WaybillTrackingImagesVo;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 回单管理
 *
 * @author yj
 * @date 2018/10/31
 */
@RestController
@Api(description = "回单管理", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ReceiptController {
    @Autowired
    private WaybillRefactorService waybillRefactorService;
    @Autowired
    private WaybillService waybillService;
    @Autowired
    private WaybillMapperExt waybillMapperExt;

    @GetMapping("/getReceiptWaybillDetailById/{id}")
    @ApiOperation("获取回单运单详情")
    public BaseResponse getReceiptWaybillDetailById(@PathVariable("id") Integer id) {
        Waybill waybill = waybillMapperExt.selectByPrimaryKey(id);
        if (waybill == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_EMPTY.getCode(), "运单id=" + id + "不存在");
        }
        if (!WaybillStatus.ACCOMPLISH.equals(waybill.getWaybillStatus())) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_EMPTY.getCode(), "运单未完成");
        }
        GetWaybillDetailDto waybillDetailDto = waybillRefactorService.getWaybillDetailById(id);
        if (waybillDetailDto == null) {
            return BaseResponse.errorInstance("回单运单详情为空");
        }
        WayBillReceiptsVo wayBillReceiptsVo = generateWayBillReceiptsVo(waybillDetailDto);
        return BaseResponse.successInstance(wayBillReceiptsVo);
    }

    @PutMapping("/updateLoadGoodsReceipt")
    @ApiOperation("回单修改提货信息")
    public BaseResponse updateLoadGoodsReceipt(@RequestBody @Validated List<UpdateWaybillGoodsDto> updateWaybillGoodsDtoList) {
        log.info("updateLoadGoodsReceipt,{}", updateWaybillGoodsDtoList);
        if (CollectionUtils.isEmpty(updateWaybillGoodsDtoList)){
            return BaseResponse.errorInstance("提货信息不能为空");
        }
        boolean success = waybillService.updateLoadGoodsReceipt(updateWaybillGoodsDtoList);
        if (success) {
            return BaseResponse.successInstance("回单修改提货信息成功");
        }
        return BaseResponse.errorInstance("回单修改提货信息失败");
    }

    @PutMapping("/updateUnLoadGoodsReceipt")
    @ApiOperation("回单修改卸货信息")
    public BaseResponse updateUnLoadGoodsReceipt(@RequestBody @Validated List<UpdateWaybillGoodsDto> updateWaybillGoodsDtoList) {
        log.info("updateUnLoadGoodsReceipt,{}", updateWaybillGoodsDtoList);
        if (CollectionUtils.isEmpty(updateWaybillGoodsDtoList)){
            return BaseResponse.errorInstance("卸货信息不能为空");
        }
        boolean success = waybillService.updateUnLoadGoodsReceipt(updateWaybillGoodsDtoList);
        if (success) {
            return BaseResponse.successInstance("回单修改卸货信息成功");
        }
        return BaseResponse.errorInstance("回单修改卸货信息失败");
    }

    @Deprecated
    @PostMapping("/uploadReceiptImage")
    public BaseResponse uploadReceiptImage(@RequestBody @Validated UploadReceiptImageDto uploadReceiptImageDto) {
        boolean success = waybillService.uploadReceiptImage(uploadReceiptImageDto);
        if (success) {
            return BaseResponse.successInstance("补传单据成功");
        }
        return BaseResponse.errorInstance("补传单据失败");
    }

    private WayBillReceiptsVo generateWayBillReceiptsVo(GetWaybillDetailDto waybillDetailDto) {
        WayBillReceiptsVo wayBillReceiptsVo = new WayBillReceiptsVo();
        //客户id
        wayBillReceiptsVo.setCustomerId(waybillDetailDto.getLoadSiteDto() == null ? null : waybillDetailDto.getLoadSiteDto().getCustomerId());
        //货物类型
        wayBillReceiptsVo.setGoodsType(waybillDetailDto.getGoodType());
        //运单卸货地
        List<GetWaybillItemDto> waybillItems = waybillDetailDto.getWaybillItems();
        //运单货物信息
        List<WaybillGoodsVo> waybillGoodsList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(waybillItems)) {
            for (GetWaybillItemDto getWaybillItemDto : waybillItems) {
                ShowSiteDto showSiteDto = getWaybillItemDto.getShowSiteDto();
                List<GetWaybillGoodsDto> goods = getWaybillItemDto.getGoods();
                if (!CollectionUtils.isEmpty(goods)) {
                    for (GetWaybillGoodsDto goodsDto : goods) {
                        WaybillGoodsVo waybillGoodsVo = new WaybillGoodsVo();
                        waybillGoodsVo
                                .setUnloadSiteName(showSiteDto == null ? null : showSiteDto.getSiteName())
                                .setUnloadSiteId(getWaybillItemDto.getUnloadSiteId())
                                .setWaybillItemId(getWaybillItemDto.getId())
                                .setRequiredTime(getWaybillItemDto.getRequiredTime())
                                .setSignStatus(getWaybillItemDto.getSignStatus());
                        BeanUtils.copyProperties(goodsDto, waybillGoodsVo);
                        waybillGoodsList.add(waybillGoodsVo);
                    }
                }
            }
            wayBillReceiptsVo.setWaybillGoodsList(waybillGoodsList);
        }
        //补录记录
        List<GetTrackingDto> trackingDtoList = waybillDetailDto.getTracking();
        if (!CollectionUtils.isEmpty(trackingDtoList)) {
            SimpleDateFormat sdf = new SimpleDateFormat();
            for (GetTrackingDto trackingDto : trackingDtoList) {
                if (trackingDto.getTrackingType().equals(TrackingType.LOAD_RECEIPT)) {
                    WayBillTrackingVo loadTrackingVo = new WayBillTrackingVo();
                    UserInfo userInfo = trackingDto.getUserInfo();
                    loadTrackingVo
                            .setCreateTime(trackingDto.getCreateTime())
                            .setOperator(userInfo == null ? null : userInfo.getName());
                    wayBillReceiptsVo.setLoadTracking(loadTrackingVo);
                }
                if (trackingDto.getTrackingType().equals(TrackingType.UNLOAD_RECEIPT)) {
                    WayBillTrackingVo unloadTrackingVo = new WayBillTrackingVo();
                    UserInfo userInfo = trackingDto.getUserInfo();
                    unloadTrackingVo
                            .setCreateTime(trackingDto.getCreateTime())
                            .setOperator(userInfo == null ? null : userInfo.getName());
                    wayBillReceiptsVo.setUnLoadTracking(unloadTrackingVo);
                }
            }

        }
        // 轨迹图片
        if (!CollectionUtils.isEmpty(trackingDtoList)) {
            // 提货图片
            List<WaybillTrackingImagesVo> loadImagesList = new ArrayList<>();
            // 卸货图片
            List<WaybillTrackingImagesVo> unLoadImagesList = new ArrayList<>();
            for (GetTrackingDto trackingDto : trackingDtoList) {
                List<GetTrackingImagesDto> imageList = trackingDto.getImageList();
                if (!CollectionUtils.isEmpty(imageList)) {
                    for (GetTrackingImagesDto imagesDto : imageList) {
                        WaybillTrackingImagesVo imagesVo = new WaybillTrackingImagesVo();
                        UserInfo userInfo = imagesDto.getUserInfo();
                        imagesVo
                                .setCreateTime(imagesDto.getCreateTime())
                                .setCreateUserName(userInfo == null ? null : userInfo.getName())
                                .setImageType(imagesDto.getImageType())
                                .setImageUrl(imagesDto.getImageUrl());
                        if (WaybillStatus.LOAD_PRODUCT.equals(trackingDto.getOldStatus())) {
                            loadImagesList.add(imagesVo);
                        }
                        if (WaybillStatus.SIGN.equals(trackingDto.getOldStatus())) {
                            unLoadImagesList.add(imagesVo);
                        }
                    }
                }
            }
            wayBillReceiptsVo.setLoadImagesList(loadImagesList);
            wayBillReceiptsVo.setUnLoadImagesList(unLoadImagesList);
        }
        return wayBillReceiptsVo;
    }
}
