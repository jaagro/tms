package com.jaagro.tms.api.service;

import com.jaagro.tms.api.dto.peripheral.CreateWashTruckRecordDto;

/**
 * 洗车服务
 * @author yj
 * @since 2018/12/10
 */
public interface WashTruckService {
    /**
     * 创建洗车记录
     * @param createWashTruckRecordDto
     */
    void createWashTruckRecord (CreateWashTruckRecordDto createWashTruckRecordDto);
}
