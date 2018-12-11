package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.peripheral.ListRepairRecordCriteriaDto;
import com.jaagro.tms.api.entity.RepairRecord;

import java.util.List;

/**
 * @author Gavin
 * @Date 20181205
 */
public interface RepairRecordMapperExt extends  RepairRecordMapper {

    List<RepairRecord> listRepairRecordByCondition(ListRepairRecordCriteriaDto criteriaDto);

}