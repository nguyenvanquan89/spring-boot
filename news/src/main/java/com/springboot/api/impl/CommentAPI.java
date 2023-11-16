package com.springboot.api.impl;

import com.github.javafaker.Faker;
import com.springboot.api.ICommentAPI;
import com.springboot.dto.CommentDTO;
import com.springboot.entity.CommentEntity;
import com.springboot.entity.NewsEntity;
import com.springboot.entity.UserEntity;
import com.springboot.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentAPI extends BaseAPI<CommentDTO, CommentEntity> implements ICommentAPI {

    @Autowired
    private ICommentService commentService;

    @PostMapping("/generateFakerCommentData")
    public ResponseEntity<?> generateFakerCommentData() {
        Faker faker = new Faker();
        CommentEntity commentEntity;
        NewsEntity newsEntity;
        UserEntity userEntity;

        for (int i = 0; i < 5000; i++) {
            commentEntity = new CommentEntity();
            newsEntity = new NewsEntity();
            userEntity = new UserEntity();

            newsEntity.setId((long)faker.number().numberBetween(34599,39500));
            userEntity.setId((long)faker.random().nextInt(1,10));

            commentEntity.setContent(faker.lorem().paragraph());
            commentEntity.setNews(newsEntity);
            commentEntity.setUser(userEntity);
            try {
                commentService.save(commentEntity);
            } catch (Exception ignored) {
                // TODO should write log
            }
        }
        return ResponseEntity.ok("Generate Faker News successfully");
    }
}
