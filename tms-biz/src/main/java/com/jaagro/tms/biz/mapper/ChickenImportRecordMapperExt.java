package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.entity.ChickenImportRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
 /**
 * 毛鸡导入记录CRUD扩展
 * @author yj
 * @date 2019/1/7 16:42
 */
public interface ChickenImportRecordMapperExt extends ChickenImportRecordMapper{
    /**
     * 批量插入
     * @param chickenImportRecordList
     */
    void batchInsert(@Param("chickenImportRecordList") List<ChickenImportRecord> chickenImportRecordList);
}
