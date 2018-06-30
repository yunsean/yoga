package com.yoga.tenant.tenant.repo;

import com.yoga.core.repository.BaseRepository;
import com.yoga.tenant.tenant.model.TenantMenu;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TenantMenuRepository extends BaseRepository<TenantMenu, Long> {

	List<TenantMenu> findAllByTenantId(long tenantId);

	@Modifying
	@Transactional
	@Query("delete from TenantMenu tm where tm.id = ?1")
	void deleteById(long id);
}
