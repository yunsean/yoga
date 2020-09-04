package com.yoga.operator.role.cache;

import com.yoga.core.base.BaseCache;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.operator.role.mapper.RoleMapper;
import com.yoga.operator.role.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RoleCache extends BaseCache {
    @Autowired
    private RoleMapper roleMapper;

    @Cacheable(value = "role", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Role> map(long tenantId) {
        List<Role> duties = new MapperQuery<>(Role.class)
                .andEqualTo("", tenantId)
                .query(roleMapper);
        return duties.stream().collect(Collectors.toMap(r-> r.getId().toString(), r-> r));
    }

    @Cacheable(value = "role", keyGenerator = "wiselyKeyGenerator")
    public List<Role> list(long tenantId) {
        return new MapperQuery<>(Role.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("deleted", false)
                .query(roleMapper);
    }

    public void clearCache(long tenantId) {
        this.redisOperator.remove("role::map:" + tenantId);
        this.redisOperator.remove("role::list:" + tenantId);
    }
}
