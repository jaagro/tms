package com.jaagro.tms.biz.schedule;


import com.jaagro.tms.api.constant.WaybillStatus;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author: Gavin
 */
@Service
@Slf4j
public class WaybillTaskService {

    @Autowired
    private WaybillMapperExt waybillMapperExt;

    /**
     * 超过30分钟未接的运单修改为被司机拒绝以便重新派单,10分钟跑一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void waybillDefaultRejectBySystem() {

        try {

            List<Waybill> waybillList = waybillMapperExt.listOverTimeWaybills();

            if (!CollectionUtils.isEmpty(waybillList)) {
                log.info("超过半小时未接单的运单数={}", waybillList.size());
                for (Waybill waybill : waybillList) {
                    waybill.setWaybillStatus(WaybillStatus.REJECT);
                    waybill.setModifyTime(new Date());
                }
                waybillMapperExt.batchUpdateWaybillStatus(waybillList);
            }
        } catch (Exception ex) {

            log.error("定时钟waybillDefaultRejectBySystem执行异常:", ex);

        }
        log.info("定时钟执行结束");
    }
}
