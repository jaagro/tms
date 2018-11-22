package com.jaagro.tms.biz.schedule;

import com.jaagro.tms.api.service.OrderService;
import com.jaagro.tms.api.service.WaybillService;
import com.jaagro.tms.biz.service.CustomerClientService;
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
public class CustomerReportService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private WaybillService waybillService;
    @Autowired
    private CustomerClientService customerClientService;

    /**
     * 每晚22点计算客户报表
     */
    @Scheduled(cron = "0 0 22 * * ?")
    public void customerReportAccount() {

    }
}
