package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.LocationDto;
import com.jaagro.tms.api.dto.waybill.ShowLocationDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author gavin
 */
public interface LocationMapperExt extends LocationMapper {
    /**
     * 批量新增司机定位数据
     *
     * @param locationList
     * @return
     */
    int insertBatch(List<LocationDto> locationList);


    /**
     * 根据运单Id查询轨迹
     *
     * @param waybillId
     * @return
     */
    List<ShowLocationDto> listLocationsByWaybillId(@Param("waybillId") Integer waybillId, @Param("interval")Integer interval);
}