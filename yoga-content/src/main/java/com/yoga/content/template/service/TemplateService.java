package com.yoga.content.template.service;

import com.yoga.content.column.repo.ColumnRepository;
import com.yoga.content.template.cache.TemplateCache;
import com.yoga.content.template.repo.TemplateFieldRepository;
import com.yoga.content.sequence.SequenceNameEnum;
import com.yoga.content.template.model.Template;
import com.yoga.content.template.repo.TemplateRepository;
import com.yoga.core.data.PageList;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Map;

@Service
public class TemplateService extends BaseService {

	@Autowired
	private TemplateFieldRepository fieldRepository = null;
	@Autowired
	private ColumnRepository columnRepository = null;
	@Autowired
	private TemplateRepository templateRepository = null;
	@Autowired
	private TemplateCache templateCache = null;
	
	public void addTemplate(long tenantId, String name, String code, String remark, boolean enabled) {
		if (code != null && code.trim().length() < 1) code = null;
		if (code != null && templateRepository.countByTenantIdAndCode(tenantId, code) > 0) {
			throw new BusinessException("已经存在同名Code的模板！");
		}

		Template template = new Template(tenantId, name, remark, code, enabled);
		template.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_CMS_TEMPLATE_ID));
		templateRepository.save(template);
		templateCache.cleanCache(tenantId);
	}

	public void delTemplate(long tenantId, long templateId) {
		Template template = templateRepository.findFirstByTenantIdAndId(tenantId, templateId);
		if (template == null) throw new BusinessException("未找到该模板！");
		long count = columnRepository.countByTenantIdAndTemplateId(tenantId, templateId);
		if (count != 0) throw new BusinessException("该模板在使用中，无法删除！");
		fieldRepository.deleteByTemplateId(templateId);
		templateRepository.delete(templateId);
		templateCache.cleanCache(tenantId);
	}

	public Iterable<Template> allTemplates(long tenantId, boolean enabledOnly) {
		Iterable<Template> templates = null;
		if (enabledOnly) templates = templateRepository.findByTenantIdAndIsEnabled(tenantId, true);
		else templates = templateRepository.findByTenantId(tenantId);
		return templates;
	}

	public Page<Template> findTemplate(long tenantId, Long templateId, String name, String code, String filter, Boolean enable, int pageIndex, int pageSize) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		Pageable request = new PageRequest(pageIndex, pageSize, sort);
		return templateRepository.findAll(new Specification<Template>() {
			@Override
			public Predicate toPredicate(Root<Template> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.equal(root.get("tenantId"), tenantId));
				if (templateId != null) expressions.add(cb.equal(root.get("templateId"), templateId));
				if (name != null && name.length() > 0) expressions.add(cb.like(root.get("name"), "%" + name + "%"));
				if (code != null && code.length() > 0) expressions.add(cb.equal(root.get("code"), code));
				if (filter != null && filter.length() > 0) expressions.add(cb.or(cb.like(root.get("name"), "%" + filter + "%"), cb.like(root.get("code"), "%" + filter + "%")));
				if (enable != null) expressions.add(cb.equal(root.get("isEnabled"), enable));
				return predicate;
			}
		}, request);
	}
	public Page<Template> findTemplate(long tenantId, String filter, Boolean enable, int pageIndex, int pageSize) {
		return findTemplate(tenantId, null, null, null, filter, enable, pageIndex, pageSize);
	}

	public PageList<Template> allTemplates(long tenantId, int pageIndex, int pageSize) {
		Page<Template> templates = findTemplate(tenantId, null, null, null, null, true, pageIndex, pageSize);
		PageList<Template> result = new PageList<>(templates);
		return result;
	}
	public Map<Long, Template> allTemplateMap(long tenantId) {
		return templateCache.templateMap(tenantId);
	}
	public List<Template> allTemplates(long tenantId) {
		return templateCache.templateList(tenantId);
	}

	public Template get(long tenantId, long templateId) {
		Template template = templateRepository.findFirstByTenantIdAndId(tenantId, templateId);
		if (template == null) throw new BusinessException("未找到该模板！");
		return template;
	}

	public void updateTemplate(long tenantId, long templateId, String name, String code, String remark, Boolean enabled) {
		Template template = templateRepository.findFirstByTenantIdAndId(tenantId, templateId);
		if (template == null) throw new BusinessException("未找到该模板！");
		if (code != null && code.trim().length() < 1) code = null;
		if (code != null) {
			List<Template> templates = templateRepository.findByTenantIdAndCode(tenantId, code);
			if (templates != null && templates.size() > 0) {
				if (templates.size() > 1 || templates.get(0).getId() != templateId) {
					throw new BusinessException("已经存在同名Code的模板！");
				}
			}
		}
		if (StrUtil.isNotBlank(name))template.setName(name);
		if (StrUtil.isNotBlank(code))template.setCode(code);
		if (StrUtil.isNotBlank(remark))template.setRemark(remark);
		if (enabled != null)template.setEnabled(enabled);
		templateRepository.save(template);
		templateCache.cleanCache(tenantId);
	}
}
