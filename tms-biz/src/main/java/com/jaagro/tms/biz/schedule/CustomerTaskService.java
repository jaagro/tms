package com.jaagro.tms.biz.schedule;

import com.jaagro.tms.api.service.CustomerReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author baiyiran
 * @Date 2018/11/21
 */
@Slf4j
@Service
public class CustomerTaskService {

    @Autowired
    private CustomerReportService customerReportService;

    /**
     * 每晚22点计算客户报表
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void customerReportAccount() {
        log.info("\n计算客户报表开始");
        try {
            customerReportService.customerReportAccount();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("\n定时任务CustomerTaskService执行异常:", ex);
        }
    }
}
