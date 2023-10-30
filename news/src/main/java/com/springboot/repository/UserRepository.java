package com.springboot.repository;

import com.springboot.entity.UserEntity;

public interface UserRepository extends BaseRepository<UserEntity> {
	UserEntity findOneByUserNameAndStatus(String name, int status);
	UserEntity findOneByUserName(String userName);
}
