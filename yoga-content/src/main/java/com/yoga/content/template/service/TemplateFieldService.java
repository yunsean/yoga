package com.yoga.content.template.service;

import com.yoga.content.template.enums.FieldType;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import com.yoga.content.sequence.SequenceNameEnum;
import com.yoga.content.template.model.TemplateField;
import com.yoga.content.template.repo.TemplateFieldRepository;
import com.yoga.content.template.repo.TemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class TemplateFieldService extends BaseService {
	
	@Autowired
	private TemplateFieldRepository fieldRepository = null;
	@Autowired
	private TemplateRepository templateRepository = null;
	
	public void addField(long tenantId, long templateId, String name, String code, String hint, FieldType type, String param, String remark, String placeholder, boolean enabled) {
		long count = templateRepository.countByTenantIdAndId(tenantId, templateId);
		if (count < 1) throw new BusinessException("未找到该模板！");
		if (StrUtil.isBlank(code)) throw new BusinessException("未指定字段编码");
		count = fieldRepository.countByTenantIdAndTemplateIdAndCode(tenantId, templateId, code);
		if (count > 0) throw new BusinessException("该模板已经存在指定字段编码");
		TemplateField field = new TemplateField(tenantId, templateId, name, code, type, param, hint, remark, placeholder, enabled);
		field.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_CMS_FIELD_ID));
		fieldRepository.save(field);
	}

	public void delField(long tenantId, long id) {
		long count = fieldRepository.countByTenantIdAndId(tenantId, id);
		if (count < 1) throw new BusinessException("该字段不存在！");
		fieldRepository.delete(id);
	}

	public TemplateField findField(long tenantId, long id) {
		TemplateField field = fieldRepository.findFirstByTenantIdAndId(tenantId, id);
		if (field == null) throw new BusinessException("该字段不存在");
		return field;
	}

	public List<TemplateField> findFields(long tenantId, Long templateId, String name, FieldType type, Boolean enable) {
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		return fieldRepository.findAll(new Specification<TemplateField>() {
			@Override
			public Predicate toPredicate(Root<TemplateField> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.equal(root.get("tenantId"), tenantId));
				if (templateId != null) expressions.add(cb.equal(root.get("templateId"), templateId));
				if (name != null && name.length() > 0) expressions.add(cb.like(root.get("name"), "%" + name + "%"));
				if (type != null) expressions.add(cb.equal(root.get("type"), type));
				if (enable != null) expressions.add(cb.equal(root.get("enabled"), enable));
				return predicate;
			}
		}, sort);
	}
	public List<TemplateField> allFields(long tenantId, long templateId) {
		return findFields(tenantId, templateId, null, null, null);
	}

	public void updateField(long tenantId, long id, String name, String code, String hint, FieldType type, String param, String remark, String placeholder, Boolean enabled) {
		TemplateField field = fieldRepository.findFirstByTenantIdAndId(tenantId, id);
		if (field == null) throw new BusinessException("未找到该字段！");
		if (StrUtil.isNotBlank(name))field.setName(name);
		if (StrUtil.isNotBlank(code))field.setCode(code);
		if (StrUtil.isNotBlank(hint))field.setHint(hint);
		if (StrUtil.isNotBlank(param))field.setParam(param);
		if (StrUtil.isNotBlank(remark))field.setRemark(remark);
		if (placeholder != null) field.setPlaceholder(placeholder);
		if (enabled != null)field.setEnabled(enabled);
		if (type != null)field.setType(type);
		fieldRepository.save(field);

	}
}
