package com.yoga.jiguang.push;

import com.yoga.core.utils.StringUtil;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.push.service.PushActor;
import com.yoga.utility.push.service.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JiGuangService implements PushActor {
    public final static String Key_Setting = "jiguang.setting";

    @Autowired
    private SettingService settingService;
    private final static JiGuangSender nullSender = new JiGuangSender(null);
    private Map<Long, JiGuangSender> getUiSenders = new HashMap<>();

    public JiGuangSetting getSetting(long tenantId) {
        return settingService.get(tenantId, PushService.ModuleName, Key_Setting, JiGuangSetting.class);
    }
    public void setSetting(long tenantId, JiGuangSetting value) {
        settingService.save(tenantId, PushService.ModuleName, Key_Setting, value, value.getAppKey());
        synchronized (nullSender) {
            getUiSenders.remove(tenantId);
        }
    }

    private JiGuangSender getSender(long tenantId) {
        JiGuangSender sender = getUiSenders.get(tenantId);
        if (sender == null) {
            synchronized (nullSender) {
                sender = getUiSenders.get(tenantId);
                if (sender == null) {
                    JiGuangSetting setting = getSetting(tenantId);
                    if (setting == null || StringUtil.isBlank(setting.getAppKey())) sender = nullSender;
                    else sender = new JiGuangSender(setting);
                    getUiSenders.put(tenantId, sender);
                }
            }
        }
        if (sender == nullSender) return null;
        return sender;
    }

    @Override
    public String getCode() {
        return "jiguang";
    }
    @Override
    public int getPriority() {
        return -999;
    }
    @Override
    public boolean supportDeviceModel(long tenantId, String deviceModel) {
        JiGuangSender sender = getSender(tenantId);
        if (sender == null) return false;
        return true;
    }

    @Override
    public boolean pushToSingle(long tenantId, String clientId, String title, String content, String url, Map<String, String> extras) {
        JiGuangSender sender = getSender(tenantId);
        if (sender == null) return false;
        return sender.pushToSingle(clientId, title, content, url, extras);
    }
    @Override
    public boolean pushToMultiple(long tenantId, List<String> clientIds, String title, String content, String url, Map<String, String> extras) {
        JiGuangSender sender = getSender(tenantId);
        if (sender == null) return false;
        return sender.pushToMultiple(clientIds, title, content, url, extras);
    }
    @Override
    public boolean pushToBroadcast(long tenantId, List<String> tags, String title, String content, String url, Map<String, String> extras) {
        JiGuangSender sender = getSender(tenantId);
        if (sender == null) return false;
        return sender.pushToBroadcast(tags, title, content, url, extras);
    }
}
