package com.yoga.utility.mail.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.mail.dto.MailSettingSaveDto;
import com.yoga.utility.mail.dto.MailSettingTestDto;
import com.yoga.utility.mail.model.MailSetting;
import com.yoga.utility.mail.service.MailService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@ApiIgnore
@Controller
@Settable
@RequestMapping("/admin/setting/email")
public class MailController extends BaseController {

    @Autowired
    private MailService mailService;

    @RequestMapping("")
    @RequiresAuthentication
    @Settable(module = MailService.ModuleName, key = MailService.Key_Email, name = "通知设置-邮件发送服务")
    public String defaultRegDept(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        MailSetting setting = mailService.getSetting(dto.getTid());
        if (setting == null) setting = new MailSetting();
        model.put("setting", setting);
        return "/admin/utility/email/email";
    }

    @ResponseBody
    @RequiresPermissions(SettingService.Permission_Update)
    @RequestMapping("/save.json")
    public ApiResult save(@Valid MailSettingSaveDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        mailService.setSetting(dto.getTid(), new MailSetting(dto.getSmtpServer(), dto.getSmtpPort(), dto.getReplyAddress(), dto.getSendAccount(), dto.getSendPassword(), dto.isUseSsl()));
        return new ApiResult();
    }
    @ResponseBody
    @RequiresAuthentication
    @RequestMapping("/test.json")
    public ApiResult test(@Valid MailSettingTestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        MailService.send(dto.getSmtpServer(), dto.getSmtpPort(), "发件人", dto.getSendAccount(), dto.getSendAccount(), dto.getSendPassword(), "收件人", dto.getTestAddress(), "测试邮件", "您好！这是一封检测邮件服务器设置的测试邮件。收到此邮件，意味着您的邮件服务器设置正确！您可以进行其它邮件发送的操作了！", dto.isUseSsl(), false);
        return new ApiResult();
    }
}
