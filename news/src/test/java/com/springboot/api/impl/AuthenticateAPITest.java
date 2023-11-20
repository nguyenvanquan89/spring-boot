package com.springboot.api.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.dto.JwtRequest;
import com.springboot.dto.JwtResponse;
import com.springboot.dto.UserDTO;
import com.springboot.service.impl.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

class AuthenticateAPITest {

  private MockMvc mockMvc;

  @Mock
  private JwtService jwtService;

  @InjectMocks
  private AuthenticateAPI authenticateAPI;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(authenticateAPI).build();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void authenticateAndCreateJwtToken() throws Exception {
    // Given
    JwtRequest jwtRequest = new JwtRequest();
    jwtRequest.setUsername("admin@gmail.com");
    jwtRequest.setPassword("1234563");

    UserDTO user = new UserDTO();
    user.setUsername("admin@gmail.com");
    String token = "valueOfTokenKey";
    JwtResponse jwtResponse = new JwtResponse(user, token);

    //2. define behavior of Repository
    when(jwtService.authenticateAndCreateJwt(any())).thenReturn(jwtResponse);

    // When
    mockMvc.perform(post("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .content(new ObjectMapper().writeValueAsString(jwtRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.jwtToken").value(token));

    // Then
    verify(jwtService, times(1)).authenticateAndCreateJwt(any());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/testInputDataToAuthenticateApi.csv", numLinesToSkip = 1)
  void authenticateHasErrorByParameter(String inputData, String fieldName, String expectMsg)
      throws Exception {
    //Given
    JwtRequest jwtRequest = new JwtRequest();
    if ("username".equals(fieldName)) {
      jwtRequest.setUsername(inputData);
    } else if ("password".equals(fieldName)) {
      jwtRequest.setPassword(inputData);
    }

    //When
    // Mock the BindingResult
    MvcResult mvcResult = mockMvc.perform(post("/api/authenticate")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(jwtRequest)))
        //.andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(
            result.getResolvedException() instanceof MethodArgumentNotValidException)).andReturn();

    // Get the BindingResult
    MethodArgumentNotValidException exception = (MethodArgumentNotValidException) mvcResult.getResolvedException();
    BindingResult bindingResult = exception.getBindingResult();

    assertEquals(2, bindingResult.getAllErrors().size());
    assertEquals(expectMsg,
        bindingResult.getFieldError(fieldName).getDefaultMessage());

    //Then
    verify(jwtService, times(0)).authenticateAndCreateJwt(any());
  }

}