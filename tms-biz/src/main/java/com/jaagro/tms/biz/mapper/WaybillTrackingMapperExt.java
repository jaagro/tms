package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.ShowTrackingDto;
import com.jaagro.tms.biz.entity.WaybillTracking;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillTrackingMapperExt extends WaybillTrackingMapper{

    /**
     * 接单时间
     *
     * @param record
     * @return
     */
    WaybillTracking selectSingleTime(WaybillTracking record);

    /**
     * 根据运单查询运单轨迹
     * @param waybillId
     * @return
     */
    List<ShowTrackingDto> listWaybillTrackingByWaybillId(Integer waybillId);
}