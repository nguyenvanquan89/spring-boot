package com.springboot.service;

import com.springboot.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface INewsService extends IBaseService<NewsEntity> {

    Page<NewsEntity> searchNews(Long categoryId,
                                String keyword,
                                Pageable pageable);
}
