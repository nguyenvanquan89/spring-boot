package com.springboot.service;

import com.springboot.entity.UserEntity;

public interface IUserService extends IBaseService<UserEntity> {
	boolean existsByUserName(String userName);
}
