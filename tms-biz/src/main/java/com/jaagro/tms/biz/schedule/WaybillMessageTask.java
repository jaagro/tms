package com.jaagro.tms.biz.schedule;

import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.api.dto.customer.ShowCustomerDto;
import com.jaagro.tms.api.dto.message.CreateMessageDto;
import com.jaagro.tms.api.service.MessageService;
import com.jaagro.tms.biz.entity.Orders;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.entity.WaybillItems;
import com.jaagro.tms.biz.mapper.OrdersMapperExt;
import com.jaagro.tms.biz.mapper.WaybillItemsMapperExt;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import com.jaagro.tms.biz.mapper.WaybillTrackingMapperExt;
import com.jaagro.tms.biz.service.CustomerClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 运单消息提醒定时
 *
 * @author yj
 * @date 2019/3/28 15:36
 */
@Slf4j
@Service
public class WaybillMessageTask {
    @Autowired
    private WaybillMapperExt waybillMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OrdersMapperExt ordersMapper;
    @Autowired
    private CustomerClientService customerClientService;
    @Autowired
    private WaybillTrackingMapperExt waybillTrackingMapper;
    @Autowired
    private WaybillItemsMapperExt waybillItemsMapper;
    private static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String value = "1";

    /**
     * 司机出发超时提醒司机
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void startInform() {
        log.info("startInform begin");
        long begin = System.currentTimeMillis();
        String uniqueKey = "WaybillStartInformDriver";
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        if (opsForValue.setIfAbsent(uniqueKey, value)) {
            List<Waybill> waybillList = waybillMapper.listToStartInform();
            if (!CollectionUtils.isEmpty(waybillList)) {
                for (Waybill waybill : waybillList) {
                    try {
                        waybillStartInformDriver(waybill);
                    } catch (Exception e) {
                        log.error("InformDriver error waybillId=" + waybill.getId(), e);
                    }
                }
            }
            //stringRedisTemplate.expire(uniqueKey, 9, TimeUnit.MINUTES);
        }
        long end = System.currentTimeMillis();
        log.info("startInform end use time =" + (end - begin));
    }

    /**
     * 司机到达装货地超时提醒调度
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void waybillLoadLateProcess() {
        log.info("waybillLoadLateProcess begin");
        long begin = System.currentTimeMillis();
        String uniqueKey = "waybillLoadLateProcess";
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        if (opsForValue.setIfAbsent(uniqueKey, value)) {
            List<Waybill> waybillList = waybillMapper.listWaybillLoadLate();
            if (!CollectionUtils.isEmpty(waybillList)) {
                for (Waybill waybill : waybillList) {
                    waybillLoadLateInformDispatcher(waybill);
                }
            }
            //stringRedisTemplate.expire(uniqueKey, 9, TimeUnit.MINUTES);
        }
        long end = System.currentTimeMillis();
        log.info("waybillLoadLateProcess end use time =" + (end - begin));
    }

    /**
     * 司机到达卸货地超时提醒调度
     */
    @Scheduled(cron = "0 0/1 * * * ?")
    public void waybillUnloadLateProcess() {
        log.info("waybillUnloadLateProcess begin");
        long begin = System.currentTimeMillis();
        String uniqueKey = "waybillUnloadLateProcess";
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        if (opsForValue.setIfAbsent(uniqueKey, value)) {
            List<Waybill> waybillList = waybillMapper.listWaybillUnLoadLate();
            if (!CollectionUtils.isEmpty(waybillList)) {
                for (Waybill waybill : waybillList) {
                    waybillUnLoadLateInformDispatcher(waybill);
                }
            }
            //stringRedisTemplate.expire(uniqueKey, 9, TimeUnit.MINUTES);
        }
        long end = System.currentTimeMillis();
        log.info("waybillUnloadLateProcess end use time =" + (end - begin));
    }

