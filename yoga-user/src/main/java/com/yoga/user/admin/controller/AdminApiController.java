package com.yoga.user.admin.controller;

import com.yoga.core.annotation.Explain;
import com.yoga.core.controller.BaseApiController;
import com.yoga.core.utils.AssertUtil;
import com.yoga.user.admin.menu.MenuItem;
import com.yoga.user.admin.menu.MenuLoader;
import com.yoga.user.basic.TenantDto;
import com.yoga.user.role.dao.PermissionDAO;
import com.yoga.user.shiro.SuperAdminUser;
import com.yoga.tenant.tenant.model.TenantSetting;
import com.yoga.tenant.tenant.service.TenantService;
import com.yoga.user.user.model.User;
import com.yoga.user.user.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;


@Explain(exclude = true)
@Controller
@EnableAutoConfiguration
@RequestMapping("/admin")
public class AdminApiController extends BaseApiController {

    @Autowired
    private SuperAdminUser superAdminUser;
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private TenantService tenantService;

    @ResponseBody
    @RequiresAuthentication
    @RequestMapping(value = "/findMenus")
    public Map<String, Object> getMenus(TenantDto dto) {
        Map<String, Object> result = new HashMap<>();
        User user = userService.getLoginInfo();
        AssertUtil.notNull(user);
        Set<String> permissions;
        if (superAdminUser.isAdmin(user.getUsername())) {
            permissions = superAdminUser.getPermissions();
        } else {
            permissions = permissionDAO.getUserPermissions(user);
        }
        Set<String> modules = new HashSet<>();
        Set<String> menus = new HashSet<>();
        for (String permission : permissions) {
            int index = permission.indexOf('.');
            if (index > 0) {
                modules.add(permission.substring(0, index));
            } else {
                menus.add(permission);
                modules.add(permission);
            }
        }
        List<MenuItem> menuItems = MenuLoader.getInstance().getMenuByModule(modules, true, true);
        List<MenuItem> validItems = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getChildren() == null) continue;
            List<MenuItem> children = new ArrayList<>();
            for (MenuItem child : menuItem.getChildren()) {
                if (menus.contains(child.getCode())) {
                    children.add(child);
                }
            }
            if (children.size() > 0) {
                menuItem.setChildren(children);
                validItems.add(menuItem);
            }
        }
        menuItems = validItems;
        if (!superAdminUser.isAdmin(user.getUsername())) {
            List<MenuItem> menuItems1 = permissionDAO.getRoleTenantMenu(user);
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
        }
        //特殊处理了角色 部门和职务三个字段
        TenantSetting tenantSetting = tenantService.getSetting(dto.getTid());
        if (tenantSetting != null) {
            renameRoleAndDeptAndDuty(menuItems, tenantSetting);
        }
        Collections.sort(menuItems, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem o1, MenuItem o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getChildren() != null) {
                Collections.sort(menuItem.getChildren(), new Comparator<MenuItem>() {
                    @Override
                    public int compare(MenuItem o1, MenuItem o2) {
                        return o1.getSort() - o2.getSort();
                    }
                });
            }
        }
        result.put("modules", menuItems);
        return result;
    }

    private void renameRoleAndDeptAndDuty(List<MenuItem> menuItems, TenantSetting setting) {
        for (MenuItem menuItem : menuItems) {
            if ("pri_role".equals(menuItem.getCode())) menuItem.setName(setting.getRoleAlias() + "管理");
            else if ("pri_dept".equals(menuItem.getCode())) menuItem.setName(setting.getDeptAlias() + "管理");
            else if ("pri_duty".equals(menuItem.getCode())) menuItem.setName(setting.getDutyAlias() + "管理");
            if (menuItem.getChildren() != null) {
                renameRoleAndDeptAndDuty(menuItem.getChildren(), setting);
            }
        }
    }
}
