package com.yoga.user.admin.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.tenant.tenant.model.TenantCustomize;
import com.yoga.tenant.tenant.model.TenantSetting;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.user.admin.dto.LoginDto;
import com.yoga.user.admin.dto.ToLoginDto;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.model.LoginUser;
import com.yoga.user.shiro.SuperAdminUser;
import com.yoga.user.shiro.TenantToken;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.patchca.background.SingleColorBackgroundFactory;
import org.patchca.color.RandomColorFactory;
import org.patchca.filter.FilterFactory;
import org.patchca.font.RandomFontFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@EnableAutoConfiguration
@RequestMapping("/admin")
public class AdminWebController extends BaseWebController {

    @Autowired
    SuperAdminUser superAdminUser;
    @Autowired
    private PropertiesService propertiesService;
    @Autowired
    private UserService userService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private SettingService settingService;

    @Value("${yoga.login.choice-tenant:false}")
    private boolean choiceTenant = false;

    @RequiresAuthentication
    @RequestMapping(value = "")
    public String index(HttpServletRequest request, @Valid TenantDto dto, ModelMap model) {
        TenantCustomize customize = tenantService.getCustomize(dto.getTid());
        if (StrUtil.isBlank(customize.getAdminIndex()) || customize.equals("/admin")) return "/admin/index";
        else return "redirect:" + customize.getAdminIndex();
    }

    @RequiresAuthentication
    @RequestMapping(value = "/top")
    public String top(HttpServletRequest request, ModelMap model, @Valid TenantDto dto, BindingResult bindingResult) {
        TenantCustomize customize = tenantService.getCustomize(dto.getTid());
        if (StrUtil.isBlank(customize.getAdminTop()) || customize.equals("/admin/top")) {
            if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
            User user = userService.getLoginInfo();
            if (null != user) {
                model.put("authName", user.getFullname());
            }
            TenantSetting setting = tenantService.getSetting(dto.getTid());
            model.put("topImg", setting.getTopImageUrl());
            return "/admin/top";
        } else {
            String query = request.getQueryString();
            String url = customize.getAdminTop();
            if (StrUtil.isNotBlank(query)) {
                if (url.contains("?")) url += "&" + query;
                else url += "?" + query;
            }
            return "redirect:" + url;
        }
    }

    @RequiresAuthentication
    @RequestMapping(value = "/left")
    public String left(HttpServletRequest request, @Valid TenantDto dto, ModelMap model) {
        TenantCustomize customize = tenantService.getCustomize(dto.getTid());
        if (StrUtil.isBlank(customize.getAdminLeft()) || customize.equals("/admin/left")) return "/admin/left";
        else return "redirect:" + customize.getAdminLeft();
    }

    @RequiresAuthentication
    @RequestMapping(value = "/welcome")
    public String welcome(HttpServletRequest request, @Valid TenantDto dto, ModelMap model, LoginUser loginUser) {
        TenantCustomize customize = tenantService.getCustomize(dto.getTid());
        if (StrUtil.isBlank(customize.getAdminWelcome()) || customize.equals("/admin/welcome")) {
            User user = userService.getLoginInfo();
            model.put("userInfo", user);
            return "/welcome";
        } else {
            String query = request.getQueryString();
            String url = customize.getAdminWelcome();
            if (StrUtil.isNotBlank(query)) {
                if (url.contains("?")) url += "&" + query;
                else url += "?" + query;
            }
            return "redirect:" + url;
        }
    }

    @RequiresAuthentication
    @RequestMapping(value = "/passwd")
    public String toResetPwd(ModelMap model) {
        User user = userService.getLoginInfo();
        if (user == null) {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
            return "redirect:/admin/toLogin";
        } else {
            model.put("user", user);
            return "/admin/passwd";
        }
    }

    @RequestMapping(value = "/toLogin")
    public void loginJump(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.getWriter().println("<script>top.location='/admin/loginJump'</script>");
        response.getWriter().close();
    }
    @RequestMapping(value = "/loginJump")
    public String toLogin(HttpServletRequest request, ModelMap model, @Valid ToLoginDto dto, BindingResult bindingResult) {
        TenantCustomize customize = tenantService.getCustomize(dto.getTid());
        if (StrUtil.isBlank(customize.getAdminLogin()) || customize.getAdminLogin().equals("/admin/loginJump") || customize.getAdminLogin().equals("/admin/toLogin")) {
            Map<String, String> img = new HashMap<>();
            TenantSetting setting = tenantService.getSetting(dto.getTid());
            img.put("pickLogin", setting.getLoginLogoUrl());
            img.put("topImage", setting.getTopImageUrl());
            img.put("loginBg", setting.getLoginBackUrl());
            model.put("img", img);
            model.put("error", dto.getReason());
            if (choiceTenant) model.put("tenants", tenantService.getAll());
            return "/admin/login";
        } else {
            String query = request.getQueryString();
            String url = customize.getAdminLogin();
            if (StrUtil.isNotBlank(query)) {
                if (url.contains("?")) url += "&" + query;
                else url += "?" + query;
            }
            return "redirect:" + url;
        }
    }

