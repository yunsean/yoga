package com.yoga.user.user.repo;

import com.yoga.user.user.model.Favor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface FavorRepository extends CrudRepository<Favor, Long>, PagingAndSortingRepository<Favor, Long>{
	@Override
	public Page<Favor> findAll(Pageable page);
	public Page<Favor> findAll(Specification<Favor> params, Pageable page);

	public long countById(long favorId);
	public long countByUserIdAndTypeAndObjectId(long userId, int type, String objectId);

	@Transactional
	@Modifying
	public void deleteByUserId(long userId);

	@Transactional
	@Modifying
	public void deleteByTypeAndObjectId(int type, String objectId);
}
