package com.yoga.imessager.group.repo;

import com.yoga.imessager.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByTenantId(long tenantId);
    List<Group> findByTenantIdAndNameLike(long tenantId, String name);
    Group findOneByTenantIdAndId(long tenantId, long id);
    Group findFirstByTenantIdAndName(long tenantId, String name);
    Group findFirstByTenantIdAndCreatorId(long tenantId, long creatorId);

    @Query(value = "select g.* from im_group g, im_group_user gu where g.id = gu.group_id and g.tenant_id = ?1 and gu.user_id = ?2", nativeQuery = true)
    List<Group> findByTenantIdAndUserId(long tenantId, long userId);
}
