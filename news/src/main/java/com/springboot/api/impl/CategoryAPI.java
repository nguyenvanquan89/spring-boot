package com.springboot.api.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.api.ICategoryAPI;
import com.springboot.dto.CategoryDTO;
import com.springboot.entity.CategoryEntity;

@RestController
@RequestMapping("/api/categorys")
public class CategoryAPI extends BaseAPI<CategoryDTO, CategoryEntity> implements ICategoryAPI {

}