    private void waybillUnLoadLateInformDispatcher(Waybill waybill) {
        String uniqueKey = "waybillUnLoadLateInformDispatcher" + waybill.getId();
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        if (opsForValue.setIfAbsent(uniqueKey, value)) {
            Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
            if (orders == null) {
                log.info("waybillUnLoadLateInformDispatcher orders is null orderId=" + waybill.getOrderId());
                return;
            }
            ShowCustomerDto customerDto = customerClientService.getShowCustomerById(orders.getCustomerId());
            if (customerDto == null) {
                log.info("waybillUnLoadLateInformDispatcher customer not found customerId=" + orders.getCustomerId());
                return;
            }
            Date requiredTime = null;
            List<WaybillItems> waybillItemsList = waybillItemsMapper.listWaybillItemsByWaybillId(waybill.getId());
            if (!CollectionUtils.isEmpty(waybillItemsList)){
                requiredTime = waybillItemsList.get(0).getRequiredTime();
            }
            CreateMessageDto messageDto = new CreateMessageDto();
            String body = "客户"+customerDto.getCustomerName()+"的运单，"+waybill.getId()+"到达卸货地已超时，要求时间为"+UDateToLocalDateTime(requiredTime).format(format)+"。";
            messageDto.setBody(body)
                    .setToUserType(ToUserType.EMPLOYEE)
                    .setToUserId(waybillTrackingMapper.getCreateUserByWaybillId(waybill.getId()))
                    .setReferId(waybill.getId())
                    .setMsgType(MsgType.WAYBILL)
                    .setMsgSource(MsgSource.WEB)
                    .setHeader(WaybillConstant.WAYBILL_ARRIVE_UNLOAD_SITE_LATE)
                    .setFromUserType(FromUserType.SYSTEM)
                    .setFromUserId(0)
                    .setCreateUserId(1)
                    .setCategory(MsgCategory.WARNING);
            messageService.createMessage(messageDto);
            stringRedisTemplate.expire(uniqueKey,7,TimeUnit.DAYS);
        }

    }

    private void waybillLoadLateInformDispatcher(Waybill waybill) {
        String uniqueKey = "waybillLoadLateInformDispatcher" + waybill.getId();
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        if (opsForValue.setIfAbsent(uniqueKey, value)) {
            Orders orders = ordersMapper.selectByPrimaryKey(waybill.getOrderId());
            if (orders == null) {
                log.info("waybillLoadLateInformDispatcher orders is null orderId=" + waybill.getOrderId());
                return;
            }
            ShowCustomerDto customerDto = customerClientService.getShowCustomerById(orders.getCustomerId());
            if (customerDto == null) {
                log.info("waybillLoadLateInformDispatcher customer not found customerId=" + orders.getCustomerId());
                return;
            }
            CreateMessageDto messageDto = new CreateMessageDto();
            String body = "客户" + customerDto.getCustomerName() + "的运单，" + waybill.getId() + "到达装货地已超时，要求时间为" + UDateToLocalDateTime(waybill.getLoadTime()).format(format) + "。";
            messageDto.setBody(body)
                    .setToUserType(ToUserType.EMPLOYEE)
                    .setToUserId(waybillTrackingMapper.getCreateUserByWaybillId(waybill.getId()))
                    .setReferId(waybill.getId())
                    .setMsgType(MsgType.WAYBILL)
                    .setMsgSource(MsgSource.WEB)
                    .setHeader(WaybillConstant.WAYBILL_ARRIVE_LOAD_SITE_LATE)
                    .setFromUserType(FromUserType.SYSTEM)
                    .setFromUserId(0)
                    .setCreateUserId(1)
                    .setCategory(MsgCategory.WARNING);
            messageService.createMessage(messageDto);
            stringRedisTemplate.expire(uniqueKey,7,TimeUnit.DAYS);
        }
    }

    private void waybillStartInformDriver(Waybill waybill) {
        Integer driverId = waybill.getDriverId();
        if (driverId != null) {
            String uniqueKey = "WaybillStartInformDriver" + waybill.getId();
            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
            if (opsForValue.setIfAbsent(uniqueKey, value)) {
                CreateMessageDto messageDto = new CreateMessageDto();
                String body = "运单号为" + waybill.getId() + "，要求" + UDateToLocalDateTime(waybill.getLoadTime()).format(format) + "提货，请尽快出发。";
                messageDto.setBody(body)
                        .setToUserType(ToUserType.DRIVER)
                        .setToUserId(driverId)
                        .setReferId(waybill.getId())
                        .setMsgType(MsgType.WAYBILL)
                        .setMsgSource(MsgSource.APP)
                        .setHeader(WaybillConstant.WAYBILL_TO_BE_START)
                        .setFromUserType(FromUserType.SYSTEM)
                        .setFromUserId(0)
                        .setCreateUserId(1)
                        .setCategory(MsgCategory.WARNING);
                messageService.createMessage(messageDto);
                stringRedisTemplate.expire(uniqueKey,7,TimeUnit.DAYS);
            }
        }
    }

    public LocalDateTime UDateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }
}
