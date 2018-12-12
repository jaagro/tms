package com.jaagro.tms.biz.mapper;

import com.jaagro.tms.api.dto.waybill.ListEvaluateTypeDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EvaluateTypeMapperExt extends EvaluateTypeMapper {

    /**
     * 根据满意度等级 查询对应的描述
     * @param lever
     * @return
     */
    List<ListEvaluateTypeDto> listEvaluateType(@Param("lever") Integer lever);
}