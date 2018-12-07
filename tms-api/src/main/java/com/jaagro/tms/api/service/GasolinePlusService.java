package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.peripheral.CreateGasolineRecordDto;

import java.util.List;

/**
 * @author @Gao.
 */
public interface GasolinePlusService {
    /**
     * 我要加油服务
     *
     * @param dto
     * @author @Gao.
     */
    void gasolineApply(CreateGasolineRecordDto dto);

    /**
     * 加油记录表
     *
     * @param
     * @return
     */
    List<CreateGasolineRecordDto> listGasolineRecords();

}
