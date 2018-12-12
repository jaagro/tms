package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.dto.waybill.ShowLocationDto;

import java.util.List;

/**
 * 消息服务
 *
 * @author gavin
 * @date 2018/10/29
 */
public interface LocationService {
    /**
     * 批量新增司机定位数据
     *
     * @param locationList
     * @return
     */
    int insertBatch(List<LocationDto> locationList);

    /**
     * 批量新增司机定位数据到MQ
     * @param LocationDtos
     */
    void insertBatchMQ(List<LocationDto> LocationDtos);

    /**
     * 根据运单Id查询轨迹
     *
     * @param waybillId
     * @return
     */
    List<ShowLocationDto> locationsByWaybillId(Integer waybillId,Integer interval);
}
