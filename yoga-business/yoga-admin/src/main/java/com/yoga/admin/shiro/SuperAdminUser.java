package com.yoga.admin.shiro;

import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.property.PropertiesService;
import com.yoga.operator.user.model.User;
import com.yoga.tenant.tenant.mapper.TenantMapper;
import com.yoga.tenant.tenant.model.Tenant;
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
    @Autowired
    private TenantMapper tenantMapper;

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
    public Set<String> getPermissions() {
        return permissions;
    }
    public User getAdminInfo() {
        Tenant tenant = new MapperQuery<>(Tenant.class)
                .andEqualTo("code", "system")
                .queryFirst(tenantMapper);
        User user = new User();
        user.setTenantId(tenant == null ? 0L : tenant.getId());
        user.setBranchId(0L);
        user.setNickname("超级管理员");
        user.setPassword(getPassword());
        user.setUsername(getUsername());
        user.setId(0L);
        return user;
    }

    private static Set<String> permissions = new HashSet<String>() {{
        add("gbl_tenant_template");
        add("gbl_tenant");
        add("admin_role");
        add("admin_branch");
        add("admin_duty");
        add("admin_user");
        add("sys_config");
        add("gbl_tenant_template.add");
        add("gbl_tenant_template.del");
        add("gbl_tenant_template.update");
        add("gbl_tenant.add");
        add("gbl_tenant.del");
        add("gbl_tenant.update");
        add("admin_role.add");
        add("admin_role.del");
        add("admin_role.update");
        add("admin_branch.add");
        add("admin_branch.del");
        add("admin_branch.update");
        add("admin_duty.add");
        add("admin_duty.del");
        add("admin_duty.update");
        add("admin_user.add");
        add("admin_user.del");
        add("admin_user.update");
        add("sys_config.update");
    }};
}
