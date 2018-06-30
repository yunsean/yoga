package com.yoga.user.user.controller;


import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonMessage;
import com.yoga.core.data.CommonResult;
import com.yoga.core.data.PageList;
import com.yoga.core.data.ResultConstants;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.core.utils.StrUtil;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.duty.model.Duty;
import com.yoga.user.duty.service.DutyService;
import com.yoga.user.user.dto.*;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserDataService;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

@Explain(value = "用户管理", module = "pri_user")
@Controller
@RequestMapping("/api/user")
public class UserApiController extends BaseApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DutyService dutyService;

    @RequiresAuthentication
    @Explain("修改密码")
    @RequestMapping(value = "/passwd")
    @ResponseBody
    public CommonResult password(HttpServletRequest request, @Valid PasswdDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        if (user == null) return new CommonResult(ResultConstants.ERROR_BUSINESSERROR, "请登录！");
        userService.modifyPassword(user.getId(), dto.getOldPwd(), dto.getNewPwd());
        return new CommonResult();
    }

    @Explain("修改用户信息")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping(value = "/update")
    public CommonResult updateInfo(HttpServletRequest request, @Valid UpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getSession().getAttribute("user");
        if (dto.getId() != null && dto.getId() != user.getId() && !subject.isPermitted("pri_user.update")) throw new UnauthorizedException("您没有修改用户信息的权限");
        if (dto.getId() == null || !subject.isPermitted("pri_user.update")) {  //通过APP端无权修改权限相关信息
            dto.setId(user.getId());
            dto.setDeptId(null);
            dto.setDutyId(null);
            dto.setRoleIds(null);
        }
        if (StrUtil.isNotBlank(dto.getPassword()) && !subject.isPermitted("pri_user.update")) {
            throw new BusinessException("您无权修改别人的密码！");
        }
        if (StrUtil.allBlank(dto.getAvatar(), dto.getUsername(), dto.getPassword(), dto.getFirstname(), dto.getLastname(),
                dto.getPhone(), dto.getEmail(), dto.getQq(), dto.getTitle(), dto.getAddress(), dto.getPostcode(),
                dto.getWechat(), dto.getExtText()) &&
                dto.getDeptId() == null && dto.getDutyId() == null && (dto.getRoleIds() == null || dto.getRoleIds().length < 1)) {
            return new CommonResult(ResultConstants.ERROR_MISSINGPARAM, "至少包含一个需要修改的信息");
        }
        user = userService.updateUser(dto.getTid(),dto.getBirthday(),dto.getId(), dto.getUsername(), dto.getPassword(), dto.getFirstname(), dto.getLastname(), dto.getAvatar(),
                dto.getEmail(), dto.getQq(), dto.getPhone(), dto.getTitle(), dto.getAddress(),
                dto.getPostcode(), dto.getCompany(), dto.getWechat(), dto.getPhone(), dto.getExtLong(), dto.getExtText(), dto.getExtInt(),
                dto.getExtDouble(), dto.getDeptId(), dto.getDutyId(), dto.getGender(), dto.getRoleIds());
        if (dto.getId() == null || dto.getId() == user.getId()) {
            SecurityUtils.getSubject().getSession().setAttribute("user", user);
            Map<String, Object> map = new MapConverter<User>(new MapConverter.Converter<User>() {
                @Override
                public void convert(User item, MapConverter.MapItem<String, Object> map) {
                    map.set("userId", item.getId())
                            .set("username", StrUtil.lowerCase(item.getUsername()))
                            .set("lastname", item.getLastname())
                            .set("firstname", item.getFirstname())
                            .set("fullname", item.getFullname())
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
        } else {
            return new CommonResult();
        }
    }

    @Explain("获取登录用户拥有的权限")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping(value = "/permissions")
    public CommonResult permissions(HttpServletRequest request) {
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        return new CommonResult(userService.getPermissions(user));
    }

    @Explain("获取登录用户的信息")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping(value = "/info")
    public CommonResult userInfo(HttpServletRequest request, InfoDto dto) {
        User user = userService.getLoginInfo();
        if (dto.getId() != null && dto.getId() != user.getId()) {
            Subject subject = SecurityUtils.getSubject();
            if (!subject.isPermitted("pri_user.update")) {
                throw new BusinessException("您无权查看他人信息！");
            }
            user = userService.getUser(dto.getTid(), dto.getId());
        }
        if (dto.getId() == null) dto.setId(user.getId());
        Map<String, Object> map = new MapConverter<User>(new MapConverter.Converter<User>(){
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
                        .set("roles", userService.getRoles(dto.getTid(), dto.getId()))

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
        return new CommonResult(map);
    }

    @Explain("查询用户列表")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping("/list")
    public CommonResult users(HttpServletRequest request, TenantPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<User> users = userService.findUsers(dto.getTid(), dto.getName(), dto.getDeptId(), dto.getDutyId(), page.getPageIndex(), page.getPageSize());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<User>() {
            @Override
            public void convert(User item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("username", StrUtil.lowerCase(item.getUsername()))
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

    @RequiresAuthentication
    @Explain("新建用户（通过权限新建）")
    @ResponseBody
    @RequestMapping("/add")
    public CommonResult addUser(HttpServletRequest request, @Valid AddDto user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (user.getFirstname() == "" && user.getLastname() == "") throw new IllegalArgumentException("姓或名请至少输入一个");
        if (user.getUsername() == "") throw new IllegalArgumentException("用户名不能为空");
        userService.addUser(user);
        return new CommonResult(ResultConstants.RESULT_OK, CommonMessage.ADDITION_SUCCESS);
    }

    @RequiresAuthentication
    @Explain("删除现有用户")
    @ResponseBody
    @RequestMapping("/delete")
    public CommonResult deleteUser(HttpServletRequest request, @Valid DeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        userService.deleteUser(dto.getTid(), dto.getId());
        return new CommonResult(ResultConstants.RESULT_OK, CommonMessage.DELETE_SUCCESS);
    }

    @RequiresAuthentication
    @Explain("使能/禁用用户")
    @ResponseBody
    @RequestMapping("/enable")
    public CommonResult enableUser(HttpServletRequest request, @Valid EnableDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getId() <= 0) {
            return new CommonResult(ResultConstants.ERROR_ILLEGALPARAM, "用户Id错误");
        }
        if (userService.disableUser(dto.getEnable() == 0, dto.getId())) {
            return new CommonResult(ResultConstants.RESULT_OK, CommonMessage.UPDATE_SUCCESS);
        }
        return new CommonResult(ResultConstants.ERROR_BUSINESSERROR, CommonMessage.UPDATE_FAIL);
    }

    @Explain("获取具有特定权限的用户列表")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping("/permission/list")
    public CommonResult usersOfPermission(HttpServletRequest request, @Valid OfPermissionDto dto, BindingResult bindingResult) {
        if (dto.getOp() == null) dto.setOp(OfPermissionDto.Op.AND);
        PageList<User> users = userService.getUserHasPrivilege(dto.getTid(), dto.getPermissions(), dto.getOp().getCode(), dto.getPageIndex(), dto.getPageSize());
        ArrayList<Map<String, Object>> result = new MapConverter<>(new MapConverter.Converter<User>() {
            @Override
            public void convert(User item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("username", StrUtil.lowerCase(item.getUsername()))
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
        }).build(users);
        return new CommonResult(result, users.getPage());
    }

    @Explain("设置用户自定义数据")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping(value = "/userdata/set")
    public CommonResult setUserData(HttpServletRequest request, @Valid SetUserDataDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        if (dto.getValue() == null) dto.setValue("");
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        userDataService.setUserData(dto.getTid(), user.getId(), dto.getKey(), dto.getValue());
        return new CommonResult();
    }

    @Explain("获取用户自定义数据")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping(value = "/userdata/get")
    public CommonResult getUserData(HttpServletRequest request, @Valid GetUserDataDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
        String value = userDataService.getUserData(dto.getTid(), user.getId(), dto.getKey(), dto.isOptional());
        return new CommonResult(value);
    }
}
