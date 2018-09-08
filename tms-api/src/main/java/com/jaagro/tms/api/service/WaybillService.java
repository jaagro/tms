package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.CreateWaybillDto;

import java.util.Map;

/**
 * @author gavin
 */
public interface WaybillService{

    /**
     * 创建运单
     * @param waybillDto 入参json
     * @return
     */
    Map<String,Object> createWaybill(CreateWaybillDto waybillDto);



}
