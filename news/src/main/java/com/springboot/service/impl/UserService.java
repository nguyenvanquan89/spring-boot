package com.springboot.service.impl;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.entity.RoleEntity;
import com.springboot.entity.UserEntity;
import com.springboot.repository.UserRepository;
import com.springboot.service.IUserService;

@Service
public class UserService extends BaseService<UserEntity> implements IUserService {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserEntity save(UserEntity entity) {
		UserEntity newUserEntity = new UserEntity();
		String passwordEncoded = passwordEncoder.encode(entity.getPassword());
		entity.setPassword(passwordEncoded);
		modelMapper.map(entity, newUserEntity);
		newUserEntity.getRoles().clear();
		newUserEntity.getRoles().addAll(entity
				.getRoles()
				.stream()
				.map(r -> {
					RoleEntity role = roleService.findOneById(r.getId());
					if(role != null) {role.getUsers().add(newUserEntity);}
					return role;
				}).collect(Collectors.toList()));
		return userRepo.save(newUserEntity);
	}

	@Override
	public boolean existsByUserName(String userName) {
		return userRepo.findOneByUserName(userName) != null;
	}

	

}
