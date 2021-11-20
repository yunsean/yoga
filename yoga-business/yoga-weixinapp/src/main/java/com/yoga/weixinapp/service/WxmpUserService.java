package com.yoga.weixinapp.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.weixinapp.ao.SettingConfig;
import com.yoga.weixinapp.mapper.WxmpUserMapper;
import com.yoga.weixinapp.model.WxmpBindUser;
import com.yoga.weixinapp.wxapi.WxApiFactory;
import com.yoga.weixinapp.wxapi.WxSessionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class WxmpUserService extends BaseService {

    @Autowired
    private WxmpUserMapper userMapper;
    @Lazy
    @Autowired
    private WxmpService wxmpService;
    @Autowired
    private WxApiFactory wxApiFactory;

    public WxmpBindUser getUser(long tenantId, String openid) {
        WxmpBindUser user = MapperQuery.create(WxmpBindUser.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("openid", openid)
                .queryFirst(userMapper);
        return user;
    }
    public WxmpBindUser getUser(long tenantId, long userId) {
        WxmpBindUser user = MapperQuery.create(WxmpBindUser.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("userId", userId)
                .queryFirst(userMapper);
        return user;
    }

    public void bindUser(long tenantId, String openid, long userId) {
        WxmpBindUser user = MapperQuery.create(WxmpBindUser.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("openid", openid)
                .queryFirst(userMapper);
        if (user != null && user.getUserId() == userId) return;
        if (user != null) throw new BusinessException("该微信号已经被绑定！");
        user = MapperQuery.create(WxmpBindUser.class)
                .andEqualTo("userId", userId)
                .queryFirst(userMapper);
        if (user != null) throw new BusinessException("该账号已经被绑定！");
        userMapper.insert(new WxmpBindUser(openid, tenantId, userId));
    }

    public void unbindUser(long tenantId, String openid, long userId) {
        WxmpBindUser user = MapperQuery.create(WxmpBindUser.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("openid", openid)
                .andEqualTo("userId", userId)
                .queryFirst(userMapper);
        if (user != null) userMapper.delete(user);
    }

    public String getOpenidByCode(long tenantId, String code) {
        try {
            SettingConfig config = wxmpService.getSetting(tenantId);
            if (config == null || StringUtil.isBlank(config.getAppId())) throw new BusinessException("尚未配置小程序开发ID！");
            WxSessionResult result = wxApiFactory.getWxApi().getSession(config.getAppId(), config.getAppSecret(), code, "authorization_code").execute().body();
            if (result == null) throw new BusinessException("请求微信回话失败！");
            if (result.getErrcode() != 0) throw new BusinessException(result.getErrMsg());
            return result.getOpenid();
        } catch (Exception ex) {
            throw new BusinessException(ex.getLocalizedMessage());
        }
    }
    public String getOpenidByCode(String appId, String appSecret, String code) {
        try {
            WxSessionResult result = wxApiFactory.getWxApi().getSession(appId, appSecret, code, "authorization_code").execute().body();
            if (result == null) throw new BusinessException("请求微信回话失败！");
            if (result.getErrcode() != 0) throw new BusinessException(result.getErrMsg());
            return result.getOpenid();
        } catch (Exception ex) {
            throw new BusinessException(ex.getLocalizedMessage());
        }
    }
}
