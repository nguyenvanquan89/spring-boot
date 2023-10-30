package com.springboot.repository;

import com.springboot.entity.CategoryEntity;

public interface CategoryRepository extends BaseRepository<CategoryEntity>{
	CategoryEntity findOneByCode(String code);
}
