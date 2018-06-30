package com.yoga.tenant.sms;

import com.alibaba.fastjson.JSONObject;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.sms.dto.SaveSettingDto;
import com.yoga.tenant.sms.dto.SettingDto;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Settable
@Controller
@RequestMapping("/common/sms")
public class SmsController extends BaseWebController {

    @Autowired
    private SmsService smsService;

    @Settable(key = SmsService.SmsConfig, name = "通知设置-短信网关设置", module = SmsService.SmsMoudle)
    @RequestMapping("")
    public String defaultRegDept(HttpServletRequest request, ModelMap model, @Valid SettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Setting setting = smsService.getSetting(dto.getTid());
        Map<String, Object> values = new HashMap<>();
        if (setting != null) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(setting.getValue());
                if (StrUtil.isBlank(dto.getService())) {
                    dto.setService(jsonObject.getString("service"));
                }
                for (String key : jsonObject.keySet()) {
                    if (key.equals("service")) continue;
                    values.put("sms." + key, jsonObject.getString(key));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        model.put("services", smsService.services());
        if (!StrUtil.isBlank(dto.getService())) {
            Map<String, String> items = smsService.configItems(dto.getService());
            Map<String, String> newItems = new HashMap<>();
            if (items != null) {
                for (String key : items.keySet()) {
                    newItems.put("sms." + key, items.get(key));
                }
            }
            model.put("configs", newItems);
        }
        model.put("values", values);
        Map<String, Object> param = new HashMap<>();
        param.put("service", dto.getService());
        model.put("param", param);
        return "/sms/setting";
    }

    @Explain(exclude = true)
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/setting/save")
    public CommonResult upsertDefaultRegDept(HttpServletRequest request, @Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, String[]> parameters = request.getParameterMap();
        JSONObject jsonObject = new JSONObject(false);
        jsonObject.put("service", dto.getService());
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            if (!key.startsWith("sms.")) continue;
            if (values == null || values.length < 1) continue;
            jsonObject.put(key.substring(4), values[0]);
        }
        String showValue = smsService.getServiceName(dto.getService());
        smsService.setSetting(dto.getTid(), jsonObject.toJSONString(), showValue);
        return new CommonResult();
    }
}

