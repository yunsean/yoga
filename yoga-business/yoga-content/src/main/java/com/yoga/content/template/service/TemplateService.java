package com.yoga.content.template.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yoga.content.column.mapper.ColumnMapper;
import com.yoga.content.column.model.Column;
import com.yoga.content.template.enums.FieldType;
import com.yoga.content.template.mapper.TemplateFieldMapper;
import com.yoga.content.template.mapper.TemplateMapper;
import com.yoga.content.template.model.Template;
import com.yoga.content.template.model.TemplateField;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("cmsTemplateService")
@LoggingPrimary(module = TemplateService.ModuleName, name = "CMS模板管理")
public class TemplateService extends BaseService implements LoggingPrimaryHandler {

	@Autowired
	private TemplateMapper templateMapper;
	@Autowired
	private TemplateFieldMapper fieldMapper;
	@Autowired
	private ColumnMapper columnMapper;

	public final static String ModuleName = "cms_template";
	@Override
	public String getPrimaryInfo(Object primaryId) {
		Template template = templateMapper.selectByPrimaryKey(primaryId);
		if (template == null) return null;
		return template.getName();
	}

	@Logging(module = ModuleName, description = "创建模板", primaryKeyIndex = -1, argNames = "租户ID，模板名称，模板编码，备注信息，是否启用")
	public Long add(long tenantId, String name, String code, String remark, boolean enabled) {
		if (code != null && code.trim().length() < 1) code = null;
		if (code != null) {
			if (new MapperQuery<>(Template.class)
				.andEqualTo("tenantId", tenantId)
				.andEqualTo("code", code)
				.count(templateMapper) > 0) throw new BusinessException("已经存在同名Code的模板！");
		}
		Template template = new Template(tenantId, name, remark, code, enabled);
		templateMapper.insert(template);
		return template.getId();
	}
	@Transactional
	@Logging(module = ModuleName, description = "删除模板", primaryKeyIndex = 1)
	public void delete(long tenantId, long templateId) {
		Template template = templateMapper.selectByPrimaryKey(templateId);
		if (template == null || template.getTenantId() != tenantId) throw new BusinessException("未找到该模板！");
		if (new MapperQuery<>(Column.class)
			.andEqualTo("templateId", templateId)
			.count(columnMapper) > 0) throw new BusinessException("该模板在使用中，无法删除！");
		new MapperQuery<>(TemplateField.class)
				.andEqualTo("templateId", templateId)
				.delete(fieldMapper);
		new MapperQuery<>(Template.class)
				.andEqualTo("id", templateId)
				.delete(templateMapper);
	}
	@Logging(module = ModuleName, description = "修改模板", primaryKeyIndex = 1, argNames = "租户ID，，模板名称，模板编码，备注信息，是否启用")
	public void update(long tenantId, long templateId, String name, String code, String remark, Boolean enabled) {
		Template template = templateMapper.selectByPrimaryKey(templateId);
		if (template == null || template.getTenantId() != tenantId) throw new BusinessException("未找到该模板！");
		if (code != null && code.trim().length() < 1) code = null;
		if (code != null && !code.equals(template.getCode())) {
			if (new MapperQuery<>(Template.class)
					.andEqualTo("tenantId", tenantId)
					.andEqualTo("code", code)
					.count(templateMapper) > 0) throw new BusinessException("已经存在同名Code的模板！");
			template.setCode(code);
		}
		if (StringUtil.isNotBlank(name))template.setName(name);
		if (StringUtil.isNotBlank(code))template.setCode(code);
		if (StringUtil.isNotBlank(remark))template.setRemark(remark);
		if (enabled != null)template.setEnabled(enabled);
		templateMapper.updateByPrimaryKey(template);
	}

