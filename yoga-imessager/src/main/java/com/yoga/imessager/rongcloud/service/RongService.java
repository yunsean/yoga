package com.yoga.imessager.rongcloud.service;

import com.alibaba.fastjson.JSONObject;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.rongcloud.RongCloud;
import com.yoga.imessager.rongcloud.models.RongCloudSetting;
import com.yoga.imessager.rongcloud.models.TokenResult;
import com.yoga.tenant.setting.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RongService {

    @Autowired
    private SettingService settingService;

    public final static String Module_Name = "gcf_imessager";
    public final static String Key_RongCloud = "rongcloud.appkey";
    public RongCloud rongCloud(long tenantId) {
        RongCloudSetting setting = settingService.get(tenantId, Module_Name, Key_RongCloud, new RongCloudSetting());
        return RongCloud.getInstance(setting.getAppCode(), setting.getAppSecret());
    }

    public RongCloudSetting getSetting(long tenantId) {
        RongCloudSetting setting = settingService.get(tenantId, Module_Name, Key_RongCloud, new RongCloudSetting());
        return setting;
    }
    public void saveSetting(long tenantId, RongCloudSetting setting) {
        settingService.save(tenantId, Module_Name, Key_RongCloud, JSONObject.toJSONString(setting), setting.getAppCode());
    }

    /**
     * 获取当前用户TOKEN，在融云中，token唯一标识一个用户，为了避免通过其他租户绕过用户身份检查，可以设置专属的prefix
     * @param tenantId  租户ID
     * @param userId    用户标识
     * @param nickname  用户昵称
     * @param avatar    用户头像
     * @return          用户token
     * @throws Exception
     */
    public String token(long tenantId, String userId, String nickname, String avatar) {
        if (avatar == null) avatar = "";
        if (StrUtil.isBlank(nickname)) nickname = "用户" + userId;
        try {
            TokenResult result = rongCloud(tenantId)
                    .user
                    .getToken(userId, nickname, avatar);
            if (200 != result.getCode()) throw new BusinessException(result.getErrorMessage());
            return result.getToken();
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }

    public void refresh(long tenantId, String userId, String nickname, String avatar) {
        try {
            rongCloud(tenantId)
                    .user
                    .refresh(userId, nickname, avatar);
        } catch (Exception ex) {
            throw new BusinessException(ex.getMessage());
        }
    }
}
