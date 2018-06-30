package com.yoga.content.layout.cache;

import com.yoga.content.layout.model.Layout;
import com.yoga.content.layout.repo.LayoutRepository;
import com.yoga.core.cache.BaseCache;
import com.yoga.core.exception.BusinessException;
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
public class LayoutCache extends BaseCache {

    @Autowired
    private LayoutRepository layoutRepository;

    @Cacheable(value = "getFields", keyGenerator = "wiselyKeyGenerator")
    public String getFields(long tenantId, long layoutId) {
        Layout layout = layoutRepository.findOne(layoutId);
        if (layout == null || layout.getId() != tenantId) throw new BusinessException("布局不存在！");
        return layout.getFields();
    }

    public void clearCache(long tenantId) {
        this.redis.remove("com.yoga.content.layout.cache.LayoutCache.getFields." + tenantId);
    }
}
