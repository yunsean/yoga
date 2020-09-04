package com.yoga.setting.cache;

import com.yoga.setting.mapper.SettingMapper;
import com.yoga.setting.model.Setting;
import com.yoga.core.base.BaseCache;
import com.yoga.core.mybatis.MapperQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SettingCache extends BaseCache {

    @Autowired
    private SettingMapper settingMapper;

    @Cacheable(value = "setting", keyGenerator = "wiselyKeyGenerator")
    public Setting get(long tenantId, String module, String key) {
        Setting setting = new MapperQuery<>(Setting.class)
                .andEqualTo("tenantId", tenantId)
                .andEqualTo("module", module)
                .andEqualTo("key", key)
                .queryFirst(settingMapper);
        return setting;
    }
    public void flush(long tenantId, String module, String key) {
        redisOperator.remove("setting::get:" + tenantId + "_" + module + "_" + key);
    }
}
