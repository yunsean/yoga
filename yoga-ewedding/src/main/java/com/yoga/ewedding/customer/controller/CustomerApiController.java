package com.yoga.ewedding.customer.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.ewedding.counselor.service.CounselorService;
import com.yoga.ewedding.customer.dto.LoginDto;
import com.yoga.ewedding.customer.dto.LogonDto;
import com.yoga.user.captcha.CaptchaService;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.shiro.SuperAdminUser;
import com.yoga.user.shiro.TenantToken;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;

@Explain(value = "Ew客户管理", module = "ew_customer")
@RestController("eweddingCustomerApiController")
@EnableAutoConfiguration
@RequestMapping("/api/ewedding/customer")
public class CustomerApiController extends BaseApiController {

    @Autowired
    private CounselorService counselorService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private SuperAdminUser superAdminUser;

    @Explain("客户注册")
    @RequestMapping("/logon")
    public CommonResult logon(@Valid LogonDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Long deptId = departmentService.defaultLogonDept(dto.getTid());
        if (deptId == null || deptId == 0L) throw new BusinessException("当前系统不允许注册");
        boolean requireMobile = departmentService.isLogonRequireMobile(dto.getTid());
        if (requireMobile && StrUtil.isBlank(dto.getMobile())) throw new IllegalArgumentException("请输入手机号！");
        if (StrUtil.isNotBlank(dto.getMobile()) && !StrUtil.isMobileNo(dto.getMobile())) throw new IllegalArgumentException("请输入正确的手机号！");
        if (StrUtil.isNotBlank(dto.getMobile())) {
            if (!captchaService.verifyCaptcha(dto.getTid(), dto.getMobile(), dto.getUuid(), dto.getCaptcha())) throw new IllegalArgumentException("验证码无效或者已过期");
        }
        userService.addUser(dto.getTid(), dto.getMobile(), dto.getPassword(), null, dto.getNickname(), deptId, 0, null, dto.getAvatar(), dto.getMobile(), null, null, null, null, null, null, null, dto.getMarryDate(), null, null, null, null, dto.getGender(), false, null);
        return new CommonResult();
    }


    @Explain("客户登录")
    @ResponseBody
    @RequestMapping(value = "/login")
    public CommonResult login(@Valid LoginDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Long deptId = departmentService.defaultLogonDept(dto.getTid());
        if (deptId == null || deptId == 0L) throw new BusinessException("当前系统不允许注册");
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
        if (user.getDeptId() == null || !user.getDeptId().equals(deptId)) {
            subject.logout();
            throw new BusinessException("请使用新人账号登录！");
        }
        if (user != null && user.getExpire() != null && user.getExpire().before(new Date())) {
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
                        .set("wechat", item.getWechat());
            }
        }).build(user);
        map.put("token", subject.getSession().getId());
        map.put("permissions", userService.getPermissions(user));
        return new CommonResult(map);
    }
}
