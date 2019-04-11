package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.GraWaybillConditionDto;
import com.jaagro.tms.biz.entity.GrabWaybillRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author baiyiran
 */
public interface GrabWaybillRecordMapperExt extends GrabWaybillRecordMapper {
    /**
     * 批量查询抢运单记录表
     *
     * @param grabWaybillRecords
     */
    void batchInsert(@Param("grabWaybillRecords") List<GrabWaybillRecord> grabWaybillRecords);

    /**
     * 根据司机id查询抢单记录
     *
     * @param dto
     * @return
     */
    List<GrabWaybillRecord> listGrabWaybillByCondition(GraWaybillConditionDto dto);

    /**
     * 更新当前接单人抢单记录
     *
     * @param dto
     */
    void updateGrabWaybillRecordByReceipt(GraWaybillConditionDto dto);

    /**
     * 更新当前拒单人记录
     *
     * @param dto
     */
    void updateGrabWaybillRecordByReject(GraWaybillConditionDto dto);

    /**
     * 批量更新
     *
     * @param ids
     */
    void batchUpdate(@Param("ids") List<Integer> ids);

    /**
     * 判断当前运单是否已经全部拒单
     *
     * @param dto
     * @return
     */
    List<GrabWaybillRecord> listNotRobGrabWaybill(GraWaybillConditionDto dto);

    /**
     * 主要判断当前运单是否是抢单模式
     *
     * @param waybillId
     * @return
     */
    List<GrabWaybillRecord> getGrabWaybillByWaybillId(@Param("waybillId") Integer waybillId);

    /**
     * 根据运单id 删除抢单记录表
     *
     * @param waybillId
     */
    void deleteByWaybillId(@Param("waybillId") Integer waybillId);
}