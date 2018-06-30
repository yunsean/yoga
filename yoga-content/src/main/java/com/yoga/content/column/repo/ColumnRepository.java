package com.yoga.content.column.repo;

import com.yoga.content.column.model.Column;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColumnRepository extends PagingAndSortingRepository<Column, Long> {

	long countByTenantIdAndId(long tenantId, long id);
	long countByTenantIdAndTemplateId(long tenantId, long templateId);
	long countByTenantIdAndParentId(long tenantId, long parentId);
	long countByTenantIdAndCode(long tenantId, String code);

	List<Column> findAll(Specification<Column> params, Sort sort);
	List<Column> findByTenantId(long tenantId);
	List<Column> findByTenantIdAndCode(long tenantId, String code);
	List<Column> findByTenantIdAndEnabled(long tenantId, boolean enabled);

	Column findFirstByTenantIdAndCode(long tenantId, String code);
	Column findFirstByTenantIdAndId(long tenantId, long id);
}
