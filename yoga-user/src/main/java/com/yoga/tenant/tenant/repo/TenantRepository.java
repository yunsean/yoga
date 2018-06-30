package com.yoga.tenant.tenant.repo;

import com.yoga.core.repository.BaseRepository;
import com.yoga.tenant.tenant.model.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TenantRepository extends BaseRepository<Tenant, Long> {

	@Query("select count(r.id) from Tenant r where r.code = ?1")
	long getCountByCode(String code);
	@Query("select count(r.id) from Tenant r where r.id = ?1")
	long getCountById(long id);

	Tenant findOneByCode(String code);
	Page<Tenant> findAllByDeleted(int deleted, Pageable page);
	List<Tenant> findAllByDeleted(int deleted);
}
