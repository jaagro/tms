package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.CreateWaybillDto;

import java.util.List;
import java.util.Map;

/**
 * @author gavin
 */
public interface WaybillService{

    /**
     * 创建运单
     * @param waybillDtos 入参json
     * @return
     */
    Map<String,Object> createWaybill(List<CreateWaybillDto> waybillDtos);

    /**
     * 计算配运单
     * @param waybillDto
     * @return
     */
    List<CreateWaybillDto> calculateWaybill(CreateWaybillDto waybillDto);

}
