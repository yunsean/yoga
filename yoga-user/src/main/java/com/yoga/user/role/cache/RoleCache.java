package com.yoga.user.role.cache;

import com.yoga.core.cache.BaseCache;
import com.yoga.user.role.model.Role;
import com.yoga.user.role.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoleCache extends BaseCache {
    @Autowired
    private RoleRepository roleRepository;

    @Cacheable(value = "getRoleMap", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Role> getRoleMap(long tenantId) {
        List<Role> duties = roleRepository.findAllByTenantId(tenantId);
        Map<String, Role> result = new HashMap<>();
        for (Role role : duties) {
            result.put(String.valueOf(role.getId()), role);
        }
        return result;
    }

    @Cacheable(value = "getRoles", keyGenerator = "wiselyKeyGenerator")
    public List<Role> getRoles(long tenantId) {
        List<Role> duties = roleRepository.findAllByTenantId(tenantId);
        List<Role> resList = new ArrayList();
        for (Role d : duties) {
            resList.add(d);
        }
        return resList;
    }

    public void clearCache(long tenantId) {
        this.redis.remove("com.yoga.user.role.cache.RoleCache.getRoleMap." + tenantId);
        this.redis.remove("com.yoga.user.role.cache.RoleCache.getRoles." + tenantId);
    }
}
