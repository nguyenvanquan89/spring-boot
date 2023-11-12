package com.springboot.api;

import com.springboot.dto.BaseDTO;
import com.springboot.entity.BaseEntity;
import com.springboot.exceptions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IBaseAPI<TDto extends BaseDTO<TDto>, TEntity extends BaseEntity> {

    ResponseEntity<?> findById(Long id,
                               TDto dto, TEntity entity) throws ResourceNotFoundException;

    ResponseEntity<?> findAll(int itemPerPage,
                              int currentPage,
                              String order,
                              String orderColumn,
                              TDto dto);

    ResponseEntity<?> create(TDto dto,
                             TEntity entity) throws Exception;

    ResponseEntity<?> update(TDto dto,
                             TEntity entity);

    ResponseEntity<?> delete(List<Long> ids);


}
