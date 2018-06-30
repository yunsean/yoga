package com.yoga.user.role.controller;

import com.yoga.core.controller.BaseWebController;
import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.IllegalArgumentException;
import com.yoga.tenant.setting.Settable;
import com.yoga.tenant.setting.dto.SettingDto;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.admin.menu.MenuItem;
import com.yoga.user.admin.menu.MenuLoader;
import com.yoga.user.role.dto.PermissionDto;
import com.yoga.user.role.model.Role;
import com.yoga.user.role.service.RoleService;
import com.yoga.tenant.tenant.service.TenantService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Settable
@Controller
@RequestMapping("/privilege")
public class RoleWebController extends BaseWebController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private TenantService tenantService;

    @RequiresAuthentication
    @RequestMapping("/roles")
    public String roles(HttpServletRequest request, @Valid TenantDto dto, BindingResult bindingResult, CommonPage
            page, ModelMap model) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        PageList<Role> roles = roleService.allRoles(dto.getTid(), page);
        model.put("roles", roles);
        model.put("page", roles.getPage());
        Map<String, Object> params = new HashMap<>();
        model.put("param", params);
        return "/role/roles";
    }

    @RequiresAuthentication
    @RequestMapping("/permissions")
    public String permissions(HttpServletRequest request, ModelMap model, @Valid PermissionDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) throw new IllegalArgumentException(bindingResult);
        Set<String> permissions = roleService.getPermissions(dto.getTid(), dto.getRoleId());
        String[] modules = tenantService.modules(dto.getTid());
        Set<String> modules1 = new HashSet<>();
        for (String module : modules) {
            modules1.add(module);
        }
        List<MenuItem> menuItems = MenuLoader.getInstance().getMenuByModule(modules1, false, true);
        List<MenuItem> menuItems1 = tenantService.menus(dto.getTid());
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
        model.put("pageIndex", request.getParameter("pageIndex"));
        model.put("permissions", permissions);
        model.put("menuList", menuItems);
        return "/role/permissions";
    }
}
