package com.yoga.user.user.controller;


import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.property.PropertiesService;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.duty.cache.DutyCache;
import com.yoga.user.duty.service.DutyService;
import com.yoga.user.role.service.RoleService;
import com.yoga.user.user.dto.ListDto;
import com.yoga.user.user.dto.UserInfoDto;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/privilege")
public class UserWebController extends BaseWebController {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DutyService dutyService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DutyCache dutyCache;
    @Autowired
    private PropertiesService propertiesService;

    @RequiresAuthentication
    @RequestMapping("/users")
    public String users(HttpServletRequest request, ModelMap model, TenantPage page, @Valid ListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<User> users = userService.findUsers(dto.getTid(), dto.getName(), dto.getDeptId(), dto.getDutyId(), page.getPageIndex(), page.getPageSize());
        List<Department> depts = departmentService.treeOfAll(dto.getTid());
        if (depts != null && depts.size() > 0) {
            depts.add(0, new Department(dto.getTid(), 0, "未指定"));
        }
        model.put("depts", depts);
        model.put("duties", dutyCache.getDuties(dto.getTid()));
        model.put("getDutyMap", dutyCache.dutyMap(dto.getTid()));
        model.put("deptMap", departmentService.getDeptMap(dto.getTid()));
        model.put("dutyMap", dutyService.getDutyMap(dto.getTid()));
        model.put("roles", roleService.roles(dto.getTid()));
        model.put("users", users);
        model.put("page", users.getPage());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", dto.getName());
        params.put("deptId", dto.getDeptId());
        params.put("dutyId", dto.getDutyId());
        model.put("param", params);
        model.put("uploadPath", propertiesService.getZuiUploadUrl());
        return "/user/users";
    }

    @RequiresAuthentication
    @RequestMapping("/userInfo")
    public String userInfo(HttpServletRequest request, ModelMap model, @Valid UserInfoDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Department> depts = departmentService.treeOfAll(dto.getTid());
        if (depts != null && depts.size() > 0) {
            depts.add(0, new Department(dto.getTid(), 0, "未指定"));
        }
        model.put("depts", depts);
        User user = userService.getUserInfo(dto.getId());
        model.put("userRoles", userService.getRoles(dto.getTid(), dto.getId()));
        model.put("deptMap", departmentService.getDeptMap(dto.getTid()));
        model.put("dutyMap", dutyService.getDutyMap(dto.getTid()));
        model.put("roles", roleService.roles(dto.getTid()));
        model.put("user", user);
        model.put("pageIndex", dto.getPageIndex());
        return "/user/userInfo";
    }
}
