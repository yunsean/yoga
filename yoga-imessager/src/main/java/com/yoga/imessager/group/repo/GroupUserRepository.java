package com.yoga.imessager.group.repo;

import com.yoga.imessager.group.enums.UserType;
import com.yoga.imessager.group.model.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    GroupUser findOneByGroupIdAndUserId(long groupId, long userId);
    List<GroupUser> findByGroupId(long groupId);
    List<GroupUser> findByGroupIdAndApplying(long groupId, boolean applying);
    List<GroupUser> findByGroupIdAndUserTypeAndApplying(long groupId, UserType userType, boolean applying);

    @Transactional
    void deleteByGroupId(long groupId);
    @Transactional
    void deleteByGroupIdAndUserId(long groupId, long userId);
    @Transactional
    void deleteByGroupIdAndUserIdIn(long groupId, long[] userIds);
}
