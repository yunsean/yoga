package com.yoga.tenant.tenant.cache;

import com.yoga.core.base.BaseCache;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.tenant.tenant.mapper.TenantMapper;
import com.yoga.tenant.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TenantCache extends BaseCache {

    @Autowired
    private TenantMapper tenantMapper;

    @Cacheable(value = "tenant", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Long> allCode2Id() {
        List<Tenant> tenants = new MapperQuery<>(Tenant.class)
                .andEqualTo("template", false)
                .andEqualTo("deleted", false)
                .query(tenantMapper);
        return tenants.stream().collect(Collectors.toMap(Tenant::getCode, Tenant::getId, (ov, nv)-> nv));
    }

    @Cacheable(value = "tenant", keyGenerator = "wiselyKeyGenerator")
    public Tenant get(long id) {
        Tenant tenant = tenantMapper.selectByPrimaryKey(id);
        if (tenant == null || tenant.getTemplate() || tenant.getDeleted()) return null;
        return tenant;
    }

    @Cacheable(value = "tenant", keyGenerator = "wiselyKeyGenerator")
    public List<Tenant> getAll() {
        return new MapperQuery<>(Tenant.class)
                .andEqualTo("template", false)
                .andEqualTo("deleted", false)
                .query(tenantMapper);
    }

    public void clearAll() {
        this.redisOperator.removePattern("tenant.*");
    }
    public void clearTenant(long tenantId) {
        this.redisOperator.removePattern("tenant::get:" + tenantId);
    }
}
