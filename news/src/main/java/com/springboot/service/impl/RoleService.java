package com.springboot.service.impl;

import com.springboot.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.entity.RoleEntity;
import com.springboot.service.IRoleService;

@Service
public class RoleService extends BaseService<RoleEntity> implements IRoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public RoleEntity findOneByCode(String code) {
        return roleRepository.findOneByCode(code);
    }
}
