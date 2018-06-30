package com.yoga.user.role.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.user.role.dto.AddRoleDto;
import com.yoga.user.role.dto.DelRoleDto;
import com.yoga.user.role.dto.SaveSettingDto;
import com.yoga.user.role.dto.SetPermissionDto;
import com.yoga.user.role.service.RoleService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Explain(value = "角色管理", module = "pri_role")
@Controller
@RequestMapping("/api/role")
public class RoleApiController extends BaseApiController {

    @Autowired
    private RoleService roleService;

    @Explain("增加新角色")
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions("pri_role.add")
    public CommonResult addRole(HttpServletRequest request, @Valid AddRoleDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        roleService.addRole(dto);
        return new CommonResult();
    }

    @Explain("删除现有角色")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("pri_role.del")
    public CommonResult delRole(HttpServletRequest request, @Valid DelRoleDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        roleService.delRole(dto.getTid(), dto.getId());
        return new CommonResult();
    }

    @Explain("修改角色拥有的权限")
    @ResponseBody
    @RequestMapping("/permission/set")
    @RequiresPermissions("pri_role.update")
    public CommonResult setPermissions(HttpServletRequest request, @Valid SetPermissionDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        roleService.savePrivilege(dto.getTid(), dto.getRoleId(), dto.getPrivileges());
        return new CommonResult();
    }

    @Explain("修改注册团队角色设置")
    @ResponseBody
    @RequiresAuthentication
    @RequiresPermissions("gbl_settable.update")
    @RequestMapping("/setting/save")
    public CommonResult upsertDefaultRegDept(HttpServletRequest request, @Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        roleService.setDefaultLogonRole(dto.getTid(), dto.getRoleId(), dto.getRoleName());
        return new CommonResult();
    }
    @Explain("修改通过团队角色设置")
    @ResponseBody
    @RequiresAuthentication
    @RequiresPermissions("gbl_settable.update")
    @RequestMapping("/setting/pass/save")
    public CommonResult upsertDefaultPassDept(HttpServletRequest request, @Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        roleService.setDefaultPassRole(dto.getTid(), dto.getRoleId(), dto.getRoleName());
        return new CommonResult();
    }
}
