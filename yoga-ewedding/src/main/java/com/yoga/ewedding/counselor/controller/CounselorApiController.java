package com.yoga.ewedding.counselor.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.ewedding.counselor.dto.*;
import com.yoga.ewedding.counselor.enums.CounselorStatus;
import com.yoga.ewedding.counselor.model.Counselor;
import com.yoga.ewedding.counselor.model.CounselorUser;
import com.yoga.ewedding.counselor.service.CounselorService;
import com.yoga.tenant.setting.Settable;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.captcha.CaptchaService;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.model.LoginUser;
import com.yoga.user.shiro.TenantToken;
import com.yoga.user.user.dto.LoginDto;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Explain(value = "Ew顾问管理", module = "ew_counselor")
@RestController
@EnableAutoConfiguration
@RequestMapping("/api/ewedding/counselor")
@Settable(module = CounselorService.Module_Name, key = CounselorService.IDCardRecognize_Key, name = "审核必须通过身份证识别", type = boolean.class, defaultValue = "true")
public class CounselorApiController extends BaseApiController {

    @Autowired
    private CounselorService counselorService;
    @Autowired
    private CaptchaService captchaService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    @Explain("顾问类型")
    @RequestMapping("/types")
    public CommonResult types(TenantDto dto) {
        Long deptId = counselorService.getDepartmentId(dto.getTid());
        if (deptId == null) throw new BusinessException("当前无法注册");
        List<Department> departments = departmentService.childrenOf(dto.getTid(), deptId);
        return new CommonResult(new MapConverter<Department>((item, map) -> {
            map.set("id", item.getId())
                    .set("name", item.getName())
                    .set("remark", item.getRemark());
        }).build(departments));
    }

    @Explain("顾问注册")
    @RequestMapping("/logon")
    public CommonResult logon(@Valid LogonDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (StrUtil.isNotBlank(dto.getMobile()) && !StrUtil.isMobileNo(dto.getMobile()))
            throw new IllegalArgumentException("请输入正确的手机号！");
        if (StrUtil.isNotBlank(dto.getMobile())) {
            if (!captchaService.verifyCaptcha(dto.getTid(), dto.getMobile(), dto.getUuid(), dto.getCaptcha()))
                throw new IllegalArgumentException("验证码无效或者已过期");
        }
        Counselor counselor = counselorService.add(dto.getTid(), dto.getTypeId(), dto.getMobile(), dto.getPassword(), dto.getLastname(), dto.getFirstname(), dto.getAvatar(), dto.getEmail(), dto.getQq(), dto.getTitle(), dto.getCompany(), dto.getWechat(), dto.getIntro(), dto.getGender());
        User user = userService.getUser(dto.getTid(), counselor.getId());
        MapConverter.MapItem<String, Object> result = buildUserInfo(dto.getTid(), user, counselor);
        result.set("token", counselor.getProveToken());
        return new CommonResult(1, "未通过认证！", result);
    }

    private MapConverter.MapItem<String, Object> buildUserInfo(long tenantId, User user, Counselor counselor) {
        MapConverter.MapItem<String, Object> result = new MapConverter.MapItem<String, Object>()
                .set("pid", counselor.getPid())
                .set("pidFront", counselor.getPidFront())
                .set("pidBack", counselor.getPidBack())
                .set("images", counselor.getImagesList())
                .set("status", counselor.getStatus())
                .set("userId", user.getId())
                .set("username", user.getUsername())
                .set("lastname", user.getLastname())
                .set("firstname", user.getFirstname())
                .set("fullname", user.getFullname())
                .set("avatar", user.getAvatar())
                .set("title", user.getTitle())
                .set("gender", user.getGender())
                .set("phone", user.getPhone())
                .set("email", user.getEmail())
                .set("qq", user.getQq())
                .set("company", user.getCompany())
                .set("wechat", user.getWechat())
                .set("intro", user.getExtText())
                .set("reason", counselor.getRejectReason());
        Department department = departmentService.get(tenantId, user.getDeptId());
        if (department != null) {
            result.set("typeId", department.getId())
                    .set("type", department.getName());
        }
        return result;
    }

