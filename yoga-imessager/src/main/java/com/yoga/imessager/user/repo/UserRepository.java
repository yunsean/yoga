package com.yoga.imessager.user.repo;

import com.yoga.imessager.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "imUserRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByTenantIdAndId(long tenantId, long id);
    Page<User> findByTenantIdAndNickname(long tenantId, String nickname, Pageable pageable);
}
