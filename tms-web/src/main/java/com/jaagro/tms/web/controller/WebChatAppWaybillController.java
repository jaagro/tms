package com.jaagro.tms.web.controller;

import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.web.vo.chat.*;
import com.jaagro.utils.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gavin
 */
@RestController
@Api(description = "微信小程序运单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebChatAppWaybillController {

    @Autowired
    private WaybillRefactorService waybillService;

    /**
     * @param id
     * @return
     * @Author gavin
     */

    @ApiOperation("通过id获取微信小程序运单详情")
    @GetMapping("/getWaybillDetailById/{id}")
    public BaseResponse getWaybillDetailById(@PathVariable("id") Integer id) {
        WaybillDetailVo detailVo = new WaybillDetailVo();
        GetWaybillDetailDto detailDto = waybillService.getWaybillDetailById(id);
        BeanUtils.copyProperties(detailDto, detailVo);
        detailVo.setTruckNumber(detailDto.getAssginedTruckDto().getTruckNumber() == null ? "--" : detailDto.getAssginedTruckDto().getTruckNumber());
        detailVo.setDriverName(detailDto.getAssginedTruckDto().getDrivers().get(0).getName() == null ? "--" : detailDto.getAssginedTruckDto().getDrivers().get(0).getName());
        detailVo.setDriverPhoneNumber(detailDto.getAssginedTruckDto().getDrivers().get(0).getPhoneNumber() == null ? "--" : detailDto.getAssginedTruckDto().getDrivers().get(0).getPhoneNumber());
        //装货地
        ShowSiteVo loadSiteVo = new ShowSiteVo();
        BeanUtils.copyProperties(detailDto.getLoadSiteDto(), loadSiteVo);
        detailVo.setLoadSiteVo(loadSiteVo);
        //运单item
        List<GetWaybillItemDto> waybillItemDtos = detailDto.getWaybillItems();
        List<WaybillItemsVo> waybillItemsVos = new ArrayList<>();

        for (GetWaybillItemDto waybillItemDto : waybillItemDtos) {
            List<GetWaybillGoodsDto> goodsDtos = waybillItemDto.getGoods();
            List<WaybillGoodsVo> goodVos = new ArrayList<>();
            for (GetWaybillGoodsDto goodsDto : goodsDtos) {
                WaybillGoodsVo waybillGoodsVo = new WaybillGoodsVo();
                BeanUtils.copyProperties(goodsDto, waybillGoodsVo);
                goodVos.add(waybillGoodsVo);
            }
            WaybillItemsVo waybillItemsVo = new WaybillItemsVo();
            BeanUtils.copyProperties(waybillItemDto, waybillItemsVo);
            //运单卸货地
            ShowSiteVo uploadSiteVo = new ShowSiteVo();
            BeanUtils.copyProperties(waybillItemDto.getShowSiteDto(), uploadSiteVo);
            waybillItemsVo.setUploadSiteVo(uploadSiteVo);
            //运单货物
            waybillItemsVo.setGoods(goodVos);
            waybillItemsVos.add(waybillItemsVo);
        }
        detailVo.setWaybillItems(waybillItemsVos);
        //运单轨迹以及图片
        List<GetTrackingDto> trackingDtos = detailDto.getTracking();
        List<WaybillTrackingVo> trackingVos = new ArrayList<>();
        for (GetTrackingDto trackingDto : trackingDtos) {
            WaybillTrackingVo waybillTrackingVo = new WaybillTrackingVo();
            BeanUtils.copyProperties(trackingDto, waybillTrackingVo);

            List<GetTrackingImagesDto> imageDtos = trackingDto.getImageList();
            List<WaybillTrackingImagesVo> imageVos = new ArrayList<>();
            for (GetTrackingImagesDto imageDto : imageDtos) {
                WaybillTrackingImagesVo waybillTrackingImagesVo = new WaybillTrackingImagesVo();
                BeanUtils.copyProperties(imageDto, waybillTrackingImagesVo);
                imageVos.add(waybillTrackingImagesVo);
            }
            waybillTrackingVo.setImageList(imageVos);
            trackingVos.add(waybillTrackingVo);
        }
        detailVo.setTracking(trackingVos);

        return BaseResponse.successInstance(detailVo);
    }

}