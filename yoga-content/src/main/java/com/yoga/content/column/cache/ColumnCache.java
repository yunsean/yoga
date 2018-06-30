package com.yoga.content.column.cache;

import com.yoga.content.column.model.Column;
import com.yoga.content.column.repo.ColumnRepository;
import com.yoga.core.cache.BaseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class ColumnCache extends BaseCache {

	@Autowired
	private ColumnRepository columnRepository = null;

	@Cacheable(value = "allColumns", keyGenerator = "wiselyKeyGenerator")
	public List<Column> allColumns(long tenantId) {
		List<Column> columns = columnRepository.findByTenantId(tenantId);
		Collections.sort(columns, new Comparator<Column>() {
			@Override
			public int compare(Column o1, Column o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return columns;
	}

	public void cleanCache(long tenantId) {
		redis.remove("com.yoga.content.column.cache.ColumnCache.allColumns." + tenantId);
	}
}
