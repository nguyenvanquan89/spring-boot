package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.ICategoryAPI;
import com.springboot.dto.CategoryDTO;
import com.springboot.dto.Views;
import com.springboot.entity.CategoryEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryAPI extends BaseAPI<CategoryDTO, CategoryEntity> implements ICategoryAPI {
    @Override
    @GetMapping
    @JsonView(Views.SearchView.class)
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "12") int itemPerPage,
            @RequestParam(defaultValue = "1") int currentPage,
            @RequestParam(required = false ,defaultValue = "DESC") String order,
            @RequestParam(required = false ,defaultValue = "modifiedDate") String orderColumn,
            CategoryDTO dto) {
        return super.findAll(itemPerPage, currentPage, order, orderColumn, dto);
    }
}
