package com.yoga.user.captcha;

import com.alibaba.fastjson.JSONObject;
import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.Settable;
import com.yoga.user.captcha.dto.SaveSettingDto;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Settable
@Controller
@RequestMapping("/common/captcha")
public class CaptchaApiController extends BaseApiController {

    @Autowired
    private CaptchaService captchaService;

    @Explain(exclude = true)
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/setting/save")
    public CommonResult upsertDefaultRegDept(HttpServletRequest request, @Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String format = dto.getFormat();
        if (StrUtil.isNotBlank(format) && !format.contains("#code#")) throw new IllegalArgumentException("无效的消息格式，请使用#code#代表验证码占位！");
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

