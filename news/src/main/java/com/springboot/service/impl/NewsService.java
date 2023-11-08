package com.springboot.service.impl;

import com.springboot.entity.CategoryEntity;
import com.springboot.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.springboot.entity.NewsEntity;
import com.springboot.service.INewsService;

@Service
public class NewsService extends BaseService<NewsEntity> implements INewsService {

    @Autowired
    private NewsRepository newsRepository;

    @Override
    public Page<NewsEntity> searchNews(Long categoryId, String keyword, Pageable pageable) {
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);
        return newsRepository.searchNews(category, keyword, pageable);
    }
}
