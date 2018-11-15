package com.jaagro.tms.biz.jpush;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Push工具类
 * @author gavin
 */
public abstract class JpushClientUtil {

    protected static final Logger log = LoggerFactory.getLogger(JpushClientUtil.class);

    protected static final String APP_KEY = "513fdd806a3572de197fc30e";
    protected static final String MASTER_SECRET = "424e702fb7f2f98c350c97e8";
    private static JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY);

    /**
     * @Author: gavin
     * @param alias
     * @param title
     * @param alertContent
     * @param regID
     * @param extraParam
     */

    public static void sendPush(String alias,String title, String alertContent,String regID,Map<String,String> extraParam) {
        Map<String, String> extra = new HashMap<String, String>();
        // maxRetryTime:最大的尝试次数，设为3表示：跟服务器进行建立连接若失败会尝试再进行两次尝试
        // jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, 3);
        PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.android())
                .setAudience(Audience.alias(alias))
                .setAudience(Audience.registrationId(regID))
                .setPlatform(Platform.all())
                .setNotification(Notification.android(alertContent, title, extraParam))
                .build();

        try {
            PushResult pushResult = jPushClient.sendPush(payload);
            System.out.println(pushResult);
        } catch (Exception e) {
            System.out.println("JPUSH Failed root cause:"+e.getMessage());
            log.error("Jpush Connection error. Should retry later. ", e.getMessage());
        }
    }

    public static int sendToRegistrationId(String regID, String notification_title, String msg_title,String msg_content, String extrasparam) {
        int result = 0;
        try {
            PushPayload pushPayload = JpushClientUtil.buildPushPayload(regID,notification_title, msg_title, msg_content, extrasparam);
            System.out.println(pushPayload);
            PushResult pushResult = jPushClient.sendPush(pushPayload);
            System.out.println(pushResult);
            if (pushResult.getResponseCode() == 200) {
                result = 1;
            }
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static PushPayload buildPushPayload(String registrationId,String notification_title, String msg_title, String msg_content, String extrasparam) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                // 指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或registration_id
                .setAudience(Audience.registrationId(registrationId))
                // jpush的通知，android的由jpush直接下发，iOS的由apns服务器下发，Winphone的由mpns下发
                .setNotification(
                        Notification.newBuilder()
                        // 指定当前推送的android通知
                        .addPlatformNotification(
                                AndroidNotification.newBuilder().setAlert(notification_title)
                                .setTitle(notification_title)
                                // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                                .addExtra("sss", extrasparam).build()
                        )
                       // 指定当前推送的iOS通知
                       .addPlatformNotification(
                               IosNotification.newBuilder()
                               // 传一个IosAlert对象，指定apns title、title、subtitle等
                                .setAlert(notification_title)
                               // 此项是指定此推送的badge自动加1
                               .incrBadge(1)
                               // 此字段的值default表示系统默认声音；传sound.caf表示此推送以项目里面打包的sound.caf声音来提醒，
                                // 如果系统没有此音频则以系统默认声音提醒；此字段如果传空字符串，iOS9及以上的系统是无声音提醒，以下的系统是默认声音
                               .setSound("sound.caf")
                              // 此字段为透传字段，不会显示在通知栏。用户可以通过此字段来做一些定制需求，如特定的key传要指定跳转的页面（value）
                              .addExtra("content", extrasparam).build()
                        )
             // 此项说明此推送是一个background推送，想了解background看：http://docs.jpush.io/client/ios_tutorials/#ios-7-background-remote-notification
             // 取消此注释，消息推送时ios将无法在锁屏情况接收
             // .setContentAvailable(true)
              .build())
              .setMessage(Message.newBuilder()
                          .setMsgContent(msg_content)
                          .setTitle(msg_title)
                          .addExtra("message extras key", extrasparam)
                          .build()
                         )
              .setOptions(
                       Options.newBuilder()
                       // 此字段的值是用来指定本推送要推送的apns环境，false表示开发，true表示生产；对android和自定义消息无意义
                       .setApnsProduction(false)
                        // 此字段是给开发者自己给推送编号，方便推送者分辨推送记录
                        .setSendno(1)
                        // 此字段的值是用来指定本推送的离线保存时长，如果不传此字段则默认保存一天，最多指定保留十天；
                        .setTimeToLive(86400)
                        .build()
                    )
                .build();
    }
}
