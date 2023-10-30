package com.springboot.api.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.api.ICommentAPI;
import com.springboot.dto.CommentDTO;
import com.springboot.entity.CommentEntity;

@RestController
@RequestMapping("/api/comments")
public class CommentAPI extends BaseAPI<CommentDTO, CommentEntity> implements ICommentAPI {

}
