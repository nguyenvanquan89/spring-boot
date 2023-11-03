package com.springboot.api.impl;

import com.springboot.api.ITokenAPI;
import com.springboot.dto.TokenDTO;
import com.springboot.entity.TokenEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tokens")
public class TokenAPI extends BaseAPI<TokenDTO, TokenEntity> implements ITokenAPI {
}
