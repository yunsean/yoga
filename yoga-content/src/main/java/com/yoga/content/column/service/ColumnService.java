package com.yoga.content.column.service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.yoga.content.column.cache.ColumnCache;
import com.yoga.content.column.model.Column;
import com.yoga.content.column.repo.ColumnRepository;
import com.yoga.content.constants.Contants;
import com.yoga.content.sequence.SequenceNameEnum;
import com.yoga.content.template.model.Template;
import com.yoga.content.template.repo.TemplateRepository;
import com.yoga.content.template.service.TemplateService;
import com.yoga.core.exception.BusinessException;
import com.yoga.core.service.BaseService;
import com.yoga.core.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.Map.Entry;

@Service
public class ColumnService extends BaseService {
	@Autowired
	private ColumnRepository columnRepository = null;
	@Autowired
	private TemplateRepository templateRepository = null;
	@Autowired
	private TemplateService templateService = null;
	@Autowired
	private ColumnCache columnCache = null;
	@Autowired
	private MongoOperations mongoOperations = null;
	
	public void addCoumn(long tenantId, String name, String code, long parentId, long templateId, boolean enabled) {
		if (parentId != 0) {
			long count = columnRepository.countByTenantIdAndId(tenantId, parentId);
			if (count < 1) throw new BusinessException("父栏目不存在！");
		}
		if (templateId != 0) {
			long count = templateRepository.countByTenantIdAndId(tenantId, templateId);
			if (count < 1) throw new BusinessException("模板不存在！");
		}
		if (code != null && code.trim().length() < 1) {
			code = null;
		}
		if (code != null && columnRepository.countByTenantIdAndCode(tenantId, code) > 0) {
			throw new BusinessException("已经存在同名Code的栏目！");
		}
		Column column = new Column(tenantId, name, parentId, enabled, templateId, code);
		column.setId(sequenceService.getNextValue(SequenceNameEnum.SEQ_CMS_COLUMN_ID));
		columnRepository.save(column);
		columnCache.cleanCache(tenantId);
	}
	public void delColumn(long tenantId, long columnId) {
		Column column = columnRepository.findFirstByTenantIdAndId(tenantId, columnId);
		if (column == null) throw new BusinessException("未找到该栏目！");
		long count = columnRepository.countByTenantIdAndParentId(tenantId, columnId);
		if (count > 0) throw new BusinessException("该栏目包含子目录，无法删除！");
		columnRepository.delete(columnId);
		columnCache.cleanCache(tenantId);
		DBCollection collection = mongoOperations.getCollection(Contants.getCollectionName(tenantId));
		collection.updateMulti(
				new BasicDBObject("columnId", column.getId()),
				new BasicDBObject("$set" ,new BasicDBObject("columnId", 0)));
	}
	public Column getColumn(long tenantId, long columnId) {
		Column column = columnRepository.findFirstByTenantIdAndId(tenantId, columnId);
		if (column == null) throw new BusinessException("未找到该栏目！");
		return column;
	}
	public Column getColumn(long tenantId, String columnCode) {
		Column column = columnRepository.findFirstByTenantIdAndCode(tenantId, columnCode);
		if (column == null) throw new BusinessException("未找到该栏目！");
		return column;
	}
	public List<Column> findColumn(long tenantId, Long parentId, Boolean enabled, String name, String code, Long templateId) {
		Sort sort = new Sort(Sort.Direction.ASC, "id");
		return columnRepository.findAll(new Specification<Column>() {
			@Override
			public Predicate toPredicate(Root<Column> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				List<Expression<Boolean>> expressions = predicate.getExpressions();
				expressions.add(cb.equal(root.get("tenantId"), tenantId));
				if (parentId != null) expressions.add(cb.equal(root.get("parentId"), parentId));
				if (enabled != null) expressions.add(cb.equal(root.get("enabled"), enabled));
				if (templateId != null) expressions.add(cb.equal(root.get("templateId"), templateId));
				if (name != null && name.length() > 0) expressions.add(cb.like(root.get("name"), "%" + name + "%"));
				if (code != null && code.length() > 0) expressions.add(cb.equal(root.get("code"), code));
				return predicate;
			}
		}, sort);
	}
	public List<Column> allColumms(long tenantId, Boolean enabled) {
		return findColumn(tenantId, null, enabled, null, null, null);
	}
	private List<Column> composeColumn(List<Column> columns, Map<Long, Template> templates) {
		Map<Long, Column> mapColumns = new HashMap<>();
		for (Column cmsColumn : columns) {
			mapColumns.put(cmsColumn.getId(), cmsColumn);
			cmsColumn.initChildren();
		}
		Iterator<Entry<Long, Column>> it = mapColumns.entrySet().iterator();
		List<Column> result = new ArrayList<>();
		while(it.hasNext()) {
		   Column self = it.next().getValue();
		   Column parent = mapColumns.get(self.getParentId());
		   if (parent != null) {
			   parent.addChildren(self);
		   } else {
			   result.add(self);
		   }
		}
		sortColumns(result);
		return result;
	}
	private void sortColumns(List<Column> columns) {
		Collections.sort(columns, new Comparator<Column>() {
			@Override
			public int compare(Column o1, Column o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		columns.stream().forEach(column -> {
			if (column.getChildren() != null) {
				sortColumns(column.getChildren());
			}
		});
	}

	@PersistenceContext
	private EntityManager entityManager;

	public List<Column> allColumns(long tenantId) {
		List<Column> columns = columnCache.allColumns(tenantId);
		return composeColumn(columns, templateService.allTemplateMap(tenantId));
	}
	public List<Column> childrenOf(long tenantId, long parentId) {
		if (parentId == 0) return allColumns(tenantId);
		if (columnRepository.countByTenantIdAndId(tenantId, parentId) < 1) throw new BusinessException("父栏目不存在！");
		Query query = entityManager.createNativeQuery("call GetColumnTreeById(?, ?, ?)", "ReturnCmsColumn")
				.setParameter(1, tenantId)
				.setParameter(2, parentId)
				.setParameter(3, false);
		List<Column> columns = query.getResultList();
		return composeColumn(columns, templateService.allTemplateMap(tenantId));
	}
	public List<Column> childrenOf(long tenantId, String parentCode) {
		if (StrUtil.isBlank(parentCode)) return allColumns(tenantId);
		Column column = columnRepository.findFirstByTenantIdAndCode(tenantId, parentCode);
		if (column == null) throw new BusinessException("父栏目不存在！");
		return childrenOf(tenantId, column.getId());
	}
	public List<Column> childrenOf(long tenantId, String code, Boolean enable) {
		if (StrUtil.isBlank(code)) return allColumns(tenantId);
		Query query = entityManager.createNativeQuery("call GetColumnTreeByCode(?, ?, ?)", "ReturnCmsColumn")
				.setParameter(1, tenantId)
				.setParameter(2, code)
				.setParameter(3, enable);
		List<Column> columns = query.getResultList();
		return composeColumn(columns, templateService.allTemplateMap(tenantId));
	}
	public List<Column> columnTreeOf(long tenantId, String nameOrCode) {
		if (StrUtil.isBlank(nameOrCode)) return allColumns(tenantId);
		Query query = entityManager.createNativeQuery("call GetColumnTreeByName(?, ?, ?)", "ReturnCmsColumn")
				.setParameter(1, tenantId)
				.setParameter(2, nameOrCode)
				.setParameter(3, false);
		List<Column> columns = query.getResultList();
		return composeColumn(columns, templateService.allTemplateMap(tenantId));
	}
	public List<Column> columnTreeOf(long tenantId, long columnId) {
		if (columnId == 0) return allColumns(tenantId);
		Query query = entityManager.createNativeQuery("call GetColumnTreeById(?, ?, ?)", "ReturnCmsColumn")
				.setParameter(1, tenantId)
				.setParameter(2, columnId)
				.setParameter(3, false);
		List<Column> columns = query.getResultList();
		return composeColumn(columns, templateService.allTemplateMap(tenantId));
	}
	public List<Column> enabledColumns(long tenantId) {
		List<Column> columns = columnRepository.findByTenantIdAndEnabled(tenantId, true);
		return composeColumn(columns, templateService.allTemplateMap(tenantId));
	}

	public void updateColumn(long tenantId, long columnId, String name, String code, Long templateId, Boolean enable, Long parentId) {
		Column saved = columnRepository.findFirstByTenantIdAndId(tenantId, columnId);
		if (saved == null) throw new BusinessException("未找到该栏目！");
		if (parentId != null && parentId != 0) {
			long count = columnRepository.countByTenantIdAndId(tenantId, parentId);
			if (count < 1) throw new BusinessException("父栏目不存在！");
		}
		if (templateId != 0) {
			long count = templateRepository.countByTenantIdAndId(tenantId, templateId);
			if (count < 1) throw new BusinessException("模板不存在！");
		}
		if (StrUtil.isNotBlank(name))saved.setName(name);
		if (StrUtil.isNotBlank(code)) {
			List<Column> columns = columnRepository.findByTenantIdAndCode(tenantId, code);
			if (columns.size() > 0 && columns.get(0).getId() != columnId) {
				throw new BusinessException("已经存在同名Code的栏目！");
			}
			saved.setCode(code);
		}
		if (templateId != null)saved.setTemplateId(templateId);
		if (enable != null)saved.setEnabled(enable);
		if (parentId != null)saved.setParentId(parentId);
		columnRepository.save(saved);
		columnCache.cleanCache(tenantId);
	}
}
