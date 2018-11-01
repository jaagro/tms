package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.biz.entity.WaybillAnomalyType;

import java.util.List;

/**
 * @author tony
 */
public interface WaybillAnomalyTypeMapperExt extends WaybillAnomalyTypeMapper {
    /**
     * 列出所有异常类型
     * @return
     *  @author @Gao.
     */
    List<WaybillAnomalyType> listAnomalyType();

}