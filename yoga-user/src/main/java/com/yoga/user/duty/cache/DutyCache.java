package com.yoga.user.duty.cache;

import com.yoga.core.cache.BaseCache;
import com.yoga.user.duty.model.Duty;
import com.yoga.user.duty.repository.DutyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DutyCache extends BaseCache {
    @Autowired
    private DutyRepository dutyRepo;

    @Cacheable(value = "deptsApp", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Duty> dutyMap(long tenantId) {
        List<Duty> duties = dutyRepo.findAllByTenantIdOrderByLevelDesc(tenantId);
        Map<String, Duty> result = new HashMap<>();
        for (Duty duty : duties) {
            result.put(String.valueOf(duty.getId()), duty);
        }
        return result;
    }

    @Cacheable(value = "deptsApp", keyGenerator = "wiselyKeyGenerator")
    public List<Duty> getDuties(long tenantId) {
        List<Duty> duties = dutyRepo.findAllByTenantIdOrderByLevelDesc(tenantId);
        List<Duty> resList = new ArrayList<Duty>();
        for (Duty d : duties) {
            resList.add(d);
        }
        return resList;
    }

    public void clearCache(long tenantId) {
        this.redis.remove("com.yoga.user.duty.cache.DutyCache.getDutyMap." + tenantId);
        this.redis.remove("com.yoga.user.duty.cache.DutyCache.getDuties." + tenantId);
    }
}
