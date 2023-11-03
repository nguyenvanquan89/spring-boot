package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IBaseAPI;
import com.springboot.dto.BaseDTO;
import com.springboot.dto.Views;
import com.springboot.entity.BaseEntity;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.service.IBaseService;
import com.springboot.util.LocaleUtils;
import com.springboot.util.MappingUtils;
import com.springboot.util.MessageKeys;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAPI<TDto extends BaseDTO<TDto>, TEntity extends BaseEntity>
        implements IBaseAPI<TDto, TEntity> {

    @Autowired
    private IBaseService<TEntity> baseService;

    @Autowired
    private LocaleUtils localeUtils;

    @Autowired
    private MappingUtils mappingUtils;

    @Override
    @GetMapping("/{id}")
    @JsonView({Views.SearchView.class})
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
    public ResponseEntity<?> findById(
            @PathVariable("id") Long id,
            TDto dto, TEntity entity)
            throws ResourceNotFoundException {
        entity = baseService.findOneById(id);
        if (entity == null) {
            Object[] obj = {id};
            throw new ResourceNotFoundException(
                    localeUtils.getMessageByKey(MessageKeys.ITEM_NOT_FOUND,obj));
        }
        mappingUtils.map(entity, dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @Override
    @PostMapping
    @JsonView(Views.AddNewView.class)
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
    public ResponseEntity<?> create(@Valid @RequestBody TDto dto,
                                    TEntity entity) throws Exception{
        mappingUtils.mapFromDTO(dto, entity);
        entity = baseService.save(entity);
        mappingUtils.map(entity, dto);
        String msg = localeUtils.getMessageByKey(MessageKeys.ADD_NEW_SUCCESS, null);
        Map<String, Object> body = new HashMap<>();
        body.put("data", dto);
        body.put("message", msg);
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @Override
    @PutMapping
    @JsonView(Views.UpdateView.class)
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
    public ResponseEntity<?> update(@Valid @RequestBody TDto dto,
                                    TEntity entity) {
        mappingUtils.mapFromDTO(dto, entity);
        entity = baseService.save(entity);
        mappingUtils.map(entity, dto);
        String msg = localeUtils.getMessageByKey(MessageKeys.UPDATE_SUCCESS, null);
        Map<String, Object> body = new HashMap<>();
        body.put("data", dto);
        body.put("message", msg);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @Override
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    @JsonView(Views.DeleteView.class)
    public ResponseEntity<?> delete(@RequestBody long[] ids) {
        baseService.delete(ids);
        String msg = localeUtils.getMessageByKey(MessageKeys.DELETE_SUCCESS, null);
        Map<String, Object> body = new HashMap<>();
        body.put("message", msg);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @SuppressWarnings("unchecked")
    @Override
    @GetMapping
    @JsonView(Views.SearchView.class)
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR', 'USER')")
    public ResponseEntity<?> findAll(
            @RequestParam(name = "itemPerPage", required = false) String itemPerPage,
            @RequestParam(name = "currentPage", required = false) String currentPage,
            TDto dto) {
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
        List<TDto> lstDto = (List<TDto>) mappingUtils.mapList(pageEntity.getContent(), dto.getClass());

        Map<String, Object> body = new HashMap<>();
        body.put("totalRecord", pageEntity.getTotalElements());
        body.put("totalPage", pageEntity.getTotalPages());
        body.put("itemPerPage", limit);
        body.put("currentPage", page);
        body.put("lstResult", lstDto);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
