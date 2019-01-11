package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.waybill.GrabWaybillParamDto;

import java.util.List;

/**
 * 派单到多个车辆 抢单服务
 *
 * @author @Gao.
 */
public interface GrabWaybillService {

    /**
     * 进行抢单模式派单
     *
     * @param dto
     * @author @Gao.
     */
    void grabWaybillToTrucks(GrabWaybillParamDto dto);
}
