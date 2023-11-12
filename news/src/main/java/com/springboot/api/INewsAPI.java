package com.springboot.api;

import com.springboot.dto.NewsDTO;
import com.springboot.entity.NewsEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface INewsAPI extends IBaseAPI<NewsDTO, NewsEntity> {
    ResponseEntity<?> create(String strDto,
                             NewsEntity entity,
                             MultipartFile file)
            throws IOException;

    ResponseEntity<?> update(String strDto,
                             NewsEntity entity,
                             MultipartFile file)
            throws IOException;

    ResponseEntity<?> searchNews(int itemPerPage,
                                 int currentPage,
                                 long categoryId,
                                 String order,
                                 String orderColumn,
                                 String keyword);
}
