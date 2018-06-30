package com.yoga.tenant.tenant.cache;

import com.yoga.core.cache.BaseCache;
import com.yoga.tenant.tenant.model.Tenant;
import com.yoga.tenant.tenant.repo.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TenantCache extends BaseCache {

    @Autowired
    private TenantRepository tenantRepository;

    @Cacheable(value = "allCode2Id", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Long> allCode2Id() {
        Iterable<Tenant> tenants = tenantRepository.findAllByDeleted(0);
        Map<String, Long> result = new HashMap<>();
        for (Tenant tenant : tenants) {
            result.put(tenant.getCode(), tenant.getId());
        }
        return result;
    }

    @Cacheable(value = "get", keyGenerator = "wiselyKeyGenerator")
    public Tenant get(long id) {
        Tenant tenant = tenantRepository.findOne(id);
        if (tenant == null) return null;
        if (tenant.getDeleted()) return null;
        return tenant;
    }

    @Cacheable(value = "getAll", keyGenerator = "wiselyKeyGenerator")
    public List<Tenant> getAll() {
        return tenantRepository.findAllByDeleted(0);
    }

    public void clearAll() {
        this.redis.remove("com.yoga.tenant.tenant.cache.TenantCache.allCode2Id");
        this.redis.remove("com.yoga.tenant.tenant.cache.TenantCache.getAll");
    }
    public void clearTenant(long tenantId) {
        this.redis.remove("com.yoga.tenant.tenant.cache.TenantCache.get." + tenantId);
    }
}
