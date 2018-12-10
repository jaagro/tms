package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.peripheral.ListRepairRecordCriteriaDto;
import com.jaagro.tms.api.entity.RepairRecord;

/**
 * @Author Gavin
 * @Date 20181206
 */

public interface RepairRecordService {

    /**
     * 新增维修记录
     * @param record
     * @return
     */
    int createRepairRecord(RepairRecord record);

    /**
     *  维修记录详情
     * @mbggenerated 2018-12-06
     */
    RepairRecord getRepairRecordById(Integer id);

    /**
     * 维修记录列表
     * @param criteriaDto
     * @return
     */
    PageInfo<RepairRecord> listRepairRecordByCriteria(ListRepairRecordCriteriaDto criteriaDto);
}
