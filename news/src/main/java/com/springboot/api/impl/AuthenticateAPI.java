package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.dto.JwtRequest;
import com.springboot.dto.JwtResponse;
import com.springboot.dto.Views;
import com.springboot.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticateAPI {

  private final JwtService jwtService;

  @Autowired
  public AuthenticateAPI(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @PostMapping({"/authenticate"})
  @JsonView(Views.LoginView.class)
  public ResponseEntity<JwtResponse> authenticateAndCreateJwtToken(
      @Valid @RequestBody JwtRequest jwtRequest) {
    return new ResponseEntity<>(jwtService.authenticateAndCreateJwt(jwtRequest), HttpStatus.OK);

  }

}
