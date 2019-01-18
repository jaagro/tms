package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.entity.ChickenImportRecord;

/**
 * 毛鸡导入记录CRUD
 * @author yj
 * @date 2019-01-07 16:40
 */
public interface ChickenImportRecordMapper {
    /**
     *
     * @mbggenerated 2019-01-07
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-01-07
     */
    int insert(ChickenImportRecord record);

    /**
     *
     * @mbggenerated 2019-01-07
     */
    int insertSelective(ChickenImportRecord record);

    /**
     *
     * @mbggenerated 2019-01-07
     */
    ChickenImportRecord selectByPrimaryKey(Integer id);

    /**
     *
     * @mbggenerated 2019-01-07
     */
    int updateByPrimaryKeySelective(ChickenImportRecord record);

    /**
     *
     * @mbggenerated 2019-01-07
     */
    int updateByPrimaryKey(ChickenImportRecord record);
}