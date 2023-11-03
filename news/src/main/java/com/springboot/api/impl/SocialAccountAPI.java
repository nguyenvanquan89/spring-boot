package com.springboot.api.impl;

import com.springboot.api.ISocialAccountAPI;
import com.springboot.dto.SocialAccountDTO;
import com.springboot.entity.SocialAccountEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/social-accounts")
public class SocialAccountAPI extends BaseAPI<SocialAccountDTO, SocialAccountEntity> implements ISocialAccountAPI {
}
