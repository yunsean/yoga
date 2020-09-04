package com.yoga.utility.push.controller;

import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.logging.model.LoginUser;
import com.yoga.setting.annotation.Settable;
import com.yoga.setting.service.SettingService;
import com.yoga.utility.mail.dto.MailSettingSaveDto;
import com.yoga.utility.mail.dto.MailSettingTestDto;
import com.yoga.utility.mail.model.MailSetting;
import com.yoga.utility.mail.service.MailService;
import com.yoga.utility.push.dto.PushRegisterDto;
import com.yoga.utility.push.service.PushService;
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

import javax.validation.Valid;

@Api(tags = "消息推送")
@Controller
@RequestMapping("/admin/utility/push")
public class PushController extends BaseController {

    @Autowired
    private PushService pushService;

    @ApiOperation("注册信息推送")
    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/register.json")
    public ApiResult register(@Valid @ModelAttribute PushRegisterDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        LoginUser loginUser = LoginUser.getLoginUser();
        pushService.register(dto.getTid(), loginUser.getId(), dto.getClientId(), dto.getPushChannel());
        return new ApiResult();
    }

    @ApiOperation("反注册信息推送")
    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/unregister.json")
    public ApiResult unregister(@Valid @ModelAttribute BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        LoginUser loginUser = LoginUser.getLoginUser();
        pushService.unregister(dto.getTid(), loginUser.getId());
        return new ApiResult();
    }
}
