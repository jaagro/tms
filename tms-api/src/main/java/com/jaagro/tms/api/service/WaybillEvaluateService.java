package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.CreateWaybillEvaluateDto;
import com.jaagro.tms.api.dto.waybill.ListEvaluateTypeDto;
import com.jaagro.tms.api.dto.waybill.ListWaybillEvaluateDto;

import java.util.List;

/**
 * @author @Gao.
 */
public interface WaybillEvaluateService {
    /**
     * 根据满意度等级 查询对应的描述
     *
     * @param lever
     * @return
     */
    List<ListEvaluateTypeDto> listEvaluateType(Integer lever);

    /**
     * 保存运单评价数据
     * @param dto
     */
    void createWaybillEvaluate(CreateWaybillEvaluateDto dto);
}
