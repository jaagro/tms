package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.CreateWaybillByExcelDto;

import java.util.Map;

/**
 * @author tony
 */
public interface WaybillService {

    /**
     * excel导入生成运单
     * @param waybills
     * @return
     * @throws Exception
     */
    Map<String, Object> createWaybillByExcel(CreateWaybillByExcelDto waybills) throws Exception;
}
