package com.yoga.weixinapp.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.quartz.QuartzService;
import com.yoga.utility.quartz.QuartzTask;
import com.yoga.weixinapp.ao.SettingConfig;
import com.yoga.weixinapp.ao.WxmpDataItem;
import com.yoga.weixinapp.mapper.WxmpSendMessageMapper;
import com.yoga.weixinapp.model.WxmpBindUser;
import com.yoga.weixinapp.model.WxmpSendMessage;
import com.yoga.weixinapp.wxapi.WxApiFactory;
import com.yoga.weixinapp.wxapi.WxBaseResult;
import com.yoga.weixinapp.wxapi.WxSendSubscribeRequest;
import com.yoga.weixinapp.wxapi.WxTokenResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class WxmpService extends BaseService {

    @Value("${app.system.wxmp.app-id:}")
    private String appId;
    @Value("${app.system.wxmp.app-secret:}")
    private String appSecret;

    @Lazy
    @Autowired
    private WxmpUserService userService;
    @Autowired
    private SettingService settingService;
    @Autowired
    private WxmpSendMessageMapper messageMapper;
    @Autowired
    private WxApiFactory wxApiFactory;
    @Autowired
    private QuartzService quartzService;

    public final static String ModuleName = "gcf_weixinapp";
    public final static String Key_Setting = "weixinapp.setting";
    public final static String Key_AppState = "weixinapp.state";
    public SettingConfig getSetting(long tenantId) {
        if (StringUtil.isNotBlank(appId) && StringUtil.isNotBlank(appSecret)) {
            return new SettingConfig(appId, appSecret);
        } else {
            return settingService.get(tenantId, ModuleName, Key_Setting, SettingConfig.class);
        }
    }
    public void saveSetting(long tenantId, SettingConfig config) {
        settingService.save(tenantId, ModuleName, Key_Setting, JSONObject.toJSONString(config), config.getAppId());
    }
    public String getAppState(long tenantId) {
        return settingService.get(tenantId, ModuleName, Key_AppState, "formal");
    }

    private final static String AccessTokenKey = "wxmp.accessToken";
    public String getToken(long tenantId, boolean forceRefresh) {
        String key = AccessTokenKey + "." + tenantId;
        if (!forceRefresh) {
            String token = redisOperator.get(key);
            if (token != null) return token;
        }
        SettingConfig config = getSetting(tenantId);
        if (config == null || StringUtil.isBlank(config.getAppId())) throw new BusinessException("尚未配置小程序开发ID！");
        return runInLock("Lock." + key, ()-> {
            try {
                WxTokenResult result = wxApiFactory.getWxApi().getAccessToken(config.getAppId(), config.getAppSecret(), "client_credential").execute().body();
                if (result == null) throw new BusinessException("请求微信会话失败！");
                if (result.getErrcode() != 0) throw new BusinessException(result.getErrMsg());
                String token = result.getAccessToken();
                redisOperator.set(key, token, result.getExpiresIn() / 2);
                return token;
            } catch (Exception ex) {
                throw new BusinessException(ex.getLocalizedMessage());
            }
        });
    }
    public String getToken(String appId, String appSecret, boolean forceRefresh) {
        String key = AccessTokenKey + "." + appId;
        if (!forceRefresh) {
            String token = redisOperator.get(key);
            if (token != null) return token;
        }
        if (StringUtil.isBlank(appId)) throw new BusinessException("尚未配置小程序开发ID！");
        return runInLock("Lock." + key, ()-> {
            try {
                WxTokenResult result = wxApiFactory.getWxApi().getAccessToken(appId, appSecret, "client_credential").execute().body();
                if (result == null) throw new BusinessException("请求微信会话失败！");
                if (result.getErrcode() != 0) throw new BusinessException(result.getErrMsg());
                String token = result.getAccessToken();
                redisOperator.set(key, token, result.getExpiresIn() / 2);
                return token;
            } catch (Exception ex) {
                throw new BusinessException(ex.getLocalizedMessage());
            }
        });
    }

    public void sendSubscribeSync(long tenantId, long userId, String templateId, String page, Map<String, Object> data, boolean force) {
        String token = getToken(tenantId, false);
        WxmpBindUser user = userService.getUser(tenantId, userId);
        if (user == null && force) throw new BusinessException("用户尚未绑定微信！");
        if (user == null) return;
        String appState = getAppState(tenantId);
        WxSendSubscribeRequest request = new WxSendSubscribeRequest(user.getOpenid(), templateId, page, convertDatas(data), appState);
        try {
            WxBaseResult result = wxApiFactory.getWxApi().sendSubscribe(token, request).execute().body();
            if (result.getErrcode() != 0) throw new BusinessException(result.getErrMsg());
        } catch (Exception ex) {
            throw new BusinessException(ex.getLocalizedMessage() == null ? "发送微信通知失败！" : ex.getLocalizedMessage());
        }
    }
    private void sendSubscribeSyncInternal(long tenantId, long userId, String templateId, String page, Map<String, WxmpDataItem> data, boolean force) {
        String token = getToken(tenantId, force);
        WxmpBindUser user = userService.getUser(tenantId, userId);
        if (user == null && force) throw new BusinessException("用户尚未绑定微信！");
        if (user == null) return;
        String appState = getAppState(tenantId);
        WxSendSubscribeRequest request = new WxSendSubscribeRequest(user.getOpenid(), templateId, page, data, appState);
        try {
            WxBaseResult result = wxApiFactory.getWxApi().sendSubscribe(token, request).execute().body();
            if (!force && result.getErrcode() == 41001) {
                sendSubscribeSyncInternal(tenantId, userId, templateId, page, data, true);
                return;
            } else if (result.getErrcode() != 0) {
                throw new BusinessException(result.getErrMsg());
            }
        } catch (Exception ex) {
            throw new BusinessException(ex.getLocalizedMessage() == null ? "发送微信通知失败！" : ex.getLocalizedMessage());
        }
    }
    private void sendSubscribeSync(WxmpSendMessage message) {
        WxmpSendMessage updated;
        try {
            Map<String, WxmpDataItem> datas = null;
            if (StringUtil.isNotBlank(message.getData())) datas = new Gson().fromJson(message.getData(), new TypeToken<Map<String, WxmpDataItem>>(){}.getType());
            sendSubscribeSyncInternal(message.getTenantId(), message.getUserId(), message.getTemplateId(), message.getPage(), datas, false);
            updated = new WxmpSendMessage(message.getId(), null);
        } catch (Exception ex) {
            updated = new WxmpSendMessage(message.getId(), ex.getLocalizedMessage());
        }
        messageMapper.updateByPrimaryKeySelective(updated);
    }

    private Map<String, WxmpDataItem> convertDatas(Map<String, ? extends Object> datas) {
        if (datas == null || datas.isEmpty()) return null;
        Map<String, WxmpDataItem> result = new HashMap<>();
        datas.forEach((key, value)-> result.put(key, new WxmpDataItem(value)));
        return result;
    }
    public void sendSubscribe(long tenantId, long userId, String templateId, String page, Map<String, Object> data) {
        String dataStr = null;
        if (data != null && !data.isEmpty()) dataStr = new Gson().toJson(convertDatas(data));
        WxmpSendMessage message = new WxmpSendMessage(tenantId, userId, templateId, page, dataStr);
        messageMapper.insert(message);
        trySendSubscribes();
    }
    public void sendSubscribes(long tenantId, Collection<Long> userIds, String templateId, String page, Map<String, Object> data) {
        final String dataStr;
        if (data != null && !data.isEmpty()) dataStr = new Gson().toJson(convertDatas(data));
        else dataStr = null;
        List<WxmpSendMessage> messages = new ArrayList<>();
        userIds.forEach(userId-> messages.add(new WxmpSendMessage(tenantId, userId, templateId, page, dataStr)));
        messageMapper.insertList(messages);
        trySendSubscribes();
    }

    @PostConstruct
    public void addQuartz() {
        quartzService.add(new QuartzTask(SendSubscribeService.class, ModuleName, "发送微信通知", "*/10 * * * * ?"));
    }
    void trySendSubscribes() {
        try {
            runInLock("Lock.wxmp.sendSubscribes", 100, 0, () -> {
                while (true) {
                    PageHelper.startPage(1, 100);
                    List<WxmpSendMessage> messages = new MapperQuery<>(WxmpSendMessage.class)
                            .andEqualTo("send", false)
                            .orderBy("addTime")
                            .query(messageMapper);
                    if (messages.isEmpty()) return;
                    messages.forEach(this::sendSubscribeSync);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
