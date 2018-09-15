package com.jaagro.tms.jpush;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

public class JPushTestMine {
    private static final Log log = LogFactory.getLog(JPushTestMine.class);

    private static final String APP_KEY = "97e71dd3af7b165822a52658";
    private static final String MASTER_SECRET = "4e6cc81b13f8f12754282c7b";
    public static final String REGISTRATION_ID3 = "18071adc030dcba91c0";

    /**
     * 消息推送类型：专家消息
     */
    public static final int JPUSH_TYPE_ALERT = 1;
    /**
     * 消息推送类型：投诉消息
     */
    public static final int JPUSH_TYPE_INFO = 2;
    /**
     * 消息推送类型：通知公告
     */
    public static final int JPUSH_TYPE_NOTICE = 3;

    /**
     * 推送服务
     *
     * @param appId 专家ID【别名（alias）】
     * @param alertid 下发 id
     * @param alert 标题（问题类型，问题描述）
     */
    public static void sendPush(String appId, String alertid, String alert) {
        Map<String, String> extra = new HashMap<String, String>();
        extra.put("alertid", alertid);

        String title = "专家消息";
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        // maxRetryTime:最大的尝试次数，设为3表示：跟服务器进行建立连接若失败会尝试再进行两次尝试
//		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.android())
                .setAudience(Audience.alias(appId))
                .setAudience(Audience.registrationId(REGISTRATION_ID3))
                .setNotification(Notification.android(alert, title, extra))
                .build();

        try {
            PushResult result = jpushClient.sendPush(payload);
        } catch (Exception e) {
            log.error("Connection error. Should retry later. ", e);

            // } catch (Exception e) {
            // log.error("Error response from JPush server. Should review and
            // fix it. ", e);
            // log.info("HTTP Status: " + e.getStatus());
            // log.info("Error Code: " + e.getErrorCode());
            // log.info("Error Message: " + e.getErrorMessage());
            // log.info("policeId: " + policeId);
            // log.info("alertid: " + alertid);
            // log.info("alert: " + alert);
            // log.info("title: " + title);
        }
    }

    public static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.newBuilder().setPlatform(Platform.android())// 设置接受的平台
                .setAudience(Audience.all())// Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.android("ssssss第一条测试消息", null, null)).build();
    }

    /**
     * 确定推送消息的目标，包括推送的平台（Android、IOS）、消息内容和目标（所有人、别名、标签），
     * 构建简单的推送对象：向所有平台，所有人，推送内容为 content 的通知。
     *
     * @param content
     * @return
     */
    public static PushPayload buildPushObject_all_all_alert(String content) {
        return PushPayload.alertAll(content);
    }

    /**
     * 构建推送对象：所有平台，推送目标是别名为alias，通知内容为 content。
     *
     * @param alias
     * @param content
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String alias, String content) {
        return PushPayload.newBuilder().setPlatform(Platform.all())// 所有平台:Platform.all();Platform.android();Platform.android_ios()
                .setAudience(Audience.alias(alias))// 向选定的人推送(alias也可以是一个集合Set<String> alias)
                .setNotification(Notification.alert(content))// 消息内容
                .build();
    }

    /**
     * 构建推送对象：向android平台，向目标标签tag，通知标题title，内容为 content。
     *
     * @param alias
     * @param title
     * @param content
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String alias, String title, String content) {
        return PushPayload.newBuilder().setPlatform(Platform.android()).setAudience(Audience.tag("tag"))// Audience.tag_and("tag1", "tag_all")向指定的组推送
                .setNotification(Notification.android("alert", title, null)).build();
    }

    /**
     * Android和IOS
     * @return
     */
    public static PushPayload buildPushObject_android_and_ios() {
        Map<String, String> extras = new HashMap<String, String>();
        extras.put("test", "https://community.jiguang.cn/push");
        return PushPayload.newBuilder().setPlatform(Platform.android_ios())
                .setAudience(Audience.all())
                .setNotification(Notification.newBuilder().setAlert("alert content")
                        .addPlatformNotification(AndroidNotification.newBuilder().setTitle("Android Title").addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtra("extra_key", "extra_value").build())
                        .build())
                .build();
    }

    public static void main(String[] arg) {
        JPushTestMine.sendPush("18071adc030dcba91c0", "12345", "信息555");
    }

}
