package com.springboot.api;

import com.springboot.dto.BaseDTO;
import com.springboot.entity.BaseEntity;
import com.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface IBaseAPI<D extends BaseDTO, E extends BaseEntity> {

  ResponseEntity<D> findById(Long id, D dto) throws ResourceNotFoundException;

  ResponseEntity<Map<String, Object>> findAll(int itemPerPage,
      int currentPage,
      String order,
      String orderColumn,
      D dto);

  ResponseEntity<Map<String, Object>> create(D dto,
      E entity) throws Exception;

  ResponseEntity<Map<String, Object>> update(D dto,
      E entity);

  ResponseEntity<Map<String, Object>> delete(List<Long> ids);

}
