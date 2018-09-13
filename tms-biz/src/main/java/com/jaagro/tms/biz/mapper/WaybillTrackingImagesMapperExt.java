package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.GetWaybillTrackingImagesDto;
import com.jaagro.tms.biz.entity.WaybillTrackingImages;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillTrackingImagesMapperExt extends WaybillTrackingImagesMapper {

    /**
     * 根据卸货地 装货地id
     * @param record
     * @return
     */
    List<GetWaybillTrackingImagesDto> listWaybillTrackingImage(WaybillTrackingImages record);
}