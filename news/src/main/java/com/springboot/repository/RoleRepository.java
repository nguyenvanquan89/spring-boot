package com.springboot.repository;

import com.springboot.entity.RoleEntity;

public interface RoleRepository extends BaseRepository<RoleEntity> {
    RoleEntity findOneByCode(String code);
}
