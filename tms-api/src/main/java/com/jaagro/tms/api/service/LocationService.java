package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.LocationDto;

import java.util.List;

/**
 * 消息服务
 *
 * @author yj
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
}
