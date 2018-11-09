package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillAnomalyLog;

import java.util.List;

/**
 * @author yj
 * @date 2018/11/8
 */
public interface WaybillAnomalyLogMapperExt extends WaybillAnomalyLogMapper {

    Integer batchInsert(List<WaybillAnomalyLog> records);
}
