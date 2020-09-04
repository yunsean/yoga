package com.yoga.utility.sms.controller;

import com.alibaba.fastjson.JSONObject;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StringUtil;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.model.Setting;
import com.yoga.utility.sms.dto.SmsSaveSettingDto;
import com.yoga.utility.sms.dto.SmsSendDto;
import com.yoga.utility.sms.dto.SmsShowSettingDto;
import com.yoga.utility.sms.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "短信发送")
@Settable
@Controller
@RequestMapping("/admin/setting/sms")
public class SmsController extends BaseController {

    @Autowired
    private SmsService smsService;

    @ApiIgnore
    @RequiresAuthentication
    @RequestMapping("")
    @Settable( module = SmsService.ModuleName, key = SmsService.Key_SmsConfig, name = "通知设置-短信网关设置")
    public String read(ModelMap model, @Valid SmsShowSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Setting setting = smsService.getSetting(dto.getTid());
        Map<String, Object> values = new HashMap<>();
        if (setting != null) {
            try {
                JSONObject jsonObject = JSONObject.parseObject(setting.getValue());
                if (StringUtil.isBlank(dto.getService())) {
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
        if (!StringUtil.isBlank(dto.getService())) {
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
        return "/admin/utility/sms/sms";
    }

    @ApiIgnore
    @ResponseBody
    @RequiresPermissions("sys_config.update")
    @RequestMapping("/save.json")
    public CommonResult save(HttpServletRequest request, @Valid SmsSaveSettingDto dto, BindingResult bindingResult) {
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

    @ApiOperation("发送短信")
    @ResponseBody
    @RequiresPermissions("gcf_sms.send")
    @PostMapping("/send.json")
    public ApiResult send(@Valid @ModelAttribute SmsSendDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        smsService.sendSms(dto.getTid(), dto.getMobile(), dto.getContent(), "send");
        return new ApiResult();
    }
}

