package com.yoga.content.template.cache;

import com.yoga.content.template.model.Template;
import com.yoga.content.template.repo.TemplateRepository;
import com.yoga.core.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TemplateCache extends BaseCache {

	@Autowired
	private TemplateRepository templateRepository = null;

	@Cacheable(value = "templateMap", keyGenerator = "wiselyKeyGenerator")
	public Map<Long, Template> templateMap(long tenantId) {
		Map<Long, Template> result = new HashMap<>();
		List<Template> templates = templateRepository.findByTenantId(tenantId);
		for (Template template : templates) {
			result.put(template.getId(), template);
		}
		return result;
	}
	@Cacheable(value = "templateList", keyGenerator = "wiselyKeyGenerator")
	public List<Template> templateList(long tenantId) {
		List<Template> templates = templateRepository.findByTenantId(tenantId);
		return templates;
	}

	public void cleanCache(long tenantId) {
		redis.remove("com.yoga.content.template.cache.TemplateCache.templateMap." + tenantId);
		redis.remove("com.yoga.content.template.cache.TemplateCache.templateList." + tenantId);
	}
}
