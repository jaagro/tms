package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.driverapp.GetWaybillTrackingImagesDto;
import com.jaagro.tms.biz.entity.WaybillTrackingImages;

import java.util.List;

public interface WaybillTrackingImagesMapper {
    /**
     * @mbggenerated 2018-09-05
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-09-05
     */
    int insert(WaybillTrackingImages record);

    /**
     * @mbggenerated 2018-09-05
     */
    int insertSelective(WaybillTrackingImages record);

    /**
     * @mbggenerated 2018-09-05
     */
    WaybillTrackingImages selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-09-05
     */
    int updateByPrimaryKeySelective(WaybillTrackingImages record);

    /**
     * @mbggenerated 2018-09-05
     */
    int updateByPrimaryKey(WaybillTrackingImages record);

    /**
     * 根据卸货地 装货地id
     * @param record
     * @return
     */
    List<GetWaybillTrackingImagesDto> listWaybillTrackingImage(WaybillTrackingImages record);
}