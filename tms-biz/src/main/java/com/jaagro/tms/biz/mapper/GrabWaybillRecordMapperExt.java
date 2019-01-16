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
     * @param dto
     * @return
     */
    List<GrabWaybillRecord> listGrabWaybillByDriverId(GraWaybillConditionDto dto);
}