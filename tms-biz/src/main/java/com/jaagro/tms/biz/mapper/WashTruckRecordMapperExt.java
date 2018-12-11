package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.peripheral.ListWashTruckRecordCriteria;
import com.jaagro.tms.api.dto.peripheral.WashTruckRecordDto;
import com.jaagro.tms.biz.entity.WashTruckRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yj
 * @since 2018/12/7
 */
public interface WashTruckRecordMapperExt extends WashTruckRecordMapper {
    /**
     * 查询洗车列表
     * @param criteria
     * @return
     */
    List<WashTruckRecord> listWashTruckRecordByCriteria(ListWashTruckRecordCriteria criteria);

    /**
     * 根据id查询明细
     * @param id
     * @return
     */
    WashTruckRecordDto selectById(@Param("id") Integer id);
}
