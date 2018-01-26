package com.commons.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.List;
import java.util.Map;

/**
 * 调用极光推送
 * <p>
 * auth：Yang
 * 2016年4月16日 上午9:22:23
 */
public class BackPushMessage {

    /**
     * 向所有人推送
     *
     * @param message 推送的消息
     * @return 2015年10月22日 下午7:05:52
     */
    public static boolean pushMessage(String message) {
        JPushClient jpushClient = null;
        PushPayload payload = null;
        boolean b = false;

        try {
            jpushClient = new JPushClient("",
                    "", 3);
            payload = buildPushObject_all_all_alert(message);
            PushResult result = jpushClient.sendPush(payload);
            if (result.isResultOK()) {
                b = true;
            }
        } catch (APIConnectionException e) {
            System.out.println("Connection error. Should retry later. "
                    + e.getMessage());
        } catch (APIRequestException e) {
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
            System.out.println("Msg ID: " + e.getMsgId());
        }
        return b;
    }

    /**
     * 推送消息的方法(多个设备带隐藏参数map)
     *
     * @param alias   设备名称集合
     * @param message 推送内容
     * @param title   推送标题
     * @param extras  额外的参数
     * @return
     */
    public static boolean pushMessage(List<String> alias, String title, String message, Map<String, String> extras) {
        JPushClient jpushClient = null;
        PushPayload payload = null;
        boolean b = false;

        try {
            jpushClient = new JPushClient("",
                    "", 3);
            for (String alia : alias) {
                payload = buildPushObject_android_and_ios_alias(alia, title, message, extras);
                PushResult result = jpushClient.sendPush(payload);
                if (result.isResultOK()) {
                    b = true;
                }
            }
        } catch (APIConnectionException e) {
            System.out.println("Connection error. Should retry later. "
                    + e.getMessage());
        } catch (APIRequestException e) {
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
            System.out.println("Msg ID: " + e.getMsgId());
        }
        return b;
    }

    /**
     * 推送消息的方法(单个设备带隐藏参数map)
     *
     * @param alias   设备名称集合
     * @param message 推送内容
     * @param title   推送标题
     * @param extras  额外的参数
     * @return
     */
    public static boolean pushMessage(String alias, String title, String message, Map<String, String> extras) {
        JPushClient jpushClient = null;
        PushPayload payload = null;
        boolean b = false;

        try {
            jpushClient = new JPushClient("",
                    "", 3);
            payload = buildPushObject_android_and_ios_alias(alias, title, message, extras);
            PushResult result = jpushClient.sendPush(payload);
            if (result.isResultOK()) {
                b = true;
            }
        } catch (APIConnectionException e) {
            System.out.println("Connection error. Should retry later. "
                    + e.getMessage());
        } catch (APIRequestException e) {
            System.out.println("HTTP Status: " + e.getStatus());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
            System.out.println("Msg ID: " + e.getMsgId());
        }
        return b;
    }

    /**
     * 推送消息的方法(单个设备无隐藏参数map)
     *
     * @param alias   设备名称
     * @param message 推送内容
     * @return 是否推送成功
     */
    public static boolean pushMessage(String alias, String message) {
        boolean flag = false;
        JPushClient jpushClient = null;
        PushPayload payload = null;

        try {
            jpushClient = new JPushClient("",
                    "", 3);
            payload = buildPushObject_all_alias_alert(alias, message);
            PushResult result = jpushClient.sendPush(payload);
            if (result.isResultOK()) {
                flag = true;
            }
        } catch (APIConnectionException e) {
            System.out.println("Connection error. Should retry later. "
                    + e.getMessage());
        } catch (APIRequestException e) {
            /*	System.out.println("HTTP Status: " + e.getStatus());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Error Message: " + e.getErrorMessage());
			System.out.println("Msg ID: " + e.getMsgId());*/
        }
        return flag;
    }

