package com.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.entity.BaseEntity;

import java.util.List;

public interface IBaseService<TEntity extends BaseEntity> {
	
	Page<TEntity> findAllByPageable(Pageable pageRequest);
	TEntity findOneById(Long id);
	TEntity save(TEntity entity);
	void delete(List<Long> lstId);
}
