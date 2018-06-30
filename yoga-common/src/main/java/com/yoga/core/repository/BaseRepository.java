package com.yoga.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.List;


public interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID>{
	
	public Page<T> findAll(Specification<T> params, Pageable page);
	public List<T> findAll(Specification<T> params);
	public Page<T> findAll(Pageable page);
}
