package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.anomaly.WaybillAbnomalTypeDto;
import com.jaagro.tms.api.dto.anomaly.WaybillAnomalyReportDto;

import java.util.List;

/**
 * @author @Gao.
 */
public interface WaybillAnomalyService {

    /**
     * 司机运单异常申报
     * Author @Gao.
     */
    void waybillAnomalyReport(WaybillAnomalyReportDto dto);

    /**
     * 运单异常类型显示
     * Author @Gao.
     */
    List<WaybillAbnomalTypeDto> displayAbnormalType();


}
