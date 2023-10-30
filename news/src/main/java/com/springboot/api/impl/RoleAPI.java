package com.springboot.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IRoleAPI;
import com.springboot.dto.RoleDTO;
import com.springboot.dto.Views;
import com.springboot.entity.RoleEntity;


@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleAPI extends BaseAPI<RoleDTO, RoleEntity> implements IRoleAPI {

	@Override
	@PostMapping
	@JsonView(Views.AddNewView.class)
	public ResponseEntity<?> create(@Valid @RequestBody RoleDTO dto, HttpServletRequest request, RoleEntity entity) {
		return super.create(dto, request, entity);
	}

	@Override
	@PutMapping
	@JsonView(Views.UpdateView.class)
	public ResponseEntity<?> update(@Valid @RequestBody RoleDTO dto, HttpServletRequest request, RoleEntity entity) {
		return super.update(dto, request, entity);
	}

	@Override
	@GetMapping
	@JsonView(Views.SearchView.class)
	public ResponseEntity<?> findAll(@RequestParam(name = "itemPerPage", required = false) String itemPerPage,
			@RequestParam(name = "currentPage", required = false) String currentPage, @RequestBody RoleDTO dto,
			HttpServletRequest request) {
		return super.findAll(itemPerPage, currentPage, dto, request);
	}

}
