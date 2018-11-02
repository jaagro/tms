package com.jaagro.tms.web.controller;

import com.jaagro.constant.UserInfo;
import com.jaagro.tms.api.constant.WaybillStatus;
import com.jaagro.tms.api.dto.customer.ShowSiteDto;
import com.jaagro.tms.api.dto.receipt.UpdateWaybillGoodsReceiptDto;
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
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 回单管理
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
    private static final String RECEIPT_TRACKING_IMAGE_INFO = "回单补传单据";
    @GetMapping("/getReceiptWaybillDetailById/{id}")
    @ApiOperation("获取回单运单详情")
    public BaseResponse getReceiptWaybillDetailById(@PathVariable("id") Integer id){
        Waybill waybill = waybillMapperExt.selectByPrimaryKey(id);
        if (waybill == null){
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_EMPTY.getCode(),"运单id="+id+"不存在");
        }
        if (!WaybillStatus.ACCOMPLISH.equals(waybill.getWaybillStatus())){
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_EMPTY.getCode(),"运单未完成");
        }
        GetWaybillDetailDto waybillDetailDto = waybillRefactorService.getWaybillDetailById(id);
        if (waybillDetailDto == null){
            return BaseResponse.errorInstance("回单运单详情为空");
        }
        WayBillReceiptsVo wayBillReceiptsVo = generateWayBillReceiptsVo(waybillDetailDto);
        return BaseResponse.successInstance(wayBillReceiptsVo);
    }

    @PutMapping("/updateWaybillGoodsReceipt")
    @ApiOperation("回单修改运单货物信息")
    public BaseResponse updateWaybillGoodsReceipt(@RequestBody @Validated UpdateWaybillGoodsReceiptDto updateWaybillGoodsReceiptDto){
        log.debug("updateWaybillGoodsReceipt,{}",updateWaybillGoodsReceiptDto);
        boolean success = waybillService.updateWaybillGoodsReceipt(updateWaybillGoodsReceiptDto);
        if (success){
            return BaseResponse.successInstance("回单修改运单货物信息成功");
        }
        return BaseResponse.errorInstance("回单修改运单货物信息失败");
    }

    @PostMapping("/uploadReceiptImage")
    @ApiOperation("补传单据")
    public BaseResponse uploadReceiptImage(@RequestParam ("waybillId") Integer waybillId,@RequestParam ("imageUrl") String imageUrl){
        boolean success = waybillService.uploadReceiptImage(waybillId,imageUrl);
        if (success){
            return BaseResponse.successInstance("补传单据成功");
        }
        return BaseResponse.errorInstance("补传单据失败");
    }

    private WayBillReceiptsVo generateWayBillReceiptsVo(GetWaybillDetailDto waybillDetailDto){
        WayBillReceiptsVo wayBillReceiptsVo = new WayBillReceiptsVo();
        //客户id
        wayBillReceiptsVo.setCustomerId(waybillDetailDto.getLoadSiteDto() == null ? null : waybillDetailDto.getLoadSiteDto().getCustomerId());
        //货物类型
        wayBillReceiptsVo.setGoodsType(waybillDetailDto.getGoodType());
        //货物信息
        List<GetWaybillItemDto> waybillItems = waybillDetailDto.getWaybillItems();
        if (!CollectionUtils.isEmpty(waybillItems)){
            List<WaybillGoodsVo> waybillGoodsVoList = new ArrayList<WaybillGoodsVo>();
            for (GetWaybillItemDto getWaybillItemDto : waybillItems){
                ShowSiteDto showSiteDto = getWaybillItemDto.getShowSiteDto();
                List<GetWaybillGoodsDto> goods = getWaybillItemDto.getGoods();
                for (GetWaybillGoodsDto goodsDto : goods){
                    WaybillGoodsVo waybillGoodsVo = new WaybillGoodsVo();
                    waybillGoodsVo.setUnloadSiteName(showSiteDto == null ? null : showSiteDto.getSiteName());
                    waybillGoodsVo
                            .setId(goodsDto.getId())
                            .setWaybillId(goodsDto.getWaybillId())
                            .setWaybillItemId(goodsDto.getWaybillItemId())
                            .setUnloadSiteId(getWaybillItemDto.getUnloadSiteId())
                            .setGoodsName(goodsDto.getGoodsName())
                            .setUnloadSiteName(showSiteDto.getSiteName())
                            .setGoodsQuantity(goodsDto.getGoodsQuantity())
                            .setGoodsUnit(goodsDto.getGoodsUnit())
                            .setGoodsWeight(goodsDto.getGoodsWeight())
                            .setJoinDrug(goodsDto.getJoinDrug())
                            .setLoadQuantity(goodsDto.getLoadQuantity())
                            .setLoadWeight(goodsDto.getLoadWeight())
                            .setRequiredTime(getWaybillItemDto.getRequiredTime())
                            .setUnloadQuantity(goodsDto.getUnloadQuantity())
                            .setUnloadWeight(goodsDto.getUnloadWeight());
                    waybillGoodsVoList.add(waybillGoodsVo);
                }
            }
            wayBillReceiptsVo.setWaybillGoodsVoList(waybillGoodsVoList);
        }
        //回单补录信息
        List<GetTrackingDto> trackingDtoList = waybillDetailDto.getTracking();
        if (!CollectionUtils.isEmpty(trackingDtoList)){
            List<WayBillTrackingVo> wayBillTrackingVoList = new ArrayList<WayBillTrackingVo>();
            for (GetTrackingDto trackingDto : trackingDtoList){
                if (trackingDto.getTrackingType() == 2 && !RECEIPT_TRACKING_IMAGE_INFO.equals(trackingDto.getTrackingInfo())){
                    WayBillTrackingVo trackingVo = new WayBillTrackingVo();
                    UserInfo userInfo = trackingDto.getUserInfo();
                    trackingVo
                            .setCreateTime(trackingDto.getCreateTime())
                            .setOperator(userInfo == null ? null : userInfo.getName())
                            .setOperatorPhoneNum(userInfo == null ? null : userInfo.getPhoneNumber())
                            .setTrackingInfo(trackingDto.getTrackingInfo());
                    wayBillTrackingVoList.add(trackingVo);
                }
            }
            wayBillReceiptsVo.setWayBillTrackingVoList(wayBillTrackingVoList);
        }
        // 轨迹图片
        if (!CollectionUtils.isEmpty(trackingDtoList)){
            List<WaybillTrackingImagesVo> waybillTrackingImagesVoList = new ArrayList<WaybillTrackingImagesVo>();
            for (GetTrackingDto trackingDto : trackingDtoList){
                List<GetTrackingImagesDto> imageList = trackingDto.getImageList();
                if (!CollectionUtils.isEmpty(imageList)){
                    for (GetTrackingImagesDto imagesDto : imageList){
                        WaybillTrackingImagesVo imagesVo = new WaybillTrackingImagesVo();
                        UserInfo userInfo = imagesDto.getUserInfo();
                        imagesVo
                                .setCreateTime(imagesDto.getCreateTime())
                                .setCreateUserName(userInfo == null ? null : userInfo.getName())
                                .setImageType(imagesDto.getImageType())
                                .setImageUrl(imagesDto.getImageUrl());
                        waybillTrackingImagesVoList.add(imagesVo);
                    }
                }
            }
            wayBillReceiptsVo.setWaybillTrackingImagesVoList(waybillTrackingImagesVoList);
        }
        return wayBillReceiptsVo;
    }
}
