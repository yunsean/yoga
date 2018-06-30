package com.yoga.imessager.user.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.imessager.group.dto.FindDto;
import com.yoga.imessager.group.model.Group;
import com.yoga.imessager.group.service.GroupService;
import com.yoga.imessager.rongcloud.service.RongService;
import com.yoga.imessager.user.dto.*;
import com.yoga.imessager.user.model.User;
import com.yoga.imessager.user.service.SystemUserService;
import com.yoga.imessager.user.service.UserService;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.model.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Explain(value = "IM用户管理", module = "gcf_imessager")
@RestController(value = "imUserApiController")
@RequestMapping("/api/im/user")
public class UserApiController extends BaseApiController{

    @Autowired
    private RongService rongService;
    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private DepartmentService departmentService;

    @Explain("获取Token")
    @RequestMapping("/token")
    public CommonResult getToken(TenantDto dto) throws Exception {
        if (!SecurityUtils.getSubject().isAuthenticated() || SecurityUtils.getSubject().getSession() == null) new CommonResult(ResultConstants.ERROR_UNAUTHENTICAED, "请登录");
        LoginUser user = new LoginUser();
        String name = StrUtil.isBlank(user.getNickname()) ? user.getUsername() : user.getNickname();
        String token = rongService.token(dto.getTid(), String.valueOf(user.getId()), name, user.getAvatar());
        return new CommonResult(token);
    }

    @Explain("获取个人信息")
    @RequiresAuthentication
    @RequestMapping("/info")
    public CommonResult getInfo(HttpServletRequest request, LoginUser loginUser, @Valid InfoDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        long userId = dto.getId() == null ? loginUser.getId() : dto.getId();
        User user = userService.get(dto.getTid(), userId);
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<User>() {
            @Override
            public void convert(User item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("nickname", item.getNickname())
                        .set("avatar", item.getAvatar());
            }
        }).build(user));
    }

    @Explain("更新个人信息")
    @RequiresAuthentication
    @RequestMapping("/update")
    public CommonResult updateInfo(HttpServletRequest request, LoginUser loginUser, @Valid UpdateDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        userService.update(dto.getTid(), loginUser.getId(), dto.getNickname(), dto.getAvatar());
        return new CommonResult();
    }


    @Explain("查找用户信息")
    @RequiresAuthentication
    @RequestMapping("/find")
    public CommonResult findUser(HttpServletRequest request, TenantPage page, @Valid FindDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Page<User> users = userService.find(dto.getTid(), dto.getName(), page.getPageIndex(), page.getPageSize());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<User>() {
            @Override
            public void convert(User item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("nickname", item.getNickname())
                        .set("avatar", item.getAvatar());
            }
        }).build(users), users);
    }

    @Explain("查找系统用户")
    @RequiresAuthentication
    @RequestMapping("/find/system")
    public CommonResult findSystemUser(HttpServletRequest request, TenantPage page, @Valid FindSystemDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<com.yoga.user.user.model.User> users = systemUserService.findUsers(dto.getTid(), dto.getGroupId(), dto.getNickname(), dto.getDeptId(), page.getPageIndex(), page.getPageSize());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<com.yoga.user.user.model.User>() {
            @Override
            public void convert(com.yoga.user.user.model.User item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("username", item.getUsername())
                        .set("firstname", item.getFirstname())
                        .set("lastname", item.getLastname())
                        .set("fullname", item.getFullname())
                        .set("email", item.getEmail())
                        .set("qq", item.getQq())
                        .set("mobile", item.getPhone())
                        .set("avatar", item.getAvatar())
                        .set("title", item.getTitle());
                Department department = departmentService.get(dto.getTid(), item.getDeptId());
                if (department != null) {
                    map.set("departmentId", department.getId())
                            .set("department", department.getName());
                }
            }
        }).build(users), users.getPage());
    }

    @Explain("获取群组列表")
    @RequiresAuthentication
    @RequestMapping("/groups")
    public CommonResult getGoups(HttpServletRequest request, LoginUser loginUser, @Valid TenantDto dto, BindingResult bindingResult) throws Exception{
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (groupService.everyOneGroup(dto.getTid())) {
            groupService.ensureUserInEveryOneGroup(dto.getTid(), loginUser);
        }
        List<Group> groups = userService.groups(dto.getTid(), loginUser.getId());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<Group>() {
            @Override
            public void convert(Group item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("name", item.getName())
                        .set("avatar", item.getAvatar())
                        .set("memberCount", item.getMemberCount())
                        .set("createTime", item.getCreateTime())
                        .set("creator", item.getCreator())
                        .set("creatorId", item.getCreatorId());
            }
        }).build(groups));
    }

    @Explain("申请加入群组")
    @RequiresAuthentication
    @RequestMapping("/apply")
    public CommonResult applyJoin(HttpServletRequest request, LoginUser login, @Valid ApplyDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.apply(dto.getTid(), dto.getId(), login.getId(), login.getNickname(), login.getAvatar(), dto.getMessage());
        return new CommonResult();
    }

    @Explain("退出群组")
    @RequiresAuthentication
    @RequestMapping("/quit")
    public CommonResult quitGroup(HttpServletRequest request, LoginUser loginUser, @Valid QuitDto dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        groupService.quit(dto.getTid(), dto.getId(), loginUser.getId());
        return new CommonResult();
    }
}