    @Explain("顾问登录")
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
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "用户不存在或者用户密码错误");
        }
        User user = userService.getLoginInfo();
        Long deptId = counselorService.getDepartmentId(dto.getTid());
        if (deptId == null) throw new BusinessException("当前无法登录！");
        Department department = departmentService.get(dto.getTid(), user.getDeptId());
        if (department == null || department.getParentId() == null || !department.getParentId().equals(deptId)) {
            subject.logout();
            throw new BusinessException("请使用顾问账号登录！");
        }
        Counselor counselor = counselorService.get(dto.getTid(), user.getId(), true);
        if (counselor.getStatus() != CounselorStatus.accepted) {
            subject.logout();
            MapConverter.MapItem<String, Object> result = buildUserInfo(dto.getTid(), user, counselor);
            result.set("token", counselor.getProveToken());
            return new CommonResult(1, "未通过认证！", result);
        } else if (user.getExpire() != null && user.getExpire().before(new Date())) {
            subject.logout();
            return new CommonResult(ResultConstants.ERROR_FORBIDDEN, "账号已过期！");
        } else{
            userService.updateUserCache(dto.getDeviceId());
            MapConverter.MapItem<String, Object> result = buildUserInfo(dto.getTid(), user, counselor);
            result.put("token", subject.getSession().getId());
            result.put("permissions", userService.getPermissions(user));
            return new CommonResult(result);
        }
    }

    @Explain("提交认证资料")
    @RequestMapping("/prove")
    public CommonResult prove(@Valid ProveDto dto, BindingResult bindingResult) throws org.apache.commons.httpclient.auth.AuthenticationException {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        CounselorStatus status = counselorService.prove(dto.getTid(), dto.getToken(), dto.getPid(), dto.getPidFront(), dto.getPidBack(), dto.getImages());
        return new CommonResult(status);
    }
    @Explain("修改审核信息")
    @RequestMapping("/update")
    public CommonResult update(@Valid UpdateDto dto, BindingResult bindingResult) throws org.apache.commons.httpclient.auth.AuthenticationException {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        counselorService.update(dto.getTid(), dto.getToken(), dto.getTypeId(), dto.getLastname(), dto.getFirstname(), dto.getAvatar(), dto.getEmail(), dto.getQq(), dto.getTitle(), dto.getCompany(), dto.getWechat(), dto.getIntro(), dto.getGender());
        return new CommonResult();
    }

    @Explain(exclude = true)
    @RequiresPermissions("pri_user.update")
    @RequestMapping("/info")
    public CommonResult info(@Valid InfoDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        CounselorUser user = counselorService.get(dto.getTid(), dto.getId());
        return new CommonResult(user);
    }

    @Explain(exclude = true)
    @RequiresPermissions("pri_user.update")
    @RequestMapping("/verify")
    public CommonResult verify(@Valid VerifyDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.isRejected() && StrUtil.isBlank(dto.getReason())) throw new BusinessException("请填写拒绝原因！");
        counselorService.verify(dto.getTid(), dto.getId(), dto.isRejected(), dto.getReason());
        return new CommonResult();
    }

    @Explain("顾问列表")
    @RequiresAuthentication
    @RequestMapping("/list")
    public CommonResult list(TenantPage page, @Valid ListDto2 dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<CounselorUser> counselors = counselorService.list(dto.getTid(), dto.getTypeId(), CounselorStatus.accepted, dto.getName(), dto.getCompany(), page.getPageIndex(), page.getPageSize());
        return new CommonResult(new MapConverter<CounselorUser>((item, map) -> {
            map.set("id", item.getId())
                    .set("name", item.getFullname())
                    .set("mobile", item.getPhone())
                    .set("gender", item.getGender())
                    .set("type", departmentService.getName(dto.getTid(), item.getDeptId()))
                    .set("avatar", item.getAvatar())
                    .set("company", item.getCompany())
                    .set("intro", item.getExtText());
        }).build(counselors), counselors.getPage());
    }

    @Explain("顾问详情")
    @RequiresAuthentication
    @RequestMapping("/get")
    public CommonResult get(TenantPage page, @Valid GetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        CounselorUser counselor = counselorService.get(dto.getTid(), dto.getId());
        if (counselor.getStatus() != CounselorStatus.accepted) throw new BusinessException("未找到顾问信息！");
        return new CommonResult(new MapConverter<CounselorUser>((item, map) -> {
            map.set("id", item.getId())
                    .set("name", item.getFullname())
                    .set("mobile", item.getPhone())
                    .set("email", item.getEmail())
                    .set("qq", item.getQq())
                    .set("wechat", item.getWechat())
                    .set("gender", item.getGender())
                    .set("type", departmentService.getName(dto.getTid(), item.getDeptId()))
                    .set("avatar", item.getAvatar())
                    .set("company", item.getCompany())
                    .set("title", item.getTenantId())
                    .set("intro", item.getExtText());
        }).build(counselor));
    }

    @Explain("个人信息")
    @RequiresAuthentication
    @RequestMapping("/info/self")
    public CommonResult info(LoginUser user, TenantDto dto) {
        CounselorUser counselor = counselorService.get(dto.getTid(), user.getId());
        return new CommonResult(new MapConverter<CounselorUser>((item, map) -> {
            map.set("id", item.getId())
                    .set("name", item.getFullname())
                    .set("mobile", item.getPhone())
                    .set("email", item.getEmail())
                    .set("qq", item.getQq())
                    .set("wechat", item.getWechat())
                    .set("gender", item.getGender())
                    .set("type", departmentService.getName(dto.getTid(), item.getDeptId()))
                    .set("avatar", item.getAvatar())
                    .set("company", item.getCompany())
                    .set("title", item.getTenantId())
                    .set("intro", item.getExtText())
                    .set("expire", item.getExpire());
        }).build(counselor));
    }

    @Explain("获取账号状态")
    @RequiresAuthentication
    @RequestMapping("/info/expire")
    public CommonResult expire(LoginUser user, TenantDto dto) {
        CounselorUser counselor = counselorService.get(dto.getTid(), user.getId());
        final boolean valid = counselor.getExpire() != null && counselor.getExpire().after(new Date());
        return new CommonResult(new MapConverter<CounselorUser>((item, map) -> {
            map.set("valid", valid)
                    .set("expire", item.getExpire());
        }).build(counselor));
    }

    @Explain("顾问信息同步")
    @RequiresAuthentication
    @RequestMapping("/sync")
    public CommonResult sync(TenantPage page, @Valid SyncDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<CounselorUser> counselors = counselorService.findByAboveId(dto.getTid(), dto.getVersion());
        return new CommonResult(new MapConverter<CounselorUser>((item, map) -> {
            map.set("id", item.getId())
                    .set("name", item.getFullname())
                    .set("mobile", item.getPhone())
                    .set("gender", item.getGender())
                    .set("type", departmentService.getName(dto.getTid(), item.getDeptId()))
                    .set("avatar", item.getAvatar())
                    .set("company", item.getCompany())
                    .set("intro", item.getExtText());
        }).build(counselors));
    }
}
