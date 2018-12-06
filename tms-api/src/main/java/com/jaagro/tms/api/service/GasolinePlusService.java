package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.peripheral.CreateGasolineRecordDto;

/**
 * @author @Gao.
 */
public interface GasolinePlusService {
    /**
     * 我要加油服务
     * @author @Gao.
     *
     * @param dto
     */
    void gasolineApply(CreateGasolineRecordDto dto);
}