        @Value("${yoga.login.patcha-font:}")
    private String patchaFont = null;
    private class FontFactory extends RandomFontFactory {
        FontFactory() {
            super();
            if (StrUtil.isNotBlank(patchaFont)) {
                this.families = new ArrayList<>();
                this.families.add(patchaFont);
            }
        }
    }

    private Random random = new Random();
    private int filterType = 0;
    private FilterFactory[] filterFactories = {new org.patchca.filter.predefined.MarbleRippleFilterFactory(),
            new org.patchca.filter.predefined.DoubleRippleFilterFactory(),
            new org.patchca.filter.predefined.WobbleRippleFilterFactory(),
            new org.patchca.filter.predefined.RippleFilterFactory(),
            new org.patchca.filter.predefined.DiffuseRippleFilterFactory(),
            new org.patchca.filter.predefined.CurvesRippleFilterFactory()};
    @RequestMapping(value = "/patchca")
    public void creatCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        RandomFontFactory fontFactory = new FontFactory();
        fontFactory.setMinSize(20);
        fontFactory.setMaxSize(30);
        RandomWordFactory wordFactory = new RandomWordFactory();
        wordFactory.setMinLength(4);
        wordFactory.setMaxLength(4);
        wordFactory.setCharacters("23456789abcdefghigkmnpqrstuvwxyz");
        cs.setHeight(30);
        cs.setWidth(140);
        cs.setWordFactory(wordFactory);
        cs.setFontFactory(fontFactory);
        cs.setColorFactory(new RandomColorFactory());
        cs.setBackgroundFactory(new SingleColorBackgroundFactory(Color.white));
        filterType++;
        //cs.setFilterFactory(filterFactories[random.nextInt(filterFactories.length)]);
        cs.setFilterFactory(new org.patchca.filter.predefined.DoubleRippleFilterFactory());
        HttpSession session = request.getSession();
        if (session == null) {
            session = request.getSession();
        }
        try {
            ServletOutputStream stream = response.getOutputStream();
            String patchcaToken = EncoderHelper.getChallangeAndWriteImage(cs, "png", stream);
            session.setAttribute("patchcaToken", patchcaToken);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/login")
    public String webLogin(HttpServletRequest request, ModelMap model, @Valid LoginDto dto, BindingResult bindingResult) {
        String patchcaToken = "";
        String reason = null;
        if (bindingResult.hasErrors()) {
            reason = bindingResult.getFieldError().getDefaultMessage();
        } else {
            try {
                patchcaToken = (String) request.getSession().getAttribute("patchcaToken");
            } catch (Exception ex) {
                patchcaToken = "";
            }
            if (propertiesService.isAdminCheckPatchca() && (patchcaToken == null || !patchcaToken.equals(dto.getPatchca()))) {
                reason = "验证码错误";
            } else {
                UsernamePasswordToken token = new TenantToken(dto.getTid(), dto.getUsername(), dto.getPassword());
                Subject subject = SecurityUtils.getSubject();
                if (dto.isRememberMe()) subject.getSession().setTimeout(15 * 24 * 3600 * 1000);
                try {
                    subject.login(token);
                } catch (AuthenticationException e) {
                    reason = "用户名无效或者密码错误";
                }
                if (reason == null && !superAdminUser.isAdmin(dto.getUsername()) && !subject.isPermitted("pri_manage.login")) {
                    reason = "您没有后台登录的权限！";
                }
            }
        }
        if (reason == null) {
            if (StrUtil.isNotBlank(dto.getUri())) return "redirect:" + dto.getUri();
            else return "redirect:/admin";
        } else {
            try {
                reason = URLEncoder.encode(reason, "UTF-8");
            } catch (Exception ex) {

            }
            return "redirect:/admin/loginJump?reason=" + reason;
        }
    }

    @RequestMapping(value = "/retrieve")
    public String retrieve(ModelMap model, TenantDto dto) {
        Map<String, String> img = new HashMap<>();
        TenantSetting setting = tenantService.getSetting(dto.getTid());
        img.put("pickLogin", setting.getLoginLogoUrl());
        img.put("topImage", setting.getTopImageUrl());
        img.put("loginBg", setting.getLoginBackUrl());
        model.put("img", img);
        return "/admin/retrieve";
    }

    @RequiresAuthentication
    @RequestMapping(value = "/logout")
    public String webLogout(HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/admin/toLogin";
    }
}
