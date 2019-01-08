package com.jaagro.tms.web.controller;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.constant.TrackingType;
import com.jaagro.tms.api.dto.base.MyInfoVo;
import com.jaagro.tms.api.dto.waybill.*;
import com.jaagro.tms.api.service.WaybillEvaluateService;
import com.jaagro.tms.api.service.WaybillRefactorService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.service.impl.CurrentUserService;
import com.jaagro.tms.web.vo.chat.*;
import com.jaagro.utils.BaseResponse;
import com.jaagro.utils.ResponseStatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author gavin
 */
@RestController
@Api(description = "微信小程序运单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class WebChatAppWaybillController {

    @Autowired
    private WaybillRefactorService waybillRefactorService;
    @Autowired
    private WaybillEvaluateService waybillEvaluateService;
    @Autowired
    private CurrentUserService userService;
    @Autowired
    private WaybillService waybillService;

    /**
     * @param id
     * @return
     * @Author gavin
     */
    @ApiOperation("通过id获取微信小程序运单详情")
    @GetMapping("/getWaybillDetailById/{id}")
    public BaseResponse getWaybillDetailById(@PathVariable("id") Integer id) {
        WaybillDetailVo detailVo = new WaybillDetailVo();
        GetWaybillDetailDto detailDto = waybillRefactorService.getWaybillDetailById(id);
        BeanUtils.copyProperties(detailDto, detailVo);
        detailVo.setTruckNumber(detailDto.getAssginedTruckDto() == null ? "--" : detailDto.getAssginedTruckDto().getTruckNumber());
        if (null == detailDto.getAssginedTruckDto()) {
            detailVo.setDriverName("--");
            detailVo.setDriverPhoneNumber("--");

        } else {
            if (CollectionUtils.isEmpty(detailDto.getAssginedTruckDto().getDrivers())) {
                detailVo.setDriverName("--");
                detailVo.setDriverPhoneNumber("--");
            } else if (null != detailDto.getAssginedTruckDto().getDrivers().get(0)) {
                detailVo.setDriverName(detailDto.getAssginedTruckDto().getDrivers().get(0).getName() == null ? "--" : detailDto.getAssginedTruckDto().getDrivers().get(0).getName());
                detailVo.setDriverPhoneNumber(detailDto.getAssginedTruckDto().getDrivers().get(0).getPhoneNumber() == null ? "--" : detailDto.getAssginedTruckDto().getDrivers().get(0).getPhoneNumber());
            }
        }
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
        // 过滤非运输轨迹 add by jia.yu 20181115
        Iterator<GetTrackingDto> iterator = trackingDtos.iterator();
        while (iterator.hasNext()) {
            GetTrackingDto getTrackingDto = iterator.next();
            if (!TrackingType.TRANSPORT.equals(getTrackingDto.getTrackingType())) {
                iterator.remove();
            }
        }
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

    @ApiOperation("评价描述")
    @GetMapping("/listEvaluateType")
    public BaseResponse listEvaluateType() {
        List<ListEvaluateTypeDto> listEvaluateTypeDtos = waybillEvaluateService.listEvaluateType();
        List<ListEvaluateTypeVo> listEvaluateTypeVos = new ArrayList<>();
        for (ListEvaluateTypeDto listEvaluateTypeDto : listEvaluateTypeDtos) {
            ListEvaluateTypeVo listEvaluateTypeVo = new ListEvaluateTypeVo();
            BeanUtils.copyProperties(listEvaluateTypeDto, listEvaluateTypeVo);
            listEvaluateTypeVos.add(listEvaluateTypeVo);
        }
        return BaseResponse.successInstance(listEvaluateTypeVos);
    }

    @ApiOperation("运单评价")
    @PostMapping("/waybillEvaluate")
    public BaseResponse waybillEvaluate(@RequestBody CreateWaybillEvaluateDto dto) {
        waybillEvaluateService.createWaybillEvaluate(dto);
        return BaseResponse.successInstance(ResponseStatusCode.OPERATION_SUCCESS);
    }

    /**
     * 我的运单列表【装卸货端】
     *
     * @param criteriaDto
     * @return
     */
    @ApiOperation("我的运单列表【装卸货端】")
    @PostMapping("/listWebChatWaybillByCriteria")
    public BaseResponse<PageInfo> listWebChatWaybillByCriteria(@RequestBody ListWebChatWaybillCriteriaDto criteriaDto) {
        if (criteriaDto.getPageNum() == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageNum不能为空");
        }
        if (criteriaDto.getPageSize() == null) {
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "pageSize不能为空");
        }
        PageInfo pageInfo;
        try {
            MyInfoVo myInfoVo = userService.getWebChatCurrentByCustomerUser();
            if (myInfoVo == null) {
                return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), "获取当前用户失败");
            }
            criteriaDto.setUserId(myInfoVo.getSiteId());
            criteriaDto.setUserType(myInfoVo.getUserType());
            pageInfo = waybillRefactorService.listWebChatWaybillByCriteria(criteriaDto);
            //得到分页数据
            List<ListWaybillDto> listWaybillDtos = pageInfo.getList();
            if (!CollectionUtils.isEmpty(listWaybillDtos)) {
                //填充分页数据的详情
                List<GetWaybillDto> getWaybillDtos = new ArrayList<>();
                for (ListWaybillDto waybillDto : listWaybillDtos) {
                    GetWaybillDto getWaybillDto = waybillService.getWaybillById(waybillDto.getId());
                    getWaybillDtos.add(getWaybillDto);
                }
                //转换为Vo
                if (getWaybillDtos.size() > 0) {
                    List<ListWaybillVo> waybillVoList = new ArrayList<>();
                    this.setWaybillDetail(getWaybillDtos, waybillVoList);
                    pageInfo.setList(waybillVoList);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return BaseResponse.errorInstance(ResponseStatusCode.QUERY_DATA_ERROR.getCode(), ex.getMessage());
        }
        return BaseResponse.successInstance(pageInfo);
    }

    /**
     * 填充运单详情
     *
     * @param getWaybillDtos
     * @param waybillVoList
     */
    public void setWaybillDetail(List<GetWaybillDto> getWaybillDtos, List<ListWaybillVo> waybillVoList) {
        //运单
        for (GetWaybillDto waybillDto : getWaybillDtos) {

            ListWaybillVo waybillVo = new ListWaybillVo();
//            ListEvaluateTypeDto waybillEvaluate = waybillEvaluateService.getWaybillEvaluateByWaybillId(waybillDto.getId());
//            if (waybillEvaluate != null) {
//                waybillVo.setWaybillEvaluate(true);
//            }
            BeanUtils.copyProperties(waybillDto, waybillVo);
            //运单装货地
            if (waybillDto.getLoadSite() != null) {
                ShowSiteVo showSiteVo = new ShowSiteVo();
                BeanUtils.copyProperties(waybillDto.getLoadSite(), showSiteVo);
                waybillVo.setLoadSiteVo(showSiteVo);
            }
            List<ListWaybillItemsVo> listWaybillItemsVoList = new ArrayList<>();
            //运单详细
            if (!CollectionUtils.isEmpty(waybillDto.getWaybillItems())) {
                for (GetWaybillItemDto waybillItemDto : waybillDto.getWaybillItems()) {
                    ListWaybillItemsVo waybillItemsVo = new ListWaybillItemsVo();
                    BeanUtils.copyProperties(waybillItemDto, waybillItemsVo);
                    //运单详情的卸货地址
                    if (waybillItemDto.getShowSiteDto() != null) {
                        ShowSiteVo showSiteVo = new ShowSiteVo();
                        BeanUtils.copyProperties(waybillItemDto.getShowSiteDto(), showSiteVo);
                        waybillItemsVo.setUploadSiteVo(showSiteVo);
                    }
                    listWaybillItemsVoList.add(waybillItemsVo);
                }
            }
            waybillVo.setWaybillItemsVoList(listWaybillItemsVoList);
            waybillVoList.add(waybillVo);
        }
    }
}