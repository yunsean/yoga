package com.yoga.imessager.rongcloud.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.imessager.rongcloud.dto.SettingDto;
import com.yoga.imessager.rongcloud.models.RongCloudSetting;
import com.yoga.imessager.rongcloud.service.RongService;
import com.yoga.tenant.setting.Settable;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Settable
@RequestMapping("/im/rong")
public class RongWebController extends BaseWebController {

    @Autowired
    private RongService rongService;

    @RequiresAuthentication
    @Settable(module = RongService.Module_Name, key = RongService.Key_RongCloud, name = "融云参数设置", systemOnly = true)
    @RequestMapping("/setting")
    public String defaultRegDept(ModelMap model, SettingDto dto) {
        RongCloudSetting setting = rongService.getSetting(dto.getTenantId());
        model.put("param", new HashMap<>());
        model.put("setting", setting);
        model.put("tenantId", dto.getTenantId());
        return "/imessager/setting";
    }
}
