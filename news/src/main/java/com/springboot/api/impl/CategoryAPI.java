package com.springboot.api.impl;

import com.springboot.api.ICategoryAPI;
import com.springboot.dto.CategoryDTO;
import com.springboot.entity.CategoryEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryAPI extends BaseAPI<CategoryDTO, CategoryEntity> implements ICategoryAPI {

}
