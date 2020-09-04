package com.yoga.utility.captcha.controller;

import com.alibaba.fastjson.JSONObject;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StringUtil;
import com.yoga.utility.captcha.dto.SaveSettingDto;
import com.yoga.utility.captcha.model.CaptchaSetting;
import com.yoga.utility.captcha.service.CaptchaService;
import com.yoga.setting.annotation.Settable;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Settable
@Controller
@RequestMapping("/admin/setting/captcha")
public class CaptchaController extends BaseController {

    @Autowired
    private CaptchaService captchaService;

    @ApiIgnore
    @Settable(module = CaptchaService.ModuleName, key = CaptchaService.Key_Config, name = "通知设置-短信验证码设置")
    @GetMapping("")
    public String defaultRegDept(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        CaptchaSetting setting = captchaService.getSetting(dto.getTid());
        if (setting == null) setting = new CaptchaSetting();
        if (StringUtil.isBlank(setting.getFormat())) setting.setFormat("您的验证码是：#code#，#time#内有效。如非您本人操作，请忽略本消息。");
        model.put("setting", setting);
        return "/admin/utility/captcha/setting";
    }

    @ApiIgnore
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/save.json")
    public CommonResult saveSetting(@Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String format = dto.getFormat();
        if (StringUtil.isNotBlank(format) && !format.contains("#code#")) throw new IllegalArgumentException("无效的消息格式，请使用#code#代表验证码占位！");
        JSONObject jsonObject = new JSONObject(false);
        jsonObject.put("format", dto.getFormat());
        jsonObject.put("length", dto.getLength());
        jsonObject.put("expire", dto.getExpire());
        jsonObject.put("interval", dto.getInterval());
        jsonObject.put("autofill", dto.isAutofill());
        String showValue = dto.getFormat() + "/" + dto.getExpire() + "秒";
        captchaService.setSetting(dto.getTid(), jsonObject.toJSONString(), showValue);
        return new CommonResult();
    }
}

