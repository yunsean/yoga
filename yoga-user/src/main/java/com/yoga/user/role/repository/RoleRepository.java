package com.yoga.user.role.repository;

import com.yoga.user.role.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {
    @Query("select count(r.id) from Role r where r.name = ?2 and r.tenantId = ?1")
    public long countByTenantIdAndName(long tenantId, String roleName);

    public Page<Role> findAllByTenantId(long tenantId, Pageable page);

    public List<Role> findAllByTenantId(long tenantId);

    @Transactional
    @Modifying
    @Query("delete from Role r where r.tenantId = ?1 and r.id = ?2")
    public void deleteByTenantIdAndRoleId(long tenantId, long id);
    Role findFirstByTenantIdAndName(long tenantId, String name);
}
