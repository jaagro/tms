package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.RepairRecord;

public interface RepairRecordMapper {
    /**
     * @mbggenerated 2018-12-05
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-12-05
     */
    int insert(RepairRecord record);

    /**
     * @mbggenerated 2018-12-05
     */
    int insertSelective(RepairRecord record);

    /**
     * @mbggenerated 2018-12-05
     */
    RepairRecord selectByPrimaryKey(Integer id);

    /**
     * @mbggenerated 2018-12-05
     */
    int updateByPrimaryKeySelective(RepairRecord record);

    /**
     * @mbggenerated 2018-12-05
     */
    int updateByPrimaryKey(RepairRecord record);
}