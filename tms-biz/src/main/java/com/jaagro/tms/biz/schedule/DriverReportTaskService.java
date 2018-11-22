package com.jaagro.tms.biz.schedule;

import com.jaagro.tms.api.service.DriverReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 司机报表定时
 * @author yj
 * @since 2018/11/22
 */
@Service
@Slf4j
public class DriverReportTaskService {
    @Autowired
    private DriverReportService driverReportService;
    /**
     * 每天零晨1点执行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void createDriverReport() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date beginTime = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
        Date endTime = calendar.getTime();
        log.info("DriverReportTaskService.createDriverReport begin beginTime={},endTime={}",beginTime,endTime);
        long begin = System.currentTimeMillis();
        driverReportService.createReport(beginTime,endTime);
        log.info("DriverReportTaskService.createDriverReport end use {} millionSeconds",System.currentTimeMillis()-begin);
    }


}
