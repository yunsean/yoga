package com.yoga.logging.cache;

import com.yoga.logging.service.LoggingService;
import com.yoga.core.base.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PrimaryInfoCache extends BaseCache {
    @Lazy
    @Autowired
    private LoggingService loggingService;

    @Cacheable(value = "logging", keyGenerator = "wiselyKeyGenerator")
    public String primaryInfo(String module, Object primaryId) {
        return loggingService.getInfo(module, primaryId);
    }
}
