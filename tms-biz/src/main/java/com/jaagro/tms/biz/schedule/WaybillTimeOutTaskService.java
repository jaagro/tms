package com.jaagro.tms.biz.schedule;

import com.jaagro.tms.api.constant.WaybillStatus;
import com.jaagro.tms.api.dto.truck.DriverReturnDto;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.jpush.JpushClientUtil;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import com.jaagro.tms.biz.service.DriverClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WaybillTimeOutTaskService {

    @Autowired
    private WaybillMapperExt waybillMapperExt;
    @Autowired
    private DriverClientService driverClientService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 运单接单超时 进行短信提醒
     *
     * @Author: @Gao.
     */

   //@Scheduled(cron = "0 0/5 * * * ?")
    public void listWaybillTimeOut() {
        log.info("start**************");
        List<Waybill> waybills = waybillMapperExt.listWaybillTimeOut(WaybillStatus.RECEIVE);
        for (Waybill waybill : waybills) {
            if (waybill.getTruckId() != null) {
                String wb = redisTemplate.opsForValue().get("TIMEOUT" + waybill.getId());
                if (StringUtils.isEmpty(wb)) {
                    List<DriverReturnDto> driverReturnDtos = driverClientService.listByTruckId(waybill.getTruckId());
                    setRefIdToRedis("TIMEOUT" + waybill.getId(), waybill.getId().toString());
                    for (DriverReturnDto driver : driverReturnDtos) {
                        if (driver != null) {
                            sendMessage(driver);
                        }
                    }
                }
            }
        }
        log.info("end**************");
    }

    /**
     * 发送jPush短信
     */
    private void sendMessage(DriverReturnDto driver) {
        String alias = "";
        String msgTitle = "运单接单超时提醒消息";
        String msgContent;
        String regId;
        Map<String, String> extraParam = new HashMap<>();
        extraParam.put("driverId", driver.getId().toString());
        extraParam.put("needVoice", "y");
        regId = driver.getRegistrationId() == null ? null : driver.getRegistrationId();
        msgContent = driver.getName() + "师傅，您有一个运单已超时，请尽快接单！";
        if (null != driver.getRegistrationId()) {
            JpushClientUtil.sendPush(alias, msgTitle, msgContent, regId, extraParam);
        }
    }

    /**
     * 将wyabillId 存入到redis中
     *
     * @param key
     * @param value
     */
    private void setRefIdToRedis(String key, String value) {
        redisTemplate.opsForValue().set(key, value, 1, TimeUnit.DAYS);
    }


}
