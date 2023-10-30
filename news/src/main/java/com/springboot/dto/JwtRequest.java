package com.springboot.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class JwtRequest {

	@NotNull(message="{userdto.username.notblank}")
	@Email(regexp="^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}", message="{userdto.username.notblank}")
	private String userName;

	@NotNull(message="{userdto.password.notnull}")
	@Size(min=6, max=40, message = "{userdto.password.notblank}")
	private String passWord;

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

}
