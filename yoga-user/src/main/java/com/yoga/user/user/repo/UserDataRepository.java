package com.yoga.user.user.repo;

import com.yoga.user.user.model.UserData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDataRepository extends CrudRepository<UserData, Long>{

	List<UserData> findByUserId(long userId);

	UserData findFirstByUserIdAndName(long userId, String name);

	UserData findFirstByUserId(long userId);

}
