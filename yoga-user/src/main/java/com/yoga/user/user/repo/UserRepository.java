package com.yoga.user.user.repo;

import com.yoga.core.repository.BaseRepository;
import com.yoga.user.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends BaseRepository<User, Long> {

	@Query(value = "SELECT u.id, u.username, ud.value FROM  s_user u INNER JOIN s_user_data ud ON u.id = ud.user_id AND ud.name = 'portraituri' AND u.username LIKE %?1% ", nativeQuery = true)
	List<Object> findByUsernameLike(String namelike);

	@Query(value = "SELECT a.role_id FROM s_accredit a WHERE a.tenant_id = ?1 AND a.object_type = 3 AND a.object_id = ?2", nativeQuery = true)
	List<Long> getUserRoles(long tenantId, long userId);
	
	@Query("select count(u.id) from User u where u.username = ?2 and u.tenantId = ?1")
	long getUserCountByUsername(long tenantId, String username);
	
	@Query("select count(u.id) from User u where u.id = ?1")
	int getUserCountById(long userId);
	
	@Query("select count(u.id) from User u where u.phone = ?2 and u.tenantId = ?1")
	int getUserCountByPhone(long tenantId, String phone);

	User findFirstByTenantIdAndUsername(long tenantId, String username);

	User findFirstByTenantIdAndId(long tenantId, long userId);

	User findFirstByTenantIdAndPhone(long tenantId, String phone);
	User findFirstByTenantIdAndExtLong(long tenantId, long extLong);

	@Query("select count(u.id) from User u where u.tenantId = ?1 and (u.username = ?2 or u.phone = ?2)")
	long countByTenantIdAndUsernameOrPhone(long tenantId, String usernameOrPhone);
	
	@Modifying
	@Query("UPDATE User u SET u.disabled = ?1 WHERE u.id = ?2")
	int setDisabled(long userId, int disabled);
	
	@Override
	Page<User> findAll(Pageable page);

	Page<User> findAll(Specification<User> params, Pageable page);

	User findFirstByUsername(String username);

	User findFirstByPhone(String username);

	List<User> findByUsernameOrPhone(String username, String phone);
}