    /**
     * 推送消息的方法(多个设备无隐藏参数map)
     *
     * @param alias   设备list名称
     * @param title   推送标题
     * @param message 推送内容
     * @return 是否推送成功
     * @Time 2014-12-23 上午11:03:34
     */
    public static boolean pushMessage(List<String> alias, String title, String message) {
        boolean flag = false;
        JPushClient jpushClient = null;
        PushPayload payload = null;
        PushResult result = null;

        try {
            jpushClient = new JPushClient("",
                    "", 3);
            for (String alia : alias) {
                payload = buildPushObject_all_alias_alert(alia, message);
                result = jpushClient.sendPush(payload);
            }
            if (result.isResultOK()) {
                flag = true;
            }
        } catch (APIConnectionException e) {
            System.out.println("Connection error. Should retry later. "
                    + e.getMessage());
            flag = false;

        } catch (APIRequestException e) {
			/*System.out.println("HTTP Status: " + e.getStatus());
			System.out.println("Error Code: " + e.getErrorCode());
			System.out.println("Error Message: " + e.getErrorMessage());
			System.out.println("Msg ID: " + e.getMsgId());
*/
            flag = false;
        }
        return flag;
    }

    /**
     * 向所有人推送通知
     *
     * @param message
     * @return
     */
    public static PushPayload buildPushObject_all_all_alert(String message) {
        return PushPayload.alertAll(message);
    }

    /**
     * 向指定别名客户发通知
     *
     * @param pushObjectName
     * @param message
     * @return
     */
    public static PushPayload buildPushObject_all_alias_alert(String pushObjectName,
                                                              String message) {
        return PushPayload.newBuilder().setPlatform(Platform.all())
                .setAudience(Audience.alias(pushObjectName)).setNotification(
                        Notification.alert(message)).build();
    }

    /**
     * 向指定标签客户发通知
     *
     * @param pushObjectName
     * @param message
     * @param title
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(
            String pushObjectName, String message, String title) {
        return PushPayload.newBuilder().setPlatform(Platform.android())
                .setAudience(Audience.tag(pushObjectName)).setNotification(
                        Notification.android(message, title, null)).build();
    }

    /**
     * 向指定标签用户发送带标题和内容的通知
     *
     * @param tag     标签名
     * @param message
     * @param title
     * @return
     */
    public static PushPayload buildPushObject_android_and_ios_tag(String tag, String message, String title) {
        return PushPayload.newBuilder().setPlatform(Platform.android_ios())
                .setAudience(Audience.tag(tag)).setNotification(
                        Notification.newBuilder().setAlert(message)
                                .addPlatformNotification(AndroidNotification.newBuilder().setTitle(title).build())
                                .addPlatformNotification(IosNotification.newBuilder().incrBadge(1).addExtra("extra_key", message).build()).build()).build();
    }

    /**
     * 向指定别名用户发送带标题和内容的通知
     * setOptions(Options.newBuilder().setApnsProduction(true).build()) ios生产环境用到必须设定
     *
     * @param alias   别名
     * @param message
     * @param title
     * @return
     */
    public static PushPayload buildPushObject_android_and_ios_alias(String alias, String title, String message, Map<String, String> extra) {
        //setTitle("")  setAlert("")空字符串app不会弹框
        //if (InitPpConstant.JPUSH_IOS_PRODUCT) {
        if (true) {
            return PushPayload.newBuilder()
                    .setPlatform(Platform.android_ios())
                    .setAudience(Audience.alias(alias))
                    .setNotification(Notification.newBuilder().setAlert("").addPlatformNotification(AndroidNotification.newBuilder().setTitle("").addExtras(extra).build()).addPlatformNotification(IosNotification.newBuilder().addExtras(extra).build()).build())
                    .setOptions(Options.newBuilder().setApnsProduction(true).build()) //IOS生产环境必须配置
                    .build();
        } else {
            return PushPayload.newBuilder()
                    .setPlatform(Platform.android_ios())
                    .setAudience(Audience.alias(alias))
                    .setNotification(Notification.newBuilder().setAlert("").addPlatformNotification(AndroidNotification.newBuilder().setTitle("").addExtras(extra).build()).addPlatformNotification(IosNotification.newBuilder().addExtras(extra).build()).build())
                    .build();
        }
    }
}
