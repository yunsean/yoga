package com.yoga.weixinapp.controller;

import com.yoga.admin.shiro.OperatorToken;
import com.yoga.admin.shiro.SuperAdminUser;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.StringUtil;
import com.yoga.operator.user.model.User;
import com.yoga.setting.annotation.Settable;
import com.yoga.weixinapp.ao.SettingConfig;
import com.yoga.weixinapp.dto.SaveSettingDto;
import com.yoga.weixinapp.dto.WexinBindDto;
import com.yoga.weixinapp.dto.WexinLoginDto;
import com.yoga.weixinapp.service.WxmpService;
import com.yoga.weixinapp.service.WxmpUserService;
import com.yoga.weixinapp.shiro.WechatToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
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

@Settable
@Api(tags = "小程序接入")
@Controller("weixinAppController")
@RequestMapping("/admin/weixinapp")
@Settable(module = WxmpService.ModuleName, key = WxmpService.Key_AppState, name = "微信小程序-小程序发布状态", placeHolder = "developer为开发版；trial为体验版；formal为正式版")
public class WeixinappController extends BaseController {

    @Autowired
    private WxmpService wxmpService;
    @Autowired
    private WxmpUserService wxmpUserService;
    @Autowired
    private SuperAdminUser superAdminUser;

    @ApiIgnore
    @RequiresAuthentication
    @Settable(module = WxmpService.ModuleName, key = WxmpService.Key_Setting, name = "微信小程序-开发者ID")
    @RequestMapping("/setting")
    public String setting(ModelMap model, @Valid BaseDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        SettingConfig config = wxmpService.getSetting(dto.getTid());
        model.put("setting", config);
        return "/admin/weixinapp/setting";
    }

    @ResponseBody
    @ApiOperation("微信用户登录")
    @PostMapping("/weixin/login.json")
    public ApiResult<String> login(@Valid @ModelAttribute WexinLoginDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String openid = wxmpUserService.getOpenidByCode(dto.getTid(), dto.getCode());
        WechatToken wxToken = new WechatToken(dto.getTid(), openid);
        Subject subject = (new Subject.Builder()).buildSubject();
        subject.getSession().setTimeout(-1);
        ThreadContext.bind(subject);
        try {
            subject.login(wxToken);
        } catch (AuthenticationException e) {
            return new ApiResult<>(-20, "用户未绑定");
        }
        String token = subject.getSession().getId().toString();
        return new ApiResult<>(token);
    }
    @ResponseBody
    @ApiOperation("微信用户绑定")
    @PostMapping("/weixin/bind.json")
    public ApiResult<String> bind(@Valid @ModelAttribute WexinBindDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String openId = wxmpUserService.getOpenidByCode(dto.getTid(), dto.getCode());
        OperatorToken token = new OperatorToken(dto.getTid(), dto.getUsername(), dto.getPassword());
        Subject subject = (new Subject.Builder()).buildSubject();
        subject.getSession().setTimeout(15 * 24 * 3600 * 1000);
        ThreadContext.bind(subject);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return new ApiResult<>(ResultConstants.ERROR_FORBIDDEN, "用户不存在或者密码错误");
        }
        if (superAdminUser.isAdmin(dto.getUsername()) || !subject.isPermitted("web.login")) {
            subject.logout();
            return new ApiResult<>(ResultConstants.ERROR_FORBIDDEN, "您没有前台登录的权限！");
        }
        User user = User.getLoginUser();
        wxmpUserService.bindUser(dto.getTid(), openId, user.getId());
        return new ApiResult<>(subject.getSession().getId().toString());
    }
    @ResponseBody
    @RequiresAuthentication
    @ApiOperation("微信用户解除绑定")
    @PostMapping("/weixin/unbind.json")
    public ApiResult unbind(@ModelAttribute BaseDto dto) {
        String openId = (String) SecurityUtils.getSubject().getSession().getAttribute("openId");
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        wxmpUserService.unbindUser(dto.getTid(), openId, user.getId());
        SecurityUtils.getSubject().logout();
        return new ApiResult();
    }


    @ResponseBody
    @ApiOperation("微信访问Token")
    @PostMapping("/weixin/token.json")
    public ApiResult<String> token(@Valid @ModelAttribute WexinLoginDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String token = wxmpService.getToken(dto.getTid(), false);
        return new ApiResult<>(token);
    }

    @ApiIgnore
    @ResponseBody
    @RequiresAuthentication
    @PostMapping("/setting/save.json")
    public ApiResult fillSetting(@Valid @ModelAttribute SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        SettingConfig config = new SettingConfig(dto.getAppId(), dto.getAppSecret());
        wxmpService.saveSetting(dto.getTid(), config);
        return new ApiResult();
    }
}
