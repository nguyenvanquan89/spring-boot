package com.springboot.api;

import com.springboot.dto.NewsDTO;
import com.springboot.entity.NewsEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

public interface INewsAPI extends IBaseAPI<NewsDTO, NewsEntity> {
    ResponseEntity<?> create(@Valid @RequestPart("data") NewsDTO dto,
                             NewsEntity entity,
                             @RequestPart(value = "file", required = false) MultipartFile file)
            throws IOException;

    ResponseEntity<?> update(@Valid @RequestPart("data") NewsDTO dto,
                             NewsEntity entity,
                             @RequestPart(value = "file", required = false) MultipartFile file)
            throws IOException;
}
