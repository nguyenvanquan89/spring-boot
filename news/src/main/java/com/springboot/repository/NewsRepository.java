package com.springboot.repository;

import java.util.List;

import com.springboot.entity.NewsEntity;

public interface NewsRepository extends BaseRepository<NewsEntity>{
	List<NewsEntity> findByCategoryId(Long categoryId);
}
