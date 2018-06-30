package com.yoga.user.shiro;

import com.yoga.core.property.PropertiesService;
import com.yoga.user.user.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SuperAdminUser {

    @Autowired
    private PropertiesService propertiesService;

    public String getUsername() {
        return propertiesService.getAdminSuperUser();
    }
    public String getPassword() {
        return propertiesService.getAdminSuperPwd();
    }

    public boolean isAdmin(String username) {
        if (username == null) return false;
        return getUsername().equals(username.toUpperCase());
    }

    public boolean passwordMatches(String password) {
        return getPassword().equals(DigestUtils.md5Hex(password));
    }
    public SimpleAuthorizationInfo assembleAdminInfo() {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(getUsername());
        info.addStringPermissions(getPermissions());
        return info;
    }
    public Set<String> getPermissions() {
        return permissions;
    }
    public User getAdminInfo() {
        User user = new User();
        user.setTenantId(0L);
        user.setDeptId(0L);
        user.setFullname("超级管理员");
        user.setPassword(getPassword());
        user.setUsername(getUsername());
        user.setId(0);
        return user;
    }

    private static Set<String> permissions = new HashSet<String>() {{
        add("gbl_tenant_template");
        add("gbl_tenant");
        add("pri_role");
        add("pri_dept");
        add("pri_duty");
        add("pri_user");
        add("gbl_settable");
        add("gbl_tenant_template.add");
        add("gbl_tenant_template.del");
        add("gbl_tenant_template.update");
        add("gbl_tenant.add");
        add("gbl_tenant.del");
        add("gbl_tenant.update");
        add("pri_role.add");
        add("pri_role.del");
        add("pri_role.update");
        add("pri_dept.add");
        add("pri_dept.del");
        add("pri_dept.update");
        add("pri_duty.add");
        add("pri_duty.del");
        add("pri_duty.update");
        add("pri_user.add");
        add("pri_user.del");
        add("pri_user.update");
        add("pri_manage.login");
        add("gbl_settable.update");
    }};
}
