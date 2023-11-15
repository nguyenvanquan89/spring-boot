package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IRoleAPI;
import com.springboot.dto.RoleDTO;
import com.springboot.dto.Views;
import com.springboot.entity.RoleEntity;
import com.springboot.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleAPI extends BaseAPI<RoleDTO, RoleEntity> implements IRoleAPI {

    @Override
    @GetMapping("/{id}")
    @JsonView(Views.SearchView.class)
    public ResponseEntity<?> findById(
            @PathVariable("id") Long id,
            RoleDTO dto, RoleEntity entity) throws ResourceNotFoundException {
        return super.findById(id, dto, entity);
    }

    @Override
    @PostMapping
    @JsonView(Views.AddNewView.class)
    public ResponseEntity<?> create(@Valid @RequestBody RoleDTO dto,
                                    RoleEntity entity) throws Exception {
        return super.create(dto, entity);
    }

    @Override
    @PutMapping
    @JsonView(Views.UpdateView.class)
    public ResponseEntity<?> update(@Valid @RequestBody RoleDTO dto,
                                    RoleEntity entity) {
        return super.update(dto, entity);
    }

    @Override
    @GetMapping
    @JsonView(Views.SearchView.class)
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "12") int itemPerPage,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(required = false ,defaultValue = "DESC") String order,
            @RequestParam(required = false ,defaultValue = "modifiedDate") String orderColumn,
            RoleDTO dto) {
        return super.findAll(itemPerPage, currentPage, order, orderColumn, dto);
    }

}
