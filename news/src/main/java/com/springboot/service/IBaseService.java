package com.springboot.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.entity.BaseEntity;

import java.util.List;

public interface IBaseService<T extends BaseEntity> {
	
	Page<T> findAllByPageable(Pageable pageRequest);
	T findOneById(Long id);
	T save(T entity);
	void delete(List<Long> lstId);
}
