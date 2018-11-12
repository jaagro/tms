package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.ShowTrackingDto;
import com.jaagro.tms.biz.entity.WaybillTracking;
import org.apache.ibatis.annotations.Param;

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

    /**
     * add by Gavin
     * 根据运单id获取运单轨迹按id倒序
     * @param waybillId
     * @return
     */

    List<ShowTrackingDto> getWaybillTrackingByWaybillId(Integer waybillId);

    /**
     * 根据运单号轨迹类型物理删除运单轨迹
     * @author yj
     * @param waybillId
     * @param trackingType
     */
    void deleteByWaybillIdAndTrackingType(@Param("waybillId") Integer waybillId,@Param("trackingType") Integer trackingType);

    /**
     * 批量插入运单轨迹
     * @author yj
     * @param trackingList
     * @return
     */
    Integer batchInsert(@Param("trackingList") List<WaybillTracking> trackingList);
}