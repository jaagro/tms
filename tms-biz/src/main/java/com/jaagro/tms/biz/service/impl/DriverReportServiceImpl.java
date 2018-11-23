package com.jaagro.tms.biz.service.impl;

import com.jaagro.tms.api.service.DriverReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 司机报表
 * @author yj
 * @since 2018/11/22
 */
@Service
@Slf4j
public class DriverReportServiceImpl implements DriverReportService {
    /**
     * 生成报表
     *
     * @param beginTime
     * @param endTime
     */
    @Override
    public void createReport(Date beginTime, Date endTime) {

    }
}
