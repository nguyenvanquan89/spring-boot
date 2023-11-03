package com.springboot.repository;

import com.springboot.entity.UserEntity;

public interface UserRepository extends BaseRepository<UserEntity> {
	UserEntity findOneByUsernameAndStatus(String username, int status);
	UserEntity findOneByUsername(String username);
}
