package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IBaseAPI;
import com.springboot.dto.BaseDTO;
import com.springboot.dto.Views;
import com.springboot.entity.BaseEntity;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.service.IBaseService;
import com.springboot.util.Constants;
import com.springboot.util.LocaleUtils;
import com.springboot.util.MappingUtils;
import com.springboot.util.MessageKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseAPI<D extends BaseDTO, E extends BaseEntity>
    implements IBaseAPI<D, E> {

  @Autowired
  private IBaseService<E> baseService;

  @Autowired
  private LocaleUtils localeUtils;

  @Autowired
  private MappingUtils mappingUtils;

  private static final Logger loggerBaseAPI = LoggerFactory.getLogger(BaseAPI.class);

  @Override
  @GetMapping("/{id}")
  @JsonView({Views.SearchView.class})
  @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
  public ResponseEntity<D> findById(
      @PathVariable("id") Long id,
      D dto)
      throws ResourceNotFoundException {

    loggerBaseAPI.info("find id {}", id);
    E entity = baseService.findOneById(id);
    if (entity == null) {
      Object[] obj = {id};
      String msg = localeUtils.getMessageByKey(MessageKeys.ITEM_NOT_FOUND, obj);
      loggerBaseAPI.error(Constants.ERROR_MESSAGE_LOGGER, msg);
      throw new ResourceNotFoundException(msg);
    }
    mappingUtils.map(entity, dto);
    return new ResponseEntity<>(dto, HttpStatus.OK);
  }

  @Override
  @PostMapping
  @JsonView(Views.AddNewView.class)
  @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
  public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody D dto,
      E entity) throws Exception {

    mappingUtils.mapFromDTO(dto, entity);
    entity = baseService.save(entity);
    mappingUtils.map(entity, dto);
    String msg = localeUtils.getMessageByKey(MessageKeys.ADD_NEW_SUCCESS, null);
    Map<String, Object> body = new HashMap<>();
    body.put(Constants.DATA_RESPONSE, dto);
    body.put(Constants.MESSAGE_RESPONSE, msg);
    return new ResponseEntity<>(body, HttpStatus.CREATED);
  }

  @Override
  @PutMapping
  @JsonView(Views.UpdateView.class)
  @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR')")
  public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody D dto,
      E entity) {
    mappingUtils.mapFromDTO(dto, entity);
    entity = baseService.save(entity);
    mappingUtils.map(entity, dto);
    String msg = localeUtils.getMessageByKey(MessageKeys.UPDATE_SUCCESS, null);
    Map<String, Object> body = new HashMap<>();
    body.put(Constants.DATA_RESPONSE, dto);
    body.put(Constants.MESSAGE_RESPONSE, msg);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @Override
  @DeleteMapping
  @PreAuthorize("hasRole('ADMIN')")
  @JsonView(Views.DeleteView.class)
  public ResponseEntity<Map<String, Object>> delete(@RequestBody List<Long> ids) {
    baseService.delete(ids);
    String msg = localeUtils.getMessageByKey(MessageKeys.DELETE_SUCCESS, null);
    Map<String, Object> body = new HashMap<>();
    body.put(Constants.MESSAGE_RESPONSE, msg);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

  @SuppressWarnings("unchecked")
  @Override
  @GetMapping
  @JsonView(Views.SearchView.class)
  @PreAuthorize("hasAnyRole('ADMIN', 'AUTHOR', 'EDITOR', 'USER')")
  public ResponseEntity<Map<String, Object>> findAll(
      @RequestParam(defaultValue = "12") int itemPerPage,
      @RequestParam(defaultValue = "1") int currentPage,
      @RequestParam(required = false, defaultValue = "DESC") String order,
      @RequestParam(required = false, defaultValue = "modifiedDate") String orderColumn,
      D dto) {

    Sort sort = new Sort(Sort.Direction.ASC, orderColumn);
    if (Constants.DESC_REQUEST.equalsIgnoreCase(order)) {
      sort = new Sort(Sort.Direction.DESC, orderColumn);
    }
    // find all in database
    Pageable pageable = new PageRequest(currentPage - 1, itemPerPage, sort);
    Page<E> pageEntity = baseService.findAllByPageable(pageable);

    // Convert Entity to DTO
    List<D> lstDto = (List<D>) mappingUtils.mapList(pageEntity.getContent(), dto.getClass());

    Map<String, Object> body = new HashMap<>();
    body.put(Constants.TOTAL_RECORD_RESPONSE, pageEntity.getTotalElements());
    body.put(Constants.TOTAL_PAGE_RESPONSE, pageEntity.getTotalPages());
    body.put(Constants.ITEM_PER_PAGE_RESPONSE, itemPerPage);
    body.put(Constants.CURRENT_PAGE_RESPONSE, currentPage);
    body.put(Constants.LST_RESULT_RESPONSE, lstDto);

    return new ResponseEntity<>(body, HttpStatus.OK);
  }

}
