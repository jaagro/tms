package com.jaagro.tms.biz.schedule;


import com.jaagro.tms.biz.entity.Waybill;
import com.jaagro.tms.biz.mapper.WaybillMapperExt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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

    /**
     * 超过30分钟未接的运单修改为被司机拒绝以便重新派单,10分钟跑一次
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void waybillDefaultRejectBySystem() {
        try {
            String format = "HH:mm:ss";
            Calendar c = Calendar.getInstance();
            String strNowTime = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
            Date nowTime = new SimpleDateFormat(format).parse(strNowTime);
            Date startTime = new SimpleDateFormat(format).parse("06:00:00");
            Date endTime = new SimpleDateFormat(format).parse("09:00:00");

            if (!isEffectiveDate(nowTime, startTime, endTime)) {

                List<Waybill> waybillList = waybillMapperExt.listOverTimeWaybills();

                if (!CollectionUtils.isEmpty(waybillList)) {
                    waybillMapperExt.batchUpdateWaybillStatus(waybillList);
                }

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