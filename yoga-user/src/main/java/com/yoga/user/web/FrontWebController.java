package com.yoga.user.web;

import com.yoga.core.controller.BaseEndController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.utils.StrUtil;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.tenant.tenant.model.TenantCustomize;
import com.yoga.tenant.tenant.model.TenantSetting;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.duty.service.DutyService;
import com.yoga.user.shiro.SuperAdminUser;
import com.yoga.user.shiro.TenantToken;
import com.yoga.user.user.model.User;
import com.yoga.user.web.dto.IndexDto;
import com.yoga.user.web.dto.LoginDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller("webWebController")
@EnableAutoConfiguration
@RequestMapping("/web")
public class FrontWebController extends BaseEndController {
    @Autowired
    private SuperAdminUser superAdminUser;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private SettingService settingService;


    private String returnLogin(ModelMap model, long tenantId, Map<String, Object> param) {
        model.put("param", param);
        Map<String, String> img = new HashMap<>();
        TenantSetting setting = tenantService.getSetting(tenantId);
        img.put("pickLogin", setting.getLoginLogoUrl());
        img.put("topImage", setting.getTopImageUrl());
        img.put("loginBg", setting.getLoginBackUrl());
        model.put("img", img);
        Long deptId = departmentService.defaultLogonDept(tenantId);
        boolean allowLogon = (deptId != null && deptId != 0L);
        model.put("allowLogon", allowLogon);
        return "/web/login";
    }

    @RequestMapping("")
    public String index(TenantDto dto) {
        TenantCustomize customize = tenantService.getCustomize(dto.getTid());
        if (StrUtil.isBlank(customize.getAdminIndex()) || customize.equals("/web")) return "/web/index";
        else return "redirect:" + customize.getAdminIndex();
    }

    @RequestMapping(value = "/retrieve")
    public String retrieve(HttpServletRequest request, ModelMap model, IndexDto dto) {
        return "/web/retrieve";
    }
    @RequestMapping(value = "/logon")
    public String logon(HttpServletRequest request, ModelMap model, IndexDto dto) {
        return "/web/logon";
    }

    @RequestMapping(value = "/toLogin")
    public String toLogin(HttpServletRequest request, ModelMap model, IndexDto dto) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) subject.logout();
        TenantCustomize customize = tenantService.getCustomize(dto.getTid());
        if (StrUtil.isBlank(customize.getFrontLogin()) || customize.getFrontLogin().equals("/web/toLogin")) {
            return returnLogin(model, dto.getTid(), dto.wrapAsMap());
        } else {
            String query = request.getQueryString();
            String url = customize.getFrontLogin();
            if (StrUtil.isNotBlank(query)) {
                if (url.contains("?")) url += "&" + query;
                else url += "?" + query;
            }
            return "redirect:" + url;
        }
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, ModelMap model, @Valid LoginDto dto, BindingResult bindingResult) {
        CommonResult result = login(dto, bindingResult);
        if (result.getCode() == 0) {
            if (dto.getUri().trim().isEmpty()){
                return "redirect:" + dto.getUri();
            }else {
                return "redirect:" + dto.getUri();
            }
        } else {
            model.put("reason", result.getMessage());
            return returnLogin(model, dto.getTid(), dto.wrapAsMap());
        }
    }

    @RequestMapping(value = "/passwd")
    public String passwd(HttpServletRequest request, ModelMap model) {
        return "/web/passwd";
    }


    @RequestMapping(value = "/info")
    public String info(HttpServletRequest request, ModelMap model, TenantDto dto) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if (user.getDeptId() != null && user.getDeptId() != 0L) {
            model.put("deptName", departmentService.getName(dto.getTid(), user.getDeptId()));
        }
        if (user.getDutyId() != null && user.getDutyId() != 0L) {
            model.put("dutyName", dutyService.getName(dto.getTid(), user.getDutyId()));
        }
        return "/web/info";
    }

    @RequestMapping(value = "/modify")
    public String modify(HttpServletRequest request, ModelMap model, TenantDto dto) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if (user.getDeptId() != null && user.getDeptId() != 0L) {
            model.put("deptName", departmentService.getName(dto.getTid(), user.getDeptId()));
        }
        if (user.getDutyId() != null && user.getDutyId() != 0L) {
            model.put("dutyName", dutyService.getName(dto.getTid(), user.getDutyId()));
        }
        return "/web/modify";
    }

    private CommonResult login(LoginDto dto, BindingResult bindingResult) {
        if (!dto.isFirst() && bindingResult.hasErrors()) {
            return new CommonResult(ResultConstants.ERROR_ILLEGALPARAM, bindingResult.getFieldError().getDefaultMessage());
        }
        if (StrUtil.hasBlank(dto.getUsername(), dto.getPassword())) {
            return new CommonResult(ResultConstants.ERROR_ILLEGALPARAM, "");
        }
        if (superAdminUser.isAdmin(dto.getUsername())) {
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "您没有前台登录的权限！");
        }
        TenantToken token = new TenantToken(dto.getTid(), dto.getUsername(), dto.getPassword());
        Subject subject = SecurityUtils.getSubject();
        if (dto.isRemember()) subject.getSession().setTimeout(35 * 24 * 3600 * 1000);
        else subject.getSession().setTimeout(30 * 60 * 1000);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, e.getMessage());
        }
        if (!subject.isPermitted("pri_client.login")) {
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "您没有前台登录的权限！");
        }
        return new CommonResult();
    }
}
