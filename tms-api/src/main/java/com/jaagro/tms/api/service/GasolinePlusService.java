package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.peripheral.CreateGasolineRecordDto;
import com.jaagro.tms.api.dto.peripheral.GasolineRecordParam;

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
    PageInfo<CreateGasolineRecordDto> listGasolineRecords(GasolineRecordParam param);

    /**
     * 加油记录详情
     *
     * @param gasolineId
     * @return
     */
    List<CreateGasolineRecordDto> gasolineDetails(Integer gasolineId);

}
