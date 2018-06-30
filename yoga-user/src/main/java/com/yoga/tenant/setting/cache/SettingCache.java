package com.yoga.tenant.setting.cache;

import com.yoga.core.cache.BaseCache;
import com.yoga.tenant.setting.model.Setting;
import com.yoga.tenant.setting.repo.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SettingCache extends BaseCache {

    @Autowired
    private SettingRepository settingRepository = null;

    @Cacheable(value = "settingMap", keyGenerator = "wiselyKeyGenerator")
    public Map<String, String> settingMap(long tenantId) {
        List<Setting> settings = settingRepository.findByTenantId(tenantId);
        Map<String, String> result = new HashMap<>();
        for (Setting setting : settings) {
            result.put(setting.getKey(), setting.getValue());
        }
        return result;
    }
    @Cacheable(value = "get", keyGenerator = "wiselyKeyGenerator")
    public Setting get(long tenantId, String module, String key) {
        return settingRepository.findOneByTenantIdAndModuleAndKey(tenantId, module, key);
    }
    public void flush(long tenantId, String module, String key) {
        redis.removePattern("com.yoga.tenant.setting.cache.SettingCache.get." + tenantId + "." + module + "." + key + "*");
    }
}
