package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.dto.peripheral.CreateWashTruckRecordDto;
import com.jaagro.tms.api.service.WashTruckService;
import com.jaagro.tms.biz.mapper.WashTruckImageMapperExt;
import com.jaagro.tms.biz.mapper.WashTruckRecordMapperExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 洗车服务
 * @author yj
 * @since 2018/12/10
 */
@Service
@Slf4j
public class WashTruckServiceImpl implements WashTruckService {
    @Autowired
    private WashTruckRecordMapperExt washTruckRecordMapperExt;
    @Autowired
    private WashTruckImageMapperExt washTruckImageMapperExt;
    /**
     * 创建洗车记录
     * @param createWashTruckRecordDto
     */
    @Override
    public void createWashTruckRecord(CreateWashTruckRecordDto createWashTruckRecordDto) {

    }
}
