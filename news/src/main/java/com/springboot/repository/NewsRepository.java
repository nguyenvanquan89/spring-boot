package com.springboot.repository;

import java.util.List;

import com.springboot.entity.CategoryEntity;
import com.springboot.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends BaseRepository<NewsEntity>{
	List<NewsEntity> findByCategoryId(Long categoryId);

	/**
	 * Search news by key word or categoryId and pageable
	 * @param category category id need find
	 * @param keyword key word need find
	 * @param pageable page and limit of page
	 * @return List news entity
	 */
	@Query("SELECT n FROM NewsEntity n WHERE " +
			"(:#{#category.id} = 0L OR n.category.id = :#{#category.id}) " +
			"AND (:#{#keyword} IS NULL OR :#{#keyword} = '' OR n.title LIKE %:#{#keyword}% " +
			"OR n.shortDescription LIKE %:#{#keyword}%)")
	Page<NewsEntity> searchNews(
			@Param("category") CategoryEntity category,
			@Param("keyword") String keyword, Pageable pageable);
}
