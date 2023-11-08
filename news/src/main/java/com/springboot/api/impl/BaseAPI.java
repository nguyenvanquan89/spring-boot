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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    private static final Logger logger = LoggerFactory.getLogger(BaseAPI.class);

    @Override
    @GetMapping("/{id}")
    @JsonView({Views.SearchView.class})
    @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
    public ResponseEntity<?> findById(
            @PathVariable("id") Long id,
            TDto dto, TEntity entity)
            throws ResourceNotFoundException {
        logger.info(String.format("find id=%d", id));
        entity = baseService.findOneById(id);
        if (entity == null) {
            Object[] obj = {id};
            String msg = localeUtils.getMessageByKey(MessageKeys.ITEM_NOT_FOUND,obj);
            logger.error(String.format("error message: %s", msg));
            throw new ResourceNotFoundException(msg);
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
    public ResponseEntity<?> delete(@RequestBody List<Long> ids) {
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
            @RequestParam(defaultValue = "12") int itemPerPage,
            @RequestParam(defaultValue = "1") int currentPage,
            TDto dto) {

        // find all in database
        Pageable pageable = new PageRequest(currentPage - 1, itemPerPage);
        Page<TEntity> pageEntity = baseService.findAllByPageable(pageable);

        // Convert Entity to DTO
        List<TDto> lstDto = (List<TDto>) mappingUtils.mapList(pageEntity.getContent(), dto.getClass());

        Map<String, Object> body = new HashMap<>();
        body.put("totalRecord", pageEntity.getTotalElements());
        body.put("totalPage", pageEntity.getTotalPages());
        body.put("itemPerPage", itemPerPage);
        body.put("currentPage", currentPage);
        body.put("lstResult", lstDto);

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

}
