package com.springboot.api.impl;

import com.springboot.api.ICommentAPI;
import com.springboot.dto.CommentDTO;
import com.springboot.entity.CommentEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentAPI extends BaseAPI<CommentDTO, CommentEntity> implements ICommentAPI {

}
