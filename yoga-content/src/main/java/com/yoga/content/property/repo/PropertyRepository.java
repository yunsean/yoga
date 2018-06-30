package com.yoga.content.property.repo;

import com.yoga.content.property.model.Property;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends PagingAndSortingRepository<Property, Long> {
	long countByTenantIdAndId(long tenantId, long id);
	long countByTenantIdAndParentId(long tenantId, long parentId);
	long countByTenantIdAndCode(long tenantId, String code);
	List<Property> findByTenantIdAndCode(long tenantId, String code);
	List<Property> findByTenantId(long tenandId);
	Property findFirstByTenantIdAndId(long tenantId, long id);
	@Procedure(name = "DeletePropertyTreeById")
	void deletePropertyById(@Param("parentid") long id, @Param("containParent") boolean flag);
}
