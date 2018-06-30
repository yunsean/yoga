package com.yoga.user.duty.repository;

import com.yoga.user.duty.model.Duty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

public interface DutyRepository extends CrudRepository<Duty, Long>, PagingAndSortingRepository<Duty, Long>{

	Duty findFirstByTenantIdAndId(long tenantId, long id);

	Duty findFirstByLevelAndTenantId(int level, long tenantId);

	Duty findOneByNameAndTenantId(String name, long tenantId);

	List<Duty> findAllByTenantIdOrderByLevelDesc(long tenantId);

	@Query("select duty.level, duty.name from Duty duty where duty.tenantId = ?1")
	public List<Map<Integer, String>> allDuties(long tenantId);

	@Query(value = "SELECT a.role_id FROM s_accredit a WHERE a.tenant_id = ?1 AND a.object_type = 2 AND a.object_id = ?2", nativeQuery = true)
	public List getDutyRoles(long tenantId, long dutyId);
}
