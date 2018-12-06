package com.jaagro.tms.biz.service.impl;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.peripheral.ListRepairRecordCriteriaDto;
import com.jaagro.tms.api.entity.RepairRecord;
import com.jaagro.tms.api.service.RepairRecordService;
import com.jaagro.tms.biz.mapper.RepairRecordMapperExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author Gavin
 * @Date 20181206
 */
@Service
public class RepairRecordServiceImpl implements RepairRecordService {

    @Autowired
    private RepairRecordMapperExt repairRecordMapper;
    /**
     * 新增维修记录
     *
     * @param record
     * @return
     */
    @Override
    public int createRepairRecord(RepairRecord record) {
        return 0;
    }

    /**
     * 维修记录详情
     *
     * @param id
     * @mbggenerated 2018-12-06
     */
    @Override
    public RepairRecord getRepairRecordById(Integer id) {
        return null;
    }

    /**
     * 维修记录列表
     *
     * @param criteriaDto
     * @return
     */
    @Override
    public PageInfo listRepairRecordByCriteria(ListRepairRecordCriteriaDto criteriaDto) {
        return null;
    }
}
