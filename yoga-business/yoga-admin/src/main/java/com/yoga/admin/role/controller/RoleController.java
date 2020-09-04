package com.yoga.admin.role.controller;

import com.github.pagehelper.PageInfo;
import com.yoga.admin.role.dto.*;
import com.yoga.admin.role.vo.RoleVo;
import com.yoga.core.base.BaseController;
import com.yoga.core.data.ApiResult;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.operator.role.model.Role;
import com.yoga.operator.role.service.RoleService;
import com.yoga.tenant.menu.MenuItem;
import com.yoga.tenant.menu.MenuLoader;
import com.yoga.tenant.tenant.service.TenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Api(tags = "角色管理")
@RequestMapping("/admin/operator/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private TenantService tenantService;

    @ApiIgnore
    @RequiresPermissions("admin_role")
    @RequestMapping("/list")
    public String roles(ModelMap model, CommonPage page, @Valid RoleListDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageInfo<Role> roles = roleService.list(dto.getTid(), dto.getFilter(), page.getPageIndex(), page.getPageSize());
        model.put("roles", roles.getList());
        model.put("page", new CommonPage(roles));
        model.put("param", dto.wrapAsMap());
        return "/admin/role/roles";
    }
    @ApiIgnore
    @RequiresPermissions("admin_role")
    @RequestMapping("/privileges")
    public String privileges(ModelMap model, @Valid RolePrivilegeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Set<String> permissions = roleService.getPermissions(dto.getTid(), dto.getRoleId());
        String[] modules = tenantService.getModules(dto.getTid());
        Set<String> modules1 = Arrays.stream(modules).collect(Collectors.toSet());
        List<MenuItem> menuItems = MenuLoader.getInstance().getMenuByModule(modules1, false, true);
        List<MenuItem> menuItems1 = tenantService.getMenus(dto.getTid());
        for (MenuItem menuItem1 : menuItems1) {
            MenuItem to = null;
            for (MenuItem menuItem : menuItems) {
                if (menuItem1.getName().equals(menuItem.getName())) {
                    to = menuItem;
                    break;
                }
            }
            if (to == null) {
                menuItems.add(menuItem1);
            } else {
                to.addChild(menuItem1.getChildren());
            }
        }
        Collections.sort(menuItems, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem o1, MenuItem o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        model.put("roleId", dto.getRoleId());
        model.put("permissions", permissions);
        model.put("menuList", menuItems);
        return "/admin/role/permissions";
    }

    @ResponseBody
    @PostMapping("/add.json")
    @RequiresPermissions("admin_role.add")
    @ApiOperation(value = "增加新角色")
    public ApiResult addRole(@Valid @ModelAttribute RoleAddDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        runInLock(RoleService.getLockKey(dto.getTid()), ()-> {
            roleService.add(dto.getTid(), dto.getName(), dto.getRemark());
        });
        return new ApiResult();
    }
    @ResponseBody
    @DeleteMapping("/delete.json")
    @RequiresPermissions("admin_role.del")
    @ApiOperation(value = "删除现有角色")
    public ApiResult delRole(@Valid @ModelAttribute RoleDeleteDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        runInLock(RoleService.getLockKey(dto.getTid()), ()-> {
            roleService.delete(dto.getTid(), dto.getId());
        });
        return new ApiResult();
    }
    @ResponseBody
    @PostMapping("/update.json")
    @RequiresPermissions("admin_role.del")
    @ApiOperation(value = "修改角色信息")
    public ApiResult updateRole(@Valid @ModelAttribute RoleUpdateDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        runInLock(RoleService.getLockKey(dto.getTid()), ()-> {
            roleService.update(dto.getTid(), dto.getId(), dto.getName(), dto.getRemark());
        });
        return new ApiResult();
    }
    @ResponseBody
    @GetMapping("/get.json")
    @RequiresAuthentication
    @ApiOperation(value = "获取角色信息")
    public ApiResult<RoleVo> getRole(@Valid @ModelAttribute RoleGetDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Role role = roleService.get(dto.getTid(), dto.getId());
        return new ApiResult<>(role, RoleVo.class);
    }
    @ResponseBody
    @PostMapping("/privilege.json")
    @RequiresPermissions("admin_role.update")
    @ApiOperation(value = "修改角色拥有的权限")
    public ApiResult setPermissions(@Valid @ModelAttribute RoleSetPrivilegeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        roleService.savePrivilege(dto.getTid(), dto.getId(), dto.getPrivileges());
        return new ApiResult();
    }
}
