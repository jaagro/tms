package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.CreateWaybillEvaluateDto;
import com.jaagro.tms.api.dto.waybill.ListEvaluateTypeDto;

import java.util.List;

/**
 * @author @Gao.
 */
public interface WaybillEvaluateService {
    /**
     * 根据满意度等级 查询对应的描述
     *
     * @param
     * @return
     */
    List<ListEvaluateTypeDto> listEvaluateType();

    /**
     * 保存运单评价数据
     *
     * @param dto
     */
    void createWaybillEvaluate(CreateWaybillEvaluateDto dto);

    /**
     * 根据运单id查询评价信息
     *
     * @param waybillId
     * @return
     */
    ListEvaluateTypeDto getWaybillEvaluateByWaybillId(Integer waybillId);
}
