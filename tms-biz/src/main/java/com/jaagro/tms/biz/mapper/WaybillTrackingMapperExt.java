package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.driverapp.ShowTrackingDto;
import com.jaagro.tms.biz.entity.WaybillTracking;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillTrackingMapperExt extends WaybillTrackingMapper {

    /**
     * 接单时间
     *
     * @param record
     * @return
     */
    WaybillTracking selectSingleTime(WaybillTracking record);

    /**
     * 根据运单查询运单轨迹
     *
     * @param waybillId
     * @return
     */
    List<ShowTrackingDto> listWaybillTrackingByWaybillId(Integer waybillId);

    /**
     * add by Gavin
     * 根据运单id获取运单轨迹按id倒序
     *
     * @param waybillId
     * @return
     */

    List<ShowTrackingDto> getWaybillTrackingByWaybillId(Integer waybillId);

    /**
     * 根据运单号轨迹类型物理删除运单轨迹
     *
     * @param waybillId
     * @param trackingType
     * @author yj
     */
    void deleteByWaybillIdAndTrackingType(@Param("waybillId") Integer waybillId, @Param("trackingType") Integer trackingType);

    /**
     * 批量插入运单轨迹
     *
     * @param trackingList
     * @return
     * @author yj
     */
    Integer batchInsert(@Param("trackingList") List<WaybillTracking> trackingList);

    /**
     * 根据运单id 查询异常轨迹id
     *
     * @param waybillId
     * @return
     */
    List<Integer> listWaybillTrackingIdByWaybillId(@Param("waybillId") Integer waybillId);

    /**
     * 批量逻辑删除运单轨迹
     *
     * @param ids
     */
    void deleteWaybillTrackingId(@Param("ids") List<Integer> ids);

    /**
     * 根据运单获取派单调度id
     *
     * @param waybillId
     * @return
     */
    Integer getCreateUserByWaybillId(@Param("waybillId") Integer waybillId);
}