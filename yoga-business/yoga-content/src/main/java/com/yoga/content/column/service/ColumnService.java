package com.yoga.content.column.service;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.yoga.content.column.mapper.ColumnMapper;
import com.yoga.content.column.model.Column;
import com.yoga.content.constants.Contants;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.base.BaseService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.mybatis.MapperQuery;
import com.yoga.core.utils.StringUtil;
import com.yoga.logging.annotation.Logging;
import com.yoga.logging.annotation.LoggingPrimary;
import com.yoga.logging.service.LoggingPrimaryHandler;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@LoggingPrimary(module = ColumnService.ModuleName, name = "CMS栏目管理")
public class ColumnService extends BaseService implements LoggingPrimaryHandler {

	@Autowired
	private ColumnMapper columnMapper;
	@Autowired
	private TemplateService templateService;
	@Autowired
	private MongoOperations mongoOperations;

	public final static String ModuleName = "cms_column";
	@Override
	public String getPrimaryInfo(Object primaryId) {
		Column column = columnMapper.selectByPrimaryKey(primaryId);
		if (column == null) return null;
		return column.getName();
	}

	@Logging(module = ModuleName, description = "创建栏目", primaryKeyIndex = -1, argNames = "租户ID，栏目名称，栏目编码，栏目备注，父栏目ID，模板ID，是否启用")
	public Long add(long tenantId, String name, String code, String remark, long parentId, long templateId, boolean enabled, boolean hidden) {
		if (parentId != 0) {
			Column parent = columnMapper.selectByPrimaryKey(parentId);
			if (parent == null || parent.getTenantId() != tenantId) throw new BusinessException("父栏目不存在！");
		}
		if (templateId != 0) templateService.get(tenantId, templateId);
		if (code != null && code.trim().length() < 1) code = null;
		if (code != null) {
			if (new MapperQuery<>(Column.class)
				.andEqualTo("tenantId", tenantId)
				.andEqualTo("code", code)
				.count(columnMapper) > 0) throw new BusinessException("已经存在同名Code的栏目！");
		}
		Column column = new Column(tenantId, name, parentId, enabled, templateId, code, remark, hidden);
		columnMapper.insert(column);
		return column.getId();
	}
	@Transactional
	@Logging(module = ModuleName, description = "删除栏目", primaryKeyIndex = 1)
	public void delete(long tenantId, long id) {
		Column column = columnMapper.selectByPrimaryKey(id);
		if (column == null || column.getTenantId() != tenantId) throw new BusinessException("未找到该栏目！");
		if (new MapperQuery<>(Column.class)
			.andEqualTo("parentId", id)
			.count(columnMapper) > 0) throw new BusinessException("该栏目包含子目录，无法删除！");
		columnMapper.deleteByPrimaryKey(id);
		MongoCollection<Document> collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
		collection.updateMany(
				new BasicDBObject("columnId", column.getId()),
				new BasicDBObject("$set", new BasicDBObject("columnId", 0))
		);
	}
	@Logging(module = ModuleName, description = "修改栏目", primaryKeyIndex = 1, argNames = "租户ID，，栏目名称，栏目编码，栏目备注，父栏目ID，模板ID，是否启用")
	public void update(long tenantId, long id, String name, String code, String remark, Long parentId, Long templateId, Boolean enabled, Boolean hidden) {
		Column column = columnMapper.selectByPrimaryKey(id);
		if (column == null || column.getTenantId() != tenantId) throw new BusinessException("未找到该栏目！");
		if (parentId != null) column.setParentId(parentId);
		if (column.getParentId() != null && column.getParentId() != 0L) {
			Column parent = columnMapper.selectByPrimaryKey(column.getParentId());
			if (parent == null || parent.getTenantId() != tenantId) throw new BusinessException("父栏目不存在！");
		}
		if (templateId != null) column.setTemplateId(templateId);
		if (column.getTemplateId() != null && column.getTemplateId() != 0L) {
			templateService.get(tenantId, column.getTemplateId());
		}
		if (StringUtil.isNotBlank(name)) column.setName(name);
		if (StringUtil.isNotBlank(code) && !code.equals(column.getCode())) {
			if (new MapperQuery<>(Column.class)
					.andEqualTo("tenantId", tenantId)
					.andEqualTo("code", code)
					.count(columnMapper) > 0) throw new BusinessException("已经存在同名Code的栏目！");
			column.setCode(code);
		}
		if (remark != null) column.setRemark(remark);
		if (enabled != null) column.setEnabled(enabled);
		if (parentId != null) column.setParentId(parentId);
		if (hidden != null) column.setHidden(hidden);
		columnMapper.updateByPrimaryKey(column);
	}
	public Column get(long tenantId, long id) {
		return get(tenantId, id, false);
	}
	public Column get(long tenantId, long id, boolean allowNull) {
		Column column = columnMapper.get(id, null);
		if (column == null || column.getTenantId() != tenantId) {
			if (!allowNull) throw new BusinessException("未找到该栏目！");
			else return null;
		}
		return column;
	}
	public Column get(long tenantId, String code) {
		return get(tenantId, code, false);
	}
	public Column get(long tenantId, String code, boolean allowNull) {
		Column column = columnMapper.get(null, code);
		if (column == null || column.getTenantId() != tenantId) {
			if (!allowNull) throw new BusinessException("未找到该栏目！");
			else return null;
		}
		return column;
	}
	public List<Column> list(long tenantId, Long parentId, String keyword, Boolean enabled, Boolean hidden) {
		return columnMapper.list(tenantId, keyword, enabled, parentId, hidden);
	}
	public List<Column> tree(long tenantId) {
		List<Column> columns = columnMapper.list(tenantId, null, null, null, null);
		return composeColumn(columns, true);
	}
	public List<Column> childrenOf(long tenantId, long parentId, boolean includeHidden, boolean showAsTree) {
		List<Column> columns;
		if (parentId == 0) columns = new MapperQuery<>(Column.class)
				.andEqualTo("tenantId", tenantId)
				.andEqualTo("hidden", 0, !includeHidden)
				.query(columnMapper);
		else columns = columnMapper.childrenOf(tenantId, parentId, includeHidden, true);
		if (showAsTree) return composeColumn(columns, true);
		else return columns;
	}
	private List<Column> composeColumn(List<Column> columns, boolean sort) {
		if (columns == null) return null;
		if (sort) columns.sort(Comparator.comparing(Column::getName));
		Map<Long, Column> mapColumns = columns.stream().collect(Collectors.toMap(Column::getId, b-> b));
		Iterator<Map.Entry<Long, Column>> it = mapColumns.entrySet().iterator();
		List<Column> result = new ArrayList<>();
		while (it.hasNext()) {
			Column self = it.next().getValue();
			Column parent = mapColumns.get(self.getParentId());
			if (parent != null) parent.addChild(self);
			else result.add(self);
		}
		return result;
	}
}
