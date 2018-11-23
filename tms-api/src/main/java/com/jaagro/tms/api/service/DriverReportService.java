package com.jaagro.tms.api.service;

import java.util.Date; /**
 * 司机报表
 * @author yj
 * @since 2018/11/22
 */
public interface DriverReportService {
    /**
     * 生成报表
     * @param beginTime
     * @param endTime
     */
    void createReport(Date beginTime, Date endTime);
}
