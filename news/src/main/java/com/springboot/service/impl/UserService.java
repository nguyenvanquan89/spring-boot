package com.springboot.service.impl;

import com.springboot.entity.RoleEntity;
import com.springboot.entity.UserEntity;
import com.springboot.repository.UserRepository;
import com.springboot.service.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

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

	@Value("${role.default.register.user}")
	private String defautRole;
	
	@Override
	public UserEntity save(UserEntity entity) {
		UserEntity newUserEntity = new UserEntity();
		String passwordEncoded = passwordEncoder.encode(entity.getPassword());
		entity.setPassword(passwordEncoded);
		//If not set role then add default role
		if(entity.getRoles() == null || entity.getRoles().isEmpty()) {
			RoleEntity roleEntity = roleService.findOneByCode(defautRole);
			entity.setRoles(Collections.singletonList(roleEntity));
		}

		modelMapper.map(entity, newUserEntity);
		newUserEntity.getRoles().clear();
		newUserEntity.getRoles().addAll(entity
				.getRoles()
				.stream()
				.map(r -> mapRole(newUserEntity, r))
				.collect(Collectors.toList()));
		return userRepo.save(newUserEntity);
	}

	private RoleEntity mapRole(UserEntity newUserEntity, RoleEntity r) {
		RoleEntity role = roleService.findOneById(r.getId());
		if(role != null) {
			role.getUsers().add(newUserEntity);
		}

		return role;
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepo.findOneByUsername(username) != null;
	}

	@Override
	public UserEntity findOneByUsername(String username) {
		return userRepo.findOneByUsername(username);
	}

}
