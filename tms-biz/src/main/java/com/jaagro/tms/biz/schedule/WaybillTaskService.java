package com.jaagro.tms.biz.schedule;


import com.jaagro.tms.api.constant.*;
import com.jaagro.tms.biz.common.RedisOperator;
import com.jaagro.tms.biz.entity.Message;
import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.entity.WaybillTracking;
import com.jaagro.tms.biz.mapper.MessageMapperExt;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import com.jaagro.tms.biz.mapper.WaybillTrackingMapperExt;
import com.jaagro.tms.biz.utils.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    @Autowired
    private WaybillTrackingMapperExt waybillTrackingMapper;
    @Autowired
    private MessageMapperExt messageMapper;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private RedisOperator redisOperator;
    /**
     * 超过30分钟未接的运单修改为被司机拒绝以便重新派单,10分钟跑一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void waybillDefaultRejectBySystem() {
        try {
            String format = "HH:mm:ss";
            Calendar c = Calendar.getInstance();
            String strNowTime = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
            Date nowTime = new SimpleDateFormat(format).parse(strNowTime);
            Date startTime = new SimpleDateFormat(format).parse("06:00:00");
            Date endTime = new SimpleDateFormat(format).parse("09:00:00");

            if (!isEffectiveDate(nowTime, startTime, endTime)) {
                //加锁
                long time = System.currentTimeMillis() + 10*1000;
                boolean success = redisLock.lock("Scheduled:redisLock:waybillDefaultRejectBySystem" , String.valueOf(time));
                if (!success) {
                    throw new RuntimeException("请求正在处理中");
                }

                List<Waybill> waybillList = waybillMapperExt.listOverTimeWaybills();

                if (!CollectionUtils.isEmpty(waybillList)) {
                    waybillMapperExt.batchUpdateWaybillStatus(waybillList);

                    /******添加拒单追踪******/
                    for (Waybill waybill : waybillList) {
                        WaybillTracking waybillTracking = new WaybillTracking();
                        waybillTracking
                                .setEnabled(true)
                                .setWaybillId(waybill.getId())
                                .setTrackingInfo("System auto Reject!")
                                .setTrackingType(TrackingType.WAYBILL_SYSTEM_REJECT)
                                .setNewStatus(WaybillStatus.REJECT)
                                .setOldStatus(WaybillStatus.RECEIVE)
                                .setReferUserId(1)
                                .setCreateTime(new Date());
                        waybillTrackingMapper.insertSelective(waybillTracking);

                        /**-------添加自动拒单消息---**/
                        Message appMessage = new Message();
                        appMessage.setReferId(waybill.getId());
                        // 消息类型：1-系统通知 2-运单相关 3-账务相关
                        appMessage.setMsgType(MsgType.WAYBILL);
                        //消息来源:1-APP,2-小程序,3-站内
                        appMessage.setMsgSource(MsgSource.WEB);
                        appMessage.setMsgStatus(MsgStatusConstant.UNREAD);
                        appMessage.setHeader(WaybillConstant.NEW__WAYBILL_FOR_RECEIVE);
                        appMessage.setBody("运单号为（" + waybill.getId() + "）的运单，系统已超时拒单，请及时处理！");
                        appMessage.setCreateTime(new Date());
                        appMessage.setCreateUserId(1);
                        appMessage.setFromUserId(FromUserType.SYSTEM);
                        appMessage.setToUserId(waybillTrackingMapper.getCreateUserByWaybillId(waybill.getId()));
                        appMessage.setHeader("你有一个拒单待处理");
                        appMessage.setFromUserType(FromUserType.SYSTEM);
                        appMessage.setToUserType(ToUserType.EMPLOYEE);
                        appMessage.setCategory(MsgCategory.WARNING);
                        appMessage.setRefuseType(RefuseType.AUTO);
                        messageMapper.insertSelective(appMessage);
                        /**------------------**/
                    }
                    /**********************/
                }
                redisOperator.del("Scheduled:redisLock:waybillDefaultRejectBySystem");
                log.info("定时钟执行结束");
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            log.error("定时钟waybillDefaultRejectBySystem执行异常:", ex);
        }
    }


    public static void main(String[] args) {

        try {
            String format = "HH:mm:ss";
            Calendar c = Calendar.getInstance();
            String strNowTime = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
            System.out.println(strNowTime);
            Date nowTime = new SimpleDateFormat(format).parse(strNowTime);
            nowTime = new SimpleDateFormat(format).parse("09:00:01");
            Date startTime = new SimpleDateFormat(format).parse("6:00:00");
            Date endTime = new SimpleDateFormat(format).parse("09:00:00");
            System.out.println(nowTime);
            System.out.println(startTime);
            System.out.println(endTime);
            System.out.println(isEffectiveDate(nowTime, startTime, endTime));

            System.out.println(DateUtils.addDays(new Date(), -1));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author gavin
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 比较2个时间大小
     *
     * @param DATE1
     * @param DATE2
     * @return 返回：0（两个时间相等），1（DATE1在DATE2之前），-1（DATE1在DATE2之后）
     */
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
}