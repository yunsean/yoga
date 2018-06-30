package com.yoga.content.template.repo;

import com.yoga.content.template.enums.FieldType;
import com.yoga.content.template.model.TemplateField;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TemplateFieldRepository extends PagingAndSortingRepository<TemplateField, Long> {

	List<TemplateField> findAll(Specification<TemplateField> params, Sort sort);
	long countByTenantIdAndId(long tenantId, long fieldId);
	TemplateField findFirstByTenantIdAndId(long tenantId, long fieldId);
	long countByTenantIdAndTypeAndCode(long tenantId, FieldType type, String code);
	long countByTenantIdAndTemplateIdAndCode(long tenantId, long templateId, String code);
	@Modifying
	@Transactional
	void deleteByTemplateId(long templateId);
}
