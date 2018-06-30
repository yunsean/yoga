package com.yoga.user.dept.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.user.basic.TenantPage;
import com.yoga.user.dept.dto.ListDeptDto;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.role.service.RoleService;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.dto.SettingDto;
import com.yoga.tenant.setting.service.SettingService;
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
@Settable(key = DepartmentService.LogonRequireMobile_Key, name = "注册账号必须输入手机号码", module = DepartmentService.Setting_Module_UserLogon, type = boolean.class)
@Settable(key = DepartmentService.MaxDepartmentLevel_key, name = "最大部门层级", module = DepartmentService.Setting_Module, type = int.class, defaultValue = "0")

public class DepartmentWebController extends BaseWebController {

    @Autowired
    private DepartmentService deptService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SettingService settingService;

    @RequiresAuthentication
    @RequestMapping("/depts")
    public String depts(HttpServletRequest request, ModelMap model, TenantPage page, @Valid ListDeptDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, Object> param = new HashMap<>();
        param.put("name", dto.getName());
        model.put("param", param);
        List<Department> depts = deptService.treeOfQuery(dto.getName(), dto.getTid());
        model.put("depts", depts);
        model.put("roles", roleService.roles(dto.getTid()));
        model.put("query", depts);
        model.put("maxLevel", deptService.getMaxLevel(dto.getTid()));
        return "/dept/dept";
    }

    @RequiresAuthentication
    @Settable(key = DepartmentService.DefaultDept_Key, name = "默认注册用户所在部门", module = DepartmentService.Setting_Module_UserLogon, type = Long.class)
    @RequestMapping("/defaultRegDept")
    public String defaultRegDept(HttpServletRequest request, ModelMap model, @Valid SettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Map<String, Object> param = new HashMap<>();
        Long value = deptService.defaultLogonDept(dto.getTid());
        param.put("value", value == null ? 0 : value);
        model.put("param", param);
        model.put("depts", deptService.treeOfAll(dto.getTid()));
        return "/dept/setting";
    }
}

