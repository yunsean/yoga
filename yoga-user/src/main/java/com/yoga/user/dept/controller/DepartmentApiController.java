package com.yoga.user.dept.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.data.CommonResult;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.core.utils.MapConverter;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.dept.dto.*;
import com.yoga.user.dept.model.Department;
import com.yoga.user.dept.service.DepartmentService;
import com.yoga.user.role.service.RoleService;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.service.SettingService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Settable
@Controller
@RequestMapping("/api/dept")
@Explain(value = "部门管理", module = "pri_dept")
public class DepartmentApiController extends BaseApiController {

    @Autowired
    private DepartmentService deptService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private SettingService settingService;

    @Explain("获取部门列表")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping("/list")
    public CommonResult allDepts(HttpServletRequest request, @Valid TenantDto tenantDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Iterable<Department> departments = deptService.getDeptDictionaryApp(tenantDto.getTid());
        return new CommonResult(new MapConverter<>(new MapConverter.Converter<Department>() {
            @Override
            public void convert(Department item, MapConverter.MapItem<String, Object> map) {
                map.set("id", item.getId())
                        .set("parentId", item.getParentId())
                        .set("name", item.getName())
                        .set("code", item.getCode());
            }
        }).build(departments));
    }

    private static List<Map<String, Object>> convert(Collection<Department> departments) {
        if (departments == null || departments.size() < 1) return null;
        return new MapConverter<Department>((item, map) -> {
            map.set("id", item.getId())
                    .set("parentId", item.getParentId())
                    .set("name", item.getName())
                    .set("code", item.getCode())
                    .set("children", DepartmentApiController.convert(item.getChildren()));
        }).build(departments);
    }

    @Explain("获取部门树")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping("/children")
    public CommonResult children(HttpServletRequest request, @Valid TreeDeptDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List<Department> departments = dto.getParentId() == 0 ? deptService.treeOfAll(dto.getTid()) : deptService.treeOfParent(dto.getTid(), dto.getParentId());
        return new CommonResult(convert(departments));
    }

    @Explain("获取部门拥有的角色")
    @RequiresAuthentication
    @ResponseBody
    @RequestMapping("/roles")
    public CommonResult getDeptRoles(HttpServletRequest request, @Valid GetDeptRolesDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        List deptRoles = deptService.getDeptRoles(dto.getTid(), dto.getId());
        return new CommonResult(deptRoles);
    }

    @Explain("增加新部门")
    @ResponseBody
    @RequestMapping("/add")
    @RequiresPermissions("pri_dept.add")
    public CommonResult addDept(HttpServletRequest request, @Valid AddDeptDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        deptService.add(dto);
        return new CommonResult();
    }

    @Explain("删除已有部门")
    @ResponseBody
    @RequestMapping("/delete")
    @RequiresPermissions("pri_dept.del")
    public CommonResult delDept(HttpServletRequest request, @Valid DelDeptDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        deptService.delete(dto);
        return new CommonResult();
    }

    @Explain("修改部门信息")
    @ResponseBody
    @RequestMapping("/update")
    @RequiresPermissions("pri_dept.update")
    public CommonResult updateDept(HttpServletRequest request, @Valid UpdateDeptDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        deptService.update(dto);
        return new CommonResult();
    }

    @Explain("修改部门设置")
    @ResponseBody
    @RequiresAuthentication
    @RequiresPermissions("gbl_settable.update")
    @RequestMapping("/setting/save")
    public CommonResult upsertDefaultRegDept(HttpServletRequest request, @Valid SaveSettingDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        deptService.setDefaultLogonDept(dto.getTid(), dto.getDeptId(), dto.getDeptName());
        return new CommonResult();
    }
}

