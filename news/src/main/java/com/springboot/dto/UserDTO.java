package com.springboot.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;



public class UserDTO extends BaseDTO<UserDTO> {

	@NotNull(message="{userdto.password.notblank}")
	@Email(regexp="^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}", message="{userdto.username.notblank}")
	@JsonView(value = { Views.CommonView.class })
	private String userName;

	@NotNull(message="{userdto.password.notnull}")
	@Size(min=6, max=40, message = "{userdto.password.notblank}")
	@JsonView(value = { Views.CommonView.class })
	private String passWord;

	@NotBlank(message="{userdto.fullname.notblank}")
	@JsonView(value = { Views.CommonView.class })
	private String fullName;

	@JsonView(value = { Views.CommonView.class })
	private int status;

	@JsonView(value = { Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class, Views.LoginView.class })
	@JsonIgnoreProperties("users")
	private List<RoleDTO> roles = new ArrayList<>();
	
	@JsonView(value = { Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class })
	@JsonIgnoreProperties("user")
	private List<CommentDTO> comments = new ArrayList<>();

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleDTO> roles) {
		this.roles = roles;
	}

	public List<CommentDTO> getComments() {
		return comments;
	}

	public void setComments(List<CommentDTO> comments) {
		this.comments = comments;
	}

}
