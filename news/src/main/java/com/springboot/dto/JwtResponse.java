package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class JwtResponse {

	@JsonView(value= {Views.LoginView.class})
	private UserDTO user;
	@JsonView(value= {Views.LoginView.class})
	private String jwtToken;

	
	public JwtResponse(UserDTO user, String jwtToken) {
		super();
		this.user = user;
		this.jwtToken = jwtToken;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

}
