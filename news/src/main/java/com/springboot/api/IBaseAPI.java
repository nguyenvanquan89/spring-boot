package com.springboot.api;

import com.springboot.dto.BaseDTO;
import com.springboot.entity.BaseEntity;
import com.springboot.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface IBaseAPI <TDto extends BaseDTO<TDto>, TEntity extends BaseEntity> {

	ResponseEntity<?> findById(@PathVariable("id") Long id,
							   TDto dto, TEntity entity) throws ResourceNotFoundException;
	ResponseEntity<?> findAll(
			@RequestParam(name = "itemPerPage", required = false) String itemPerPage,
			@RequestParam(name = "currentPage", required = false) String currentPage,
			TDto dto);
	ResponseEntity<?> create(@RequestBody TDto dto,
							 TEntity entity) throws Exception;
	ResponseEntity<?> update(@RequestBody TDto dto,
							 TEntity entity);
	ResponseEntity<?> delete(@RequestBody long[] ids);


}