	public PageInfo<Template> list(long tenantId, String keyword, Boolean enabled, int pageIndex, int pageSize) {
		PageHelper.startPage(pageIndex + 1, pageSize);
		List<Template> templates = templateMapper.list(tenantId, keyword, enabled);
		return new PageInfo<>(templates);
	}
	public List<Template> list(long tenantId, String keyword, Boolean enabled) {
		return templateMapper.list(tenantId, keyword, enabled);
	}
	public Template get(long tenantId, long templateId) {
		Template template = templateMapper.selectByPrimaryKey(templateId);
		if (template == null || template.getTenantId() != tenantId) throw new BusinessException("未找到该模板！");
		return template;
	}

	@Logging(module = ModuleName, description = "添加模板字段", primaryKeyIndex = 1, argNames = "租户ID，，字段名称，字段编码，字段提示，字段类型，字段参数，字段备注，缺省值，是否启用")
	public Long addField(long tenantId, long templateId, String name, String code, String hint, FieldType type, String param, String remark, String placeholder, boolean enabled, Integer sort) {
		Template template = templateMapper.selectByPrimaryKey(templateId);
		if (template == null || template.getTenantId() != tenantId) throw new BusinessException("未找到该模板！");
		if (StringUtil.isBlank(code)) throw new BusinessException("未指定字段编码");
		if (new MapperQuery<>(TemplateField.class)
			.andEqualTo("templateId", templateId)
			.andEqualTo("code", code)
			.count(fieldMapper) > 0) throw new BusinessException("该模板已经存在指定字段编码");
		TemplateField field = new TemplateField(templateId, name, code, type, param, hint, remark, placeholder, enabled, sort);
		fieldMapper.insert(field);
		return field.getId();
	}
	@Logging(module = ModuleName, description = "删除模板字段", primaryKeyIndex = -1)
	public Long deleteField(long tenantId, long fieldId) {
		TemplateField field = fieldMapper.selectByPrimaryKey(fieldId);
		if (field == null) throw new BusinessException("该字段不存在！");
		fieldMapper.deleteByPrimaryKey(fieldId);
		return field.getTemplateId();
	}
	@Logging(module = ModuleName, description = "更新模板字段", primaryKeyIndex = -1, argNames = "租户ID，，字段名称，字段编码，字段提示，字段类型，字段参数，字段备注，缺省值，是否启用")
	public Long updateField(long tenantId, long fieldId, String name, String code, String hint, FieldType type, String param, String remark, String placeholder, Boolean enabled, Integer sort) {
		TemplateField field = fieldMapper.selectByPrimaryKey(fieldId);
		if (field == null) throw new BusinessException("该字段不存在！");
		if (StringUtil.isNotBlank(code) && !code.equals(field.getCode())) {
			if (new MapperQuery<>(TemplateField.class)
					.andEqualTo("templateId", field.getTemplateId())
					.andEqualTo("code", code)
					.count(fieldMapper) > 0) throw new BusinessException("该模板已经存在指定字段编码");
			field.setCode(code);
		}
		if (StringUtil.isNotBlank(name))field.setName(name);
		if (StringUtil.isNotBlank(hint))field.setHint(hint);
		if (StringUtil.isNotBlank(param))field.setParam(param);
		if (StringUtil.isNotBlank(remark))field.setRemark(remark);
		if (placeholder != null) field.setPlaceholder(placeholder);
		if (enabled != null)field.setEnabled(enabled);
		if (sort != null) field.setSort(sort);
		if (type != null)field.setType(type);
		fieldMapper.updateByPrimaryKey(field);
		return field.getTemplateId();
	}
	public TemplateField getField(long tenantId, long fieldId) {
		TemplateField field = fieldMapper.selectByPrimaryKey(fieldId);
		if (field == null) throw new BusinessException("该字段不存在");
		return field;
	}
	public List<TemplateField> listField(long templateId, String name, FieldType type, Boolean enabled) {
		return new MapperQuery<>(TemplateField.class)
				.andEqualTo("templateId", templateId)
				.andLike("name", "%" + name + "%", StringUtil.isNotBlank(name))
				.andEqualTo("type", type, type != null)
				.andEqualTo("enabled", true, enabled != null && enabled)
				.orderBy("sort")
				.orderBy("id", false)
				.query(fieldMapper);
	}
}
