package com.yoga.core.behavior.repo;

import com.yoga.core.behavior.model.Behavior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BehaviorRepository extends CrudRepository<Behavior, Long> {

	Page<Behavior> findAll(Specification<Behavior> params, Pageable page);
}
