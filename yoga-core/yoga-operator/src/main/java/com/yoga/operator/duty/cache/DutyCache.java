package com.yoga.operator.duty.cache;

import com.yoga.core.base.BaseCache;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.operator.duty.mapper.DutyMapper;
import com.yoga.operator.duty.model.Duty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DutyCache extends BaseCache {
    @Autowired
    private DutyMapper dutyMapper;

    @Cacheable(value = "duty", keyGenerator = "wiselyKeyGenerator")
    public Map<String, Duty> map(long tenantId) {
        List<Duty> duties = new MapperQuery<>(Duty.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("deleted", false)
                .query(dutyMapper);
        return duties.stream().collect(Collectors.toMap(d-> d.getId().toString(), d-> d));
    }

    @Cacheable(value = "duty", keyGenerator = "wiselyKeyGenerator")
    public List<Duty> list(long tenantId) {
        List<Duty> duties = new MapperQuery<>(Duty.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("deleted", false)
                .query(dutyMapper);
        return duties;
    }

    public void clearCache(long tenantId) {
        this.redisOperator.remove("duty::map:" + tenantId);
        this.redisOperator.remove("duty::list:" + tenantId);
    }
}
