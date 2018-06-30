package com.yoga.user.captcha;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.Settable;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.captcha.model.CaptchaSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Settable
@Controller
@RequestMapping("/common/captcha")
public class CaptchaWebController extends BaseWebController {

    @Autowired
    private CaptchaService captchaService;

    @Settable(key = CaptchaService.CaptchaConfig, name = "通知设置-短信验证码设置", module = CaptchaService.CaptchaMoudle)
    @RequestMapping("")
    public String defaultRegDept(HttpServletRequest request, ModelMap model, @Valid TenantDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        CaptchaSetting setting = captchaService.getSetting(dto.getTid());
        if (setting == null) setting = new CaptchaSetting();
        if (StrUtil.isBlank(setting.getFormat())) setting.setFormat("您的验证码是：#code#，#time#内有效。如非您本人操作，请忽略本消息。");
        model.put("setting", setting);
        return "/captcha/setting";
    }
}

