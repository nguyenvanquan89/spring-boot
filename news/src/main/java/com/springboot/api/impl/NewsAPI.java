package com.springboot.api.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.api.INewsAPI;
import com.springboot.dto.NewsDTO;
import com.springboot.entity.NewsEntity;

@RestController
@RequestMapping("/api/news")
public class NewsAPI extends BaseAPI<NewsDTO, NewsEntity> implements INewsAPI {

}
