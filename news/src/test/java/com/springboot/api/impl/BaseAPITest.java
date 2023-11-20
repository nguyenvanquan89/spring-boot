package com.springboot.api.impl;

import com.springboot.dto.BaseDTO;
import com.springboot.entity.BaseEntity;
import com.springboot.service.IBaseService;
import com.springboot.util.LocaleUtils;
import com.springboot.util.MappingUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BaseAPITest<D extends BaseDTO, E extends BaseEntity> {

  @Mock
  protected IBaseService<E> baseService;

  @Mock
  protected LocaleUtils localeUtils;

  @Mock
  protected MappingUtils mappingUtils;

  /*@InjectMocks
  private BaseAPI<D, E> baseAPI;*/

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterEach
  void tearDown() {
  }

}