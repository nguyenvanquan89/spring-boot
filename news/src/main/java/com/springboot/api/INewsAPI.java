package com.springboot.api;

import com.springboot.dto.NewsDTO;
import com.springboot.entity.NewsEntity;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface INewsAPI extends IBaseAPI<NewsDTO, NewsEntity> {

  ResponseEntity<Map<String, Object>> create(String strDto,
      NewsEntity entity,
      MultipartFile file)
      throws IOException;

  ResponseEntity<Map<String, Object>> update(String strDto,
      NewsEntity entity,
      MultipartFile file)
      throws IOException;

  ResponseEntity<Map<String, Object>> searchNews(int itemPerPage,
      int currentPage,
      long categoryId,
      String order,
      String orderColumn,
      String keyword);
}
