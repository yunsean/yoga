package com.yoga.operator.role.service;

import com.yoga.core.base.BaseService;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.operator.role.cache.RoleCache;
import com.yoga.operator.role.mapper.AccreditMapper;
import com.yoga.operator.role.mapper.PrivilegeMapper;
import com.yoga.operator.role.mapper.RoleMapper;
import com.yoga.operator.role.model.Accredit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrivilegeService extends BaseService {

    @Autowired
    private AccreditMapper accreditMapper;

    public final static int ObjectType_Branch = 1;
    public final static int ObjectType_Duty = 2;
    public final static int ObjectType_User = 3;

    @Transactional
    public void setDutyRoles(long tenantId, long dutyId, Long[] roleIds) {
        runInLock(RoleService.getLockKey(tenantId), ()-> {
            new MapperQuery<>(Accredit.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("objectType", ObjectType_Duty)
                    .andEqualTo("objectId", dutyId)
                    .delete(accreditMapper);
            if (roleIds == null || roleIds.length < 1) return;
            Arrays.stream(roleIds).forEach(r -> accreditMapper.insert(new Accredit(tenantId, ObjectType_Duty, dutyId, r)));
        });
    }
    public List<Long> getDutyRoleIds(long tenantId, long dutyId) {
        List<Accredit> accredits = new MapperQuery<>(Accredit.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("objectType", ObjectType_Duty)
                .andEqualTo("objectId", dutyId)
                .query(accreditMapper);
        return accredits.stream().map(Accredit::getRoleId).collect(Collectors.toList());
    }

    @Transactional
    public void setBranchRoles(long tenantId, long branchId, Long[] roleIds) {
        runInLock(RoleService.getLockKey(tenantId), ()-> {
            new MapperQuery<>(Accredit.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("objectType", ObjectType_Branch)
                    .andEqualTo("objectId", branchId)
                    .delete(accreditMapper);
            if (roleIds == null || roleIds.length < 1) return;
            Arrays.stream(roleIds).forEach(r -> accreditMapper.insert(new Accredit(tenantId, ObjectType_Branch, branchId, r)));
        });
    }
    public List<Long> getBranchRoleIds(long tenantId, long branchId) {
        List<Accredit> accredits = new MapperQuery<>(Accredit.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("objectType", ObjectType_Branch)
                .andEqualTo("objectId", branchId)
                .query(accreditMapper);
        return accredits.stream().map(Accredit::getRoleId).collect(Collectors.toList());
    }

    @Transactional
    public void setUserRoles(long tenantId, long userId, Long[] roleIds) {
        runInLock(RoleService.getLockKey(tenantId), ()-> {
            new MapperQuery<>(Accredit.class)
                    .andEqualTo("tenantId", tenantId)
                    .andEqualTo("objectType", ObjectType_User)
                    .andEqualTo("objectId", userId)
                    .delete(accreditMapper);
            if (roleIds == null || roleIds.length < 1) return;
            Arrays.stream(roleIds).forEach(r -> accreditMapper.insert(new Accredit(tenantId, ObjectType_User, userId, r)));
        });
    }
    public List<Long> getUserRoleIds(long tenantId, long userId) {
        List<Accredit> accredits = new MapperQuery<>(Accredit.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("objectType", ObjectType_User)
                .andEqualTo("objectId", userId)
                .query(accreditMapper);
        return accredits.stream().map(Accredit::getRoleId).collect(Collectors.toList());
    }

    public Set<String> getPrivileges(long tenantId, long userId) {
        return accreditMapper.getPrivileges(tenantId, userId);
    }
}
