package com.yoga.user.user.controller;


import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.captcha.CaptchaService;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.duty.model.Duty;
import com.yoga.user.duty.service.DutyService;
import com.yoga.user.shiro.SuperAdminUser;
import com.yoga.user.shiro.TenantToken;
import com.yoga.user.user.dto.CaptchaDto;
import com.yoga.user.user.dto.LoginDto;
import com.yoga.user.user.dto.LogonDto;
import com.yoga.user.user.dto.RetrieveDto;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Explain(value = "用户登录")
@Controller
@RequestMapping("/api/auth")
public class AuthApiController extends BaseApiController {

    @Autowired
    private SuperAdminUser superAdminUser;
    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DutyService dutyService;

    @Explain("用户注册")
    @ResponseBody
    @RequestMapping(value = "/logon")
    public CommonResult logon(HttpServletRequest request, @Valid LogonDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Long deptId = departmentService.defaultLogonDept(dto.getTid());
        if (deptId == null || deptId == 0L) throw new BusinessException("当前系统不允许注册");
        boolean requireMobile = departmentService.isLogonRequireMobile(dto.getTid());
        if (requireMobile && StrUtil.isBlank(dto.getMobile())) throw new IllegalArgumentException("请输入手机号！");
        if (StrUtil.isNotBlank(dto.getMobile()) && !StrUtil.isMobileNo(dto.getMobile())) throw new IllegalArgumentException("请输入正确的手机号！");
        if (StrUtil.isNotBlank(dto.getMobile())) {
            if (!captchaService.verifyCaptcha(dto.getTid(), dto.getPhone(), dto.getUuid(), dto.getCaptcha())) throw new IllegalArgumentException("验证码无效或者已过期");
        }
        if (StrUtil.isBlank(dto.getUsername())) dto.setUsername(dto.getMobile());
        if (dto.getUsername() == null || dto.getUsername().length() < 3) throw new IllegalArgumentException("用户名至少包含3个字符");
        if (dto.getPassword() == null || dto.getPassword().length() < 6) throw new IllegalArgumentException("密码至少包含6个字符");
        dto.setDeptId(deptId);
        dto.setRoleIds(null);
        dto.setDutyId(0);
        userService.addUser(dto);
        return new CommonResult();
    }

    @Explain("用户登录")
    @ResponseBody
    @RequestMapping(value = "/login")
    public CommonResult login(@Valid LoginDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        TenantToken token = new TenantToken(dto.getTid(), dto.getUsername(), dto.getPassword());
        Subject subject = (new Subject.Builder()).buildSubject();
        subject.getSession().setTimeout(15 * 24 * 3600 * 1000);
        ThreadContext.bind(subject);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "用户不存在或者密码错误");
        }
        if (!superAdminUser.isAdmin(dto.getUsername()) && !subject.isPermitted("pri_client.login")) {
            subject.logout();
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "您没有前台登录的权限！");
        }
        User user = userService.getLoginInfo();
        if (user != null && user.getExpire() != null && user.getExpire().before(new Date())) {
            subject.logout();
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "账号已过期！");
        }
        userService.updateUserCache(dto.getDeviceId());
        Map<String, Object> map = new MapConverter<User>(new MapConverter.Converter<User>() {
            @Override
            public void convert(User item, MapConverter.MapItem<String, Object> map) {
                map.set("userId", item.getId())
                        .set("username", StrUtil.lowerCase(item.getUsername()))
                        .set("lastName", item.getLastname())
                        .set("firstName", item.getFirstname())
                        .set("fullName", item.getFullname())
                        .set("avatar", item.getAvatar())
                        .set("title", item.getTitle())
                        .set("phone", item.getPhone())
                        .set("email", item.getEmail())
                        .set("qq", item.getQq())
                        .set("address", item.getAddress())
                        .set("postcode", item.getPostcode())
                        .set("company", item.getCompany())
                        .set("wechat", item.getWechat())
                        .set("roles", userService.getRoles(dto.getTid(), user.getId()))

                        .set("birthday", item.getBirthday())
                        .set("expire", item.getExpire())
                        .set("extLong", item.getExtLong())
                        .set("extText", item.getExtText())
                        .set("extInt", item.getExtInt())
                        .set("extDouble", item.getExtDouble())
                        .set("gender", item.getGender());
                Department department = departmentService.get(dto.getTid(), item.getDeptId());
                if (department != null) {
                    map.set("departmentId", department.getId())
                            .set("department", department.getName());
                }
                Duty duty = dutyService.get(dto.getTid(), item.getDutyId());
                if (duty != null) {
                    map.set("dutyId", duty.getId())
                            .set("duty", duty.getName());
                }
            }
        }).build(user);
        map.put("token", subject.getSession().getId());
        map.put("permissions", userService.getPermissions(user));
        return new CommonResult(map);
    }

    @Explain("获取手机验证码")
    @RequestMapping(value = "/captcha")
    @ResponseBody
    public CommonResult captcha(HttpServletRequest request, @Valid CaptchaDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String uuid = UUID.randomUUID().toString();
        String captcha = captchaService.sendCaptcha(dto.getTid(), dto.getMobile(), uuid);
        MapConverter.MapItem<String, Object> data = new MapConverter.MapItem<String, Object>()
                .set("uuid", uuid)
                .set("captcha", captcha);
        return new CommonResult("短信发送成功", data);
    }

    @Explain("用户找回密码")
    @RequestMapping(value = "/retrieve")
    @ResponseBody
    public CommonResult retrieve(HttpServletRequest request, @Valid RetrieveDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (!captchaService.verifyCaptcha(dto.getTid(), dto.getMobile(), dto.getUuid(), dto.getCaptcha())) {
            return new CommonResult(ResultConstants.ERROR_BUSINESSERROR, "验证码无效或者已过期");
        }
        if (dto.getNewPwd() == null || dto.getNewPwd().length() < 6) {
            return new CommonResult(ResultConstants.ERROR_ILLEGALPARAM, "密码至少包含6个字符");
        }
        userService.updatePassword(dto.getTid(), dto.getMobile(), dto.getNewPwd());
        return new CommonResult();
    }
}
