package com.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.entity.BaseEntity;

public interface IBaseService<TEntity extends BaseEntity> {
	
	Page<TEntity> findAllByPageable(Pageable pageRequest);
	TEntity findOneById(Long id);
	TEntity save(TEntity entity);
	void delete(long[] lstId);
}
