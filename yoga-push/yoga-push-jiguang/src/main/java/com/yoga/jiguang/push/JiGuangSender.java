package com.yoga.jiguang.push;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
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

public class JiGuangSender {
    private JiGuangSetting setting;
    private JPushClient pushClient;

    private final static long timeToLive = 86400L;
    JiGuangSender(JiGuangSetting setting) {
        if (setting != null) {
            this.setting = setting;
            pushClient = new JPushClient(setting.getMasterSecret(), setting.getAppKey(), null, ClientConfig.getInstance());
        }
    }

    public boolean pushToSingle(String clientId, String title, String content, String url, Map<String, String> extras) {
        PushPayload payload = PushPayload
                .newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(clientId))
                .setNotification(
                        Notification.newBuilder()
                                .setAlert(title)
                                .addPlatformNotification(AndroidNotification.newBuilder()
                                        .setTitle(title)
                                        .setAlert(content)
                                        .addExtras(extras)
                                        .build())
                                .addPlatformNotification(IosNotification.newBuilder()
                                        .addExtras(extras)
                                        .setAlert(content)
                                        .setBadge(0)
                                        .build())
                                .build())
                .setOptions(
                        Options.newBuilder()
                                .setApnsProduction(setting.isProduct())
                                .setTimeToLive(timeToLive)
                                .build())
                .build();
        try {
            pushClient.sendPush(payload);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean pushToMultiple(List<String> clientIds, String title, String content, String url, Map<String, String> extras) {
        clientIds.forEach(clientId-> {
            pushToSingle(clientId, title, content, url, extras);
        });
        return true;
    }

    public boolean pushToBroadcast(List<String> tags, String title, String content, String url, Map<String, String> extras) {
        PushPayload payload = PushPayload
                .newBuilder()
                .setPlatform(Platform.all())
                .setAudience(tags != null && tags.size() > 0 ? Audience.alias(tags) : Audience.all())
                .setNotification(
                        Notification.newBuilder()
                                .setAlert(title)
                                .addPlatformNotification(AndroidNotification.newBuilder()
                                        .addExtras(extras)
                                        .setTitle(title)
                                        .setAlert(content)
                                        .build())
                                .addPlatformNotification(IosNotification.newBuilder()
                                        .addExtras(extras)
                                        .setAlert(content)
                                        .setBadge(0)
                                        .build())
                                .build())
                .setOptions(
                        Options.newBuilder()
                                .setApnsProduction(setting.isProduct())
                                .setTimeToLive(timeToLive)
                                .build())
                .build();
        try {
            PushResult result = pushClient.sendPush(payload);
            return result != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
