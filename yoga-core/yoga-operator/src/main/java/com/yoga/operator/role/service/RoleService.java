package com.yoga.operator.role.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.core.base.BaseService;
import com.yoga.core.data.CommonPage;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import com.yoga.operator.branch.service.BranchService;
import com.yoga.operator.role.cache.RoleCache;
import com.yoga.operator.role.mapper.AccreditMapper;
import com.yoga.operator.role.mapper.PrivilegeMapper;
import com.yoga.operator.role.mapper.RoleMapper;
import com.yoga.operator.role.model.Accredit;
import com.yoga.operator.role.model.Priviege;
import com.yoga.operator.role.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@LoggingPrimary(module = RoleService.ModuleName, name = "角色管理")
public class RoleService extends BaseService implements LoggingPrimaryHandler {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AccreditMapper accreditMapper;
    @Autowired
    private PrivilegeMapper privilegeMapper;
    @Autowired
    private RoleCache roleCache;

    public final static String ModuleName = "admin_role";
    public static String getLockKey(long tenantId) {
        return RoleService.class.getName() + "." + tenantId;
    }

    @Override
    public String getPrimaryInfo(Object primaryId) {
        Role role = roleMapper.selectByPrimaryKey(primaryId);
        if (role == null) return null;
        return role.getName();
    }

    @Logging(module = ModuleName, primaryKeyIndex = -1, description = "添加角色", argNames = "租户ID，角色名称，角色描述")
    public long add(long tenantId, String name, String remark) {
        if (new MapperQuery<>(Role.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("name", name)
                .andEqualTo("deleted", false)
                .count(roleMapper) > 0) throw new BusinessException("已经存在同名角色！");
        Role role = new Role(tenantId, name, remark);
        roleMapper.insert(role);
        roleCache.clearCache(tenantId);
        return role.getId();
    }
    @Transactional
    @Logging(module = ModuleName, primaryKeyIndex = 1, description = "删除角色")
    public void delete(long tenantId, long roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if (role == null || role.getTenantId() != tenantId) throw new BusinessException("角色不存在！");
        if (new MapperQuery<>(Accredit.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("roleId", roleId)
                .count(accreditMapper) > 0) throw new BusinessException("该角色拥有授权对象,无法删除");
        role.setDeleted(true);
        roleMapper.updateByPrimaryKey(role);
        roleCache.clearCache(tenantId);
    }
    @Logging(module = ModuleName, primaryKeyIndex = 2, description = "修改角色信息", argNames = "租户ID，角色ID，角色名称，角色描述")
    public void update(long tenantId, long id, String name, String remark) {
        Role role = roleMapper.selectByPrimaryKey(id);
        if (role == null || role.getDeleted() || role.getTenantId() != tenantId) throw new BusinessException("角色不存在！");
        if (StringUtil.isNotBlank(name)) {
            Role exist = new MapperQuery<>(Role.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("name", name)
                    .andEqualTo("deleted", false)
                    .queryFirst(roleMapper);
            if (exist != null && exist.getId() != id) throw new BusinessException("已经存在同名角色！");
            role.setName(name);
        }
        if (remark != null) role.setRemark(remark);
        roleMapper.updateByPrimaryKey(role);
        roleCache.clearCache(tenantId);
    }
    public PageInfo<Role> list(long tenantId, String filter, int pageIndex, int pageCount) {
        PageHelper.startPage(pageIndex + 1, pageCount);
        List<Role> roles = new MapperQuery<>(Role.class)
                .andEqualTo("tenantId", tenantId)
                .andLike("name", "%" + filter + "%", StringUtil.isNotBlank(filter))
                .andEqualTo("deleted", false)
                .query(roleMapper);
        return new PageInfo<>(roles);
    }
    public Role get(long tenantId, long roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        if (role == null || role.getTenantId() != tenantId) throw new BusinessException("角色不存在！");
        return role;
    }
    public List<Role> list(long tenantId) {
        return roleCache.list(tenantId);
    }

    @Transactional
    @Logging(module = ModuleName, primaryKeyIndex = 1, description = "设置角色权限", argNames = "租户ID，角色ID，角色权限")
    public void savePrivilege(long tenantId, long roleId, String[] permissions) {
        new MapperQuery<>(Priviege.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("roleId", roleId)
                .delete(privilegeMapper);
        Arrays.stream(permissions).collect(Collectors.toSet()).forEach(p-> privilegeMapper.insert(new Priviege(tenantId, roleId, p)));
    }
    public Set<String> getPermissions(long tenantId, long roldId) {
        List<Priviege> privieges = new MapperQuery<>(Priviege.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("roleId", roldId)
                .query(privilegeMapper);
        return privieges.stream().map(Priviege::getCode).collect(Collectors.toSet());
    }
}
