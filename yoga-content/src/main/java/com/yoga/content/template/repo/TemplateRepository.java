package com.yoga.content.template.repo;

import com.yoga.content.template.model.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends PagingAndSortingRepository<Template, Long> {
	public Page<Template> findAll(Specification<Template> params, Pageable sort);
	public long countByTenantIdAndId(long tenantId, long id);
	public long countByTenantIdAndCode(long tenantId, String code);
	public List<Template> findByTenantIdAndCode(long tenantId, String code);
	public Template findFirstByTenantIdAndId(long tenantId, long id);
	public Template findFirstByTenantIdAndCode(long tenantId, String code);
	public List<Template> findByTenantIdAndIsEnabled(long tenantId, boolean isEnabled);
	public List<Template> findByTenantId(long tenantId);
}
