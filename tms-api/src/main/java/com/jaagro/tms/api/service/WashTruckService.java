package com.jaagro.tms.api.service;

import com.github.pagehelper.PageInfo;
import com.jaagro.tms.api.dto.peripheral.CreateWashTruckRecordDto;
import com.jaagro.tms.api.dto.peripheral.ListWashTruckRecordCriteria;
import com.jaagro.tms.api.dto.peripheral.WashTruckRecordDto;

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

    /**
     * 洗车记录列表
     * @param criteria
     * @return
     */
    PageInfo listWashTruckRecordByCriteria(ListWashTruckRecordCriteria criteria);

    /**
     * 根据id查询洗车详情
     * @param id
     * @return
     */
    WashTruckRecordDto getById(Integer id);
}
