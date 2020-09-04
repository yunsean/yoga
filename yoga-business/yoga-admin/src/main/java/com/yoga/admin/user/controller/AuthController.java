package com.yoga.admin.user.controller;


import com.yoga.admin.user.vo.CaptchaVo;
import com.yoga.admin.user.vo.UserInfoVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.base.BaseDto;
import com.yoga.core.data.*;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.redis.RedisOperator;
import com.yoga.core.utils.StringUtil;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.utility.captcha.service.CaptchaService;
import com.yoga.admin.shiro.RedisSessionDAO;
import com.yoga.admin.shiro.SuperAdminUser;
import com.yoga.admin.shiro.OperatorToken;
import com.yoga.admin.user.dto.UserCaptchaDto;
import com.yoga.admin.user.dto.UserLoginDto;
import com.yoga.admin.user.dto.UserLogonDto;
import com.yoga.admin.user.dto.UserRetrieveDto;
import com.yoga.operator.user.model.User;
import com.yoga.operator.user.service.UserService;
import com.yoga.setting.annotation.Settable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Controller
@Api(tags = "用户登录")
@RequestMapping("/admin/auth")
@Settable(module = "gcf_user_logon", key = UserService.Key_LogonUseCaptcha, name = "用户注册-需要手机号验证", type = boolean.class, defaultValue = "true")
public class AuthController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private SuperAdminUser superAdminUser;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private RedisSessionDAO sessionDAO;

    @ResponseBody
    @PostMapping(value = "/logon.json")
    @ApiOperation(value = "用户注册")
    public ApiResult logon(@Valid @ModelAttribute UserLogonDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Long deptId = branchService.getLogonBranch(dto.getTid());
        if (deptId == null || deptId == 0L) throw new BusinessException("当前系统不允许注册");
        if (userService.isLogonUseCaptcha(dto.getTid())) {
            if (StringUtil.isNotBlank(dto.getMobile())) throw new IllegalArgumentException("请输入正确的手机号！");
            if (!captchaService.verifyCaptcha(dto.getTid(), dto.getMobile(), dto.getUuid(), dto.getCaptcha())) throw new IllegalArgumentException("验证码无效或者已过期");
        }
        if (StringUtil.isBlank(dto.getUsername())) dto.setUsername(dto.getMobile());
        if (dto.getUsername() == null || dto.getUsername().length() < 3) throw new IllegalArgumentException("用户名至少包含3个字符");
        if (dto.getPassword() == null || dto.getPassword().length() < 6) throw new IllegalArgumentException("密码至少包含6个字符");
        userService.add(dto.getTid(), dto.getUsername(), dto.getPassword(), deptId, null, dto.getNickname(),
                null, null, dto.getAvatar(), dto.getMobile(), dto.getEmail(), null, null, null,
                null, null);
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping(value = "/login.json")
    @ApiOperation(value = "用户登录")
    public ApiResult<UserInfoVo> login(@Valid @ModelAttribute UserLoginDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        OperatorToken token = new OperatorToken(dto.getTid(), dto.getUsername(), dto.getPassword());
        Subject subject = (new Subject.Builder()).buildSubject();
        subject.getSession().setTimeout(15 * 24 * 3600 * 1000);
        ThreadContext.bind(subject);
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return new ApiResult<>(ResultConstants.ERROR_FORBIDDEN, "用户不存在或者密码错误");
        }
        if (!superAdminUser.isAdmin(dto.getUsername()) && !subject.isPermitted("web.login")) {
            subject.logout();
            return new ApiResult<>(ResultConstants.ERROR_FORBIDDEN, "您没有前台登录的权限！");
        }
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        updateUserCache(dto.getDeviceId());
        return new ApiResult<>(user, UserInfoVo.class, (po, vo)-> {
            if (vo.getAddress() == null) vo.setAddress("");
            vo.setToken(subject.getSession().getId().toString());
            vo.setPermissions(userService.getPrivileges(user.getTenantId(), user.getId()));
        });
    }
    @ResponseBody
    @RequiresAuthentication
    @GetMapping(value = "/info.json")
    @ApiOperation(value = "获取登录用户的信息")
    public ApiResult<UserInfoVo> info(HttpServletRequest request, @ModelAttribute BaseDto dto) {
        User loginUser = User.getLoginUser();
        User user = userService.get(dto.getTid(), loginUser.getId());
        return new ApiResult<>(user, UserInfoVo.class, (po, vo)-> {
            if (vo.getAddress() == null) vo.setAddress("");
            String token = SecurityUtils.getSubject().getSession().getId().toString();
            if (StringUtil.isNotBlank(request.getHeader("token"))) token = request.getHeader("token");
            vo.setToken(token);
            vo.setPermissions(userService.getPrivileges(user.getTenantId(), user.getId()));
        });
    }
    @GetMapping(value = "/captcha.json")
    @ResponseBody
    @ApiOperation(value = "获取手机验证码")
    public ApiResult<CaptchaVo> captcha(@Valid @ModelAttribute UserCaptchaDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        String uuid = UUID.randomUUID().toString();
        String captcha = captchaService.sendCaptcha(dto.getTid(), dto.getMobile(), uuid);
        return new ApiResult<>(new CaptchaVo(uuid, captcha));
    }

    @PostMapping(value = "/retrieve.json")
    @ResponseBody
    @ApiOperation(value = "用户找回密码")
    public ApiResult retrieve(@Valid @ModelAttribute UserRetrieveDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (!captchaService.verifyCaptcha(dto.getTid(), dto.getMobile(), dto.getUuid(), dto.getCaptcha())) {
            throw  new IllegalArgumentException("验证码无效或者已过期");
        }
        if (dto.getNewPwd() == null || dto.getNewPwd().length() < 6) {
            throw new IllegalArgumentException("密码至少包含6个字符");
        }
        userService.passwd(dto.getTid(), dto.getMobile(), dto.getNewPwd());
        return new ApiResult();
    }

    private void updateUserCache(String deviceId) {
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) return;
        String token = (String) session.getId();
        long userId = user.getId();
        String redisKey = "user-cache." + userId + "@" + deviceId;
        String old = redisOperator.get(redisKey);
        if (old != null && !old.equals(token)) sessionDAO.close(old);
        redisOperator.set(redisKey, token);
    }
}
