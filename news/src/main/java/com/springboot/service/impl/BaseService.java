package com.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.entity.BaseEntity;
import com.springboot.repository.BaseRepository;
import com.springboot.service.IBaseService;

import java.util.List;

public class BaseService<TEntity extends BaseEntity> implements IBaseService<TEntity> {

	@Autowired
	private BaseRepository<TEntity> baseRepos;
	
	@Transactional
	@Override
	public TEntity save(TEntity entity) {
		entity = baseRepos.save(entity);
		return entity;
	}

	@Transactional
	@Override
	public void delete(List<Long> lstId) {
		// Delete rows by id
		for (long id : lstId) {
			baseRepos.delete(id);
		}
	}
	
	@Override
	public Page<TEntity> findAllByPageable(Pageable pageRequest) {
		return baseRepos.findAll(pageRequest);
	}

	@Override
	public TEntity findOneById(Long id) {
		return baseRepos.findOne(id);
	}

}
