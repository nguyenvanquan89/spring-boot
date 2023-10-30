package com.springboot.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

public class RoleDTO extends BaseDTO<RoleDTO> {

	@NotBlank(message="{roledto.name.notblank}")
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	private String name;
	
	@NotBlank(message="{roledto.code.notblank}")
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class, Views.LoginView.class})
	private String code;
	
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	@JsonIgnoreProperties("roles")
	private List<UserDTO> users;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}

}
