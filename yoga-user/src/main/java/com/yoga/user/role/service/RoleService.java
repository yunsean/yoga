package com.yoga.user.role.service;

import com.yoga.core.data.CommonPage;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.tenant.setting.service.SettingService;
import com.yoga.user.role.cache.RoleCache;
import com.yoga.user.role.dao.PermissionDAO;
import com.yoga.user.role.dto.AddRoleDto;
import com.yoga.user.role.model.Role;
import com.yoga.user.role.repository.RoleRepository;
import com.yoga.user.sequence.SequenceNameEnum;
import com.yoga.user.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class RoleService extends BaseService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionDAO permissionDAO;
    @Autowired
    private SettingService settingService;

    @Autowired
    private RoleCache roleCache;


    public void addRole(AddRoleDto addRole) {
        if (roleRepository.countByTenantIdAndName(addRole.getTid(), addRole.getName()) > 0) {
            throw new BusinessException("已经存在同名角色！");
        }
        Role role = new Role();
        role.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_S_ROLE_ID));
        role.setTenantId(addRole.getTid());
        role.setName(addRole.getName());
        role.setRemark(addRole.getRemark());
        Date date = new Date();
        role.setCode("" + date.getTime());
        role.setCreateTime(date);
        role.setDisabled(false);
        roleRepository.save(role);
        roleCache.clearCache(addRole.getTid());
    }

    @Transactional
    public void delRole(long tenantId, long roleId) {
        if (permissionDAO.getRoleUsedCount(tenantId, roleId) > 0) {
            throw new BusinessException("该角色拥有授权对象,无法删除");
        }
        roleRepository.deleteByTenantIdAndRoleId(tenantId, roleId);
        roleCache.clearCache(tenantId);
    }

    public PageList<Role> allRoles(long tenantId, CommonPage commonPage) {
        Page<Role> roles = roleRepository.findAllByTenantId(tenantId, commonPage.asPageRequest());
        return new PageList<Role>(roles);
    }

    public List<Role> roles(long tenantId) {
        return roleCache.getRoles(tenantId);
    }

    @Transactional
    public void savePrivilege(long tenantId, long roleId, String[] permissions) {
        permissionDAO.setRolePermissions(tenantId, roleId, permissions);
        clearShiroSessionCache();
    }
    public void clearShiroSessionCache() {
        redisOperator.remove(RedisSessionDAO.key);
    }
    public Set<String> getPermissions(long tenantId, long roldId) {
        return permissionDAO.getRolePermissions(tenantId, roldId);
    }

    public final static String DefaultRole_Key = "defaultRegRole";

    public final static String Setting_Module_UserLogon = "gcf_user_logon";

    public final static String DefaultPassRole_Key = "defaultPassRole";


    public Long defaultLogonRole(long tenantId) {
        Number number = settingService.get(tenantId, Setting_Module_UserLogon, DefaultRole_Key, Long.class);
        if (number == null) return null;
        return number.longValue();
    }
    public void setDefaultLogonRole(long tenantId, Long roleId, String roleName) {
        settingService.save(tenantId, Setting_Module_UserLogon, DefaultRole_Key, String.valueOf(roleId), roleName);
    }
    public Long defaultPassRole(long tenantId) {
        Number number = settingService.get(tenantId, Setting_Module_UserLogon, DefaultPassRole_Key, Long.class);
        if (number == null) return null;
        return number.longValue();
    }
    public void setDefaultPassRole(long tenantId, Long roleId, String roleName) {
        settingService.save(tenantId, Setting_Module_UserLogon, DefaultPassRole_Key, String.valueOf(roleId), roleName);
    }

    public List<Role> list(long tenantId){
        return roleRepository.findAllByTenantId(tenantId);
    }


}
