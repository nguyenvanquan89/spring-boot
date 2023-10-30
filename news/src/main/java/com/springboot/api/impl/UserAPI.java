package com.springboot.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IUserAPI;
import com.springboot.dto.UserDTO;
import com.springboot.dto.Views;
import com.springboot.entity.UserEntity;
import com.springboot.service.IUserService;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserAPI extends BaseAPI<UserDTO, UserEntity> implements IUserAPI{

	@Autowired
	private IUserService userService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	@PostMapping
	@JsonView(Views.AddNewView.class)
	public ResponseEntity<?> create(@Valid @RequestBody UserDTO dto, HttpServletRequest request, UserEntity entity) {
		String userName = dto.getUserName();
		if(userService.existsByUserName(userName)) {
			Object[] objs = {userName};
			String msg = messageSource.getMessage("msg.username.exist", objs, request.getLocale());
			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", new Date());
			body.put("status", HttpStatus.BAD_REQUEST);
			body.put("errors", msg);
			
			return new ResponseEntity<>(body, HttpStatus.IM_USED);
		}
		return super.create(dto, request, entity);
	}

	@Override
	@PutMapping
	@JsonView(Views.UpdateView.class)
	public ResponseEntity<?> update(@Valid @RequestBody UserDTO dto, HttpServletRequest request, UserEntity entity){
		return super.update(dto, request, entity);
	}

	@Override
	@GetMapping
	@JsonView(Views.SearchView.class)
	public ResponseEntity<?> findAll(@RequestParam(name = "itemPerPage", required = false) String itemPerPage,
			@RequestParam(name = "currentPage", required = false) String currentPage, @RequestBody UserDTO dto,
			HttpServletRequest request) {
		return super.findAll(itemPerPage, currentPage, dto, request);
	}
	
	

}
