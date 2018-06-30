package com.yoga.user.dept.repo;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.yoga.core.repository.BaseRepository;
import com.yoga.user.dept.model.Department;
import org.springframework.transaction.annotation.Transactional;

public interface DepartmentRepository extends BaseRepository<Department, Long> {

    long countByTenantIdAndName(long tenantId, String name);
    long countByTenantIdAndParentId(long tenantId, long parentId);

    Department findOneByTenantIdAndId(long tenantId, long id);
    Department findFirstByTenantIdAndParentIdOrderByCodeDesc(long teantId, long parentId);
    List<Department> findByTenantIdAndParentIdOrderByCode(long tenantId, long parentId);
    List<Department> findByTenantId(long tenantId);
    List<Department> findByTenantIdAndParentId(long tenantId, long parentId);

    @Modifying
    @Transactional
    Long deleteByTenantIdAndId(long tenantId, long id);

    Department findByNameAndTenantId(String name, long tenantId);

    Page<Department> findAll(Pageable page);

    @Query("select dept.id, dept.name from Department dept where dept.tenantId = ?1")
    List<Map<Long, String>> getAllDept(long tenantId);

    @Query(value = "SELECT a.role_id FROM s_accredit a WHERE a.tenant_id = ?1 AND a.object_type = 1 AND a.object_id = ?2", nativeQuery=true)
    List getDeptRoles(long tenantId, long deptId);

    @Query(value = "SELECT T2.* FROM (" +
            "SELECT @r AS _id, " +
            "(SELECT @r \\:= parent_id FROM s_department WHERE id = _id) AS parent_id, @l \\:= @l + 1 AS lvl FROM (SELECT @r \\:= ?1, @l \\:= 0) vars," +
            "s_department h WHERE @r <> 0) T1 JOIN s_department T2 ON T1._id = T2.id ORDER BY T1.lvl DESC", nativeQuery = true)
    List<Department> queryParentById(long selfId);
    @Query(value = "SELECT T2.id FROM (" +
            "SELECT @r AS _id, " +
            "(SELECT @r \\:= parent_id FROM s_department WHERE id = _id) AS parent_id, @l \\:= @l + 1 AS lvl FROM (SELECT @r \\:= ?1, @l \\:= 0) vars," +
            "s_department h WHERE @r <> 0) T1 JOIN s_department T2 ON T1._id = T2.id ORDER BY T1.lvl DESC", nativeQuery = true)
    List<BigInteger> queryParentIdById(long selfId);

    @Query(value = "SELECT t.* FROM" +
            "(SELECT s.* FROM s_department s where s.parent_id > 0 ORDER BY s.parent_id, s.id DESC) t," +
            "  (SELECT @pv \\:= ?1) initialisation" +
            "  WHERE (FIND_IN_SET(parent_id, @pv) > 0 And @pv \\:= concat(@pv, ',', id))", nativeQuery = true)
    List<Department> queryChildrenById(long parentId);

    @Query(value = "SELECT t.id FROM" +
            "(SELECT s.* FROM s_department s where s.parent_id > 0 ORDER BY s.parent_id, s.id DESC) t," +
            "  (SELECT @pv \\:= ?1) initialisation" +
            "  WHERE (FIND_IN_SET(parent_id, @pv) > 0 And @pv \\:= concat(@pv, ',', id))", nativeQuery = true)
    List<BigInteger> queryChildrenIdById(long parentId);

    List<Department> findByTenantIdAndCodeLike(long tenantId, String parentCode);
    List<BigInteger> findIdByTenantIdAndCodeLike(long tenantId, String parentCode);
}
