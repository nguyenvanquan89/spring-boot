package com.springboot.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IBaseAPI;
import com.springboot.dto.BaseDTO;
import com.springboot.dto.Views;
import com.springboot.entity.BaseEntity;
import com.springboot.service.IBaseService;

public class BaseAPI<TDto extends BaseDTO<TDto>, TEntity extends BaseEntity> implements IBaseAPI<TDto, TEntity> {

	@Autowired
	private IBaseService<TEntity> baseService;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MessageSource messageSource;

	@Override
	@PostMapping
	@JsonView(Views.AddNewView.class)
	@PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
	public ResponseEntity<?> create(@Valid @RequestBody TDto dto, HttpServletRequest request, TEntity entity) {
		modelMapper.map(dto, entity);
		entity = baseService.save(entity);
		modelMapper.map(entity, dto);
		dto.setMessage(messageSource.getMessage("msg.addnew.success", null, request.getLocale()));
		return new ResponseEntity<>(dto, HttpStatus.CREATED);
	}

	@Override
	@PutMapping
	@JsonView(Views.UpdateView.class)
	@PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
	public ResponseEntity<?> update(@Valid @RequestBody TDto dto, HttpServletRequest request, TEntity entity) {
		modelMapper.map(dto, entity);
		entity = baseService.save(entity);
		modelMapper.map(entity, dto);
		dto.setMessage(messageSource.getMessage("msg.update.success", null, request.getLocale()));
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@Override
	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	@JsonView(Views.DeleteView.class)
	public ResponseEntity<?> delete(@RequestBody TDto dto, HttpServletRequest request) {
		baseService.delete(dto.getLstId());
		dto.setMessage(messageSource.getMessage("msg.delete.success", null, request.getLocale()));
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@Override
	@GetMapping
	@JsonView(Views.SearchView.class)
	@PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR', 'USER')")
	public ResponseEntity<?> findAll(@RequestParam(name = "itemPerPage", required = false) String itemPerPage,
			@RequestParam(name = "currentPage", required = false) String currentPage, @RequestBody TDto dto,
			HttpServletRequest request) {
		int limit = 5; // set default
		int page = 1; // set default
		if (StringUtils.isNotBlank(itemPerPage)) {
			limit = Integer.parseInt(itemPerPage);
		}
		if (StringUtils.isNotBlank(currentPage)) {
			page = Integer.parseInt(currentPage);
		}
		// find all in database
		Pageable pageable = new PageRequest(page - 1, limit);
		Page<TEntity> pageEntity = baseService.findAllByPageable(pageable);

		// Convert Entity to DTO
		List<TDto> lstDto = new ArrayList<TDto>();
		lstDto.addAll(pageEntity.getContent().stream().map(entity -> {
			TDto dtoTmp = (TDto) modelMapper.map(entity, dto.getClass());
			return dtoTmp;
		}).collect(Collectors.toList()));

		Map<String, Object> body = new HashMap<>();
		body.put("totalRecord", pageEntity.getTotalElements());
		body.put("totalPage", pageEntity.getTotalPages());
		body.put("itemPerPage", limit);
		body.put("currentPage", page);
		body.put("lstResult", lstDto);

		return new ResponseEntity<>(body, HttpStatus.OK);
	}

}
