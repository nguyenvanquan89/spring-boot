package com.springboot.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.dto.BaseDTO;
import com.springboot.entity.BaseEntity;

public interface IBaseAPI <TDto extends BaseDTO<TDto>, TEntity extends BaseEntity> {
	
	ResponseEntity<?> findAll(@RequestParam(name = "itemPerPage", required = false) String itemPerPage,
			@RequestParam(name = "currentPage", required = false) String currentPage, @RequestBody TDto dto, HttpServletRequest request);
	ResponseEntity<?> create(@RequestBody TDto dto, HttpServletRequest request, TEntity entity);
	ResponseEntity<?> update(@RequestBody TDto dto, HttpServletRequest request, TEntity entity);
	ResponseEntity<?> delete(@RequestBody TDto dto, HttpServletRequest request);
}
