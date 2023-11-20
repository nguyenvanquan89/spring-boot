package com.springboot.api.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.dto.RoleDTO;
import com.springboot.dto.UserDTO;
import com.springboot.entity.UserEntity;
import com.springboot.exceptions.DuplicateItemException;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.service.impl.UserService;
import com.springboot.util.MessageKeys;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;


@WebMvcTest(UserAPI.class)
class UserAPITest extends BaseAPITest<UserDTO, UserEntity> {

  @Autowired
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserAPI userAPI;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userAPI).build();
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void findUserById() throws Exception {
    //Given
    long id = 2L;
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("user@gmail.com");
    userEntity.setId(id);

    //When
    when(baseService.findOneById(anyLong())).thenReturn(userEntity);
    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/users/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andReturn();

    //Then
    verify(baseService, times(1)).findOneById(id);
  }

  @Test
  void findUserByIdNotFound() throws Exception {
    //Given
    long id = 2L;

    //When
    when(baseService.findOneById(anyLong())).thenReturn(null);
    when(localeUtils.getMessageByKey(any(), any())).thenReturn(
        "Not found item id = " + id);

    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/users/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(mvcResult -> assertTrue(
            mvcResult.getResolvedException() instanceof ResourceNotFoundException))
        .andExpect(mvcResult -> assertEquals("Not found item id = " + id,
            mvcResult.getResolvedException().getMessage()))
        .andReturn();

    //Then
    verify(baseService, times(1)).findOneById(id);
  }

  @Test
  void createUserBlank() throws Exception {
    //Given
    UserDTO userDTO = new UserDTO();

    //When
    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userDTO)))
        //.andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(mvcResult -> assertTrue(
            mvcResult.getResolvedException() instanceof MethodArgumentNotValidException));

    //Then
    verify(userService, times(0)).existsByUsername(anyString());
  }

  @Test
  void createHasErrorAllNull() throws Exception {
    //Given
    UserDTO userDTO = new UserDTO();

    //When
    // Mock the BindingResult
    MvcResult mvcResult = mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userDTO)))
        //.andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(
            result.getResolvedException() instanceof MethodArgumentNotValidException)).andReturn();

    // Get the BindingResult
    MethodArgumentNotValidException exception = (MethodArgumentNotValidException) mvcResult.getResolvedException();
    BindingResult bindingResult = exception.getBindingResult();

    assertEquals(3, bindingResult.getAllErrors().size());
    assertEquals("{userdto.username.notblank}",
        bindingResult.getFieldError("username").getDefaultMessage());
    assertEquals("{userdto.password.notnull}",
        bindingResult.getFieldError("password").getDefaultMessage());
    assertEquals("{userdto.fullname.notblank}",
        bindingResult.getFieldError("fullName").getDefaultMessage());

    //Then
    verify(userService, times(0)).existsByUsername(anyString());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "/testInputDataToUserApi.csv", numLinesToSkip = 1)
  void createHasErrorByParameter(String inputData, String fieldName, String expectMsg)
      throws Exception {
    //Given
    UserDTO userDTO = new UserDTO();
    if ("username".equals(fieldName)) {
      userDTO.setUsername(inputData);
    } else if ("password".equals(fieldName)) {
      userDTO.setPassword(inputData);
    } else if ("fullName".equals(fieldName)) {
      userDTO.setFullName(inputData);
    }

    //When
    // Mock the BindingResult
    MvcResult mvcResult = mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userDTO)))
        //.andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(result -> assertTrue(
            result.getResolvedException() instanceof MethodArgumentNotValidException)).andReturn();

    // Get the BindingResult
    MethodArgumentNotValidException exception = (MethodArgumentNotValidException) mvcResult.getResolvedException();
    BindingResult bindingResult = exception.getBindingResult();

    assertEquals(3, bindingResult.getAllErrors().size());
    assertEquals(expectMsg,
        bindingResult.getFieldError(fieldName).getDefaultMessage());

    //Then
    verify(userService, times(0)).existsByUsername(anyString());
  }

  @Test
  void createDuplicateUser() throws Exception {
    //Given
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("usernew@gmail.com");
    userDTO.setFullName("Nguyen Van A");
    userDTO.setPassword("123456");

    //When
    when(userService.existsByUsername(anyString())).thenReturn(true);
    when(localeUtils.getMessageByKey(any(), any())).thenReturn(
        "User name exits = " + userDTO.getUsername());

    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userDTO)))
        .andExpect(status().isBadRequest())
        .andExpect(mvcResult -> assertTrue(
            mvcResult.getResolvedException() instanceof DuplicateItemException))
        .andExpect(mvcResult -> assertEquals("User name exits = " + userDTO.getUsername(),
            mvcResult.getResolvedException().getMessage()));

    //Then
    verify(userService, times(1)).existsByUsername(userDTO.getUsername());
  }

  @Test
  void createUserByCustom() throws Exception {
    //Given
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("usernew@gmail.com");
    userDTO.setFullName("Nguyen Van A");
    userDTO.setPassword("123456");
    String msgRegisterSuccess = "Register is successfully, login please !";
    //When
    when(userService.existsByUsername(anyString())).thenReturn(false);
    when(localeUtils.getMessageByKey(MessageKeys.USER_REGISTER_SUCCESS, null)).thenReturn(
        msgRegisterSuccess);

    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.username").value("usernew@gmail.com"))
        .andExpect(jsonPath("$.message").value(msgRegisterSuccess));

    //Then
    verify(userService, times(1)).existsByUsername(userDTO.getUsername());
    verify(baseService, times(1)).save(any());
  }


  @Test
  void createUserByAdmin() throws Exception {
    //Given
    UserDTO userDTO = new UserDTO();
    userDTO.setUsername("usernew@gmail.com");
    userDTO.setFullName("Nguyen Van A");
    userDTO.setPassword("123456");
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setCode("Editor");
    roleDTO.setId(2L);
    List<RoleDTO> lstRole = Collections.singletonList(roleDTO);
    userDTO.setRoles(lstRole);
    String msgRegisterSuccess = "Register is successfully, login please !";
    //When
    when(userService.existsByUsername(anyString())).thenReturn(false);
    when(localeUtils.getMessageByKey(MessageKeys.USER_REGISTER_SUCCESS, null)).thenReturn(
        msgRegisterSuccess);

    mockMvc.perform(post("/api/users/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.username").value("usernew@gmail.com"))
        .andExpect(jsonPath("$.data.roles[0].code").value("Editor"))
        .andExpect(jsonPath("$.message").value(msgRegisterSuccess));

    //Then
    verify(userService, times(1)).existsByUsername(userDTO.getUsername());
    verify(baseService, times(1)).save(any());
  }

  @Test
  void update() throws Exception {
    //Given
    UserDTO userDTO = new UserDTO();
    userDTO.setId(2L);
    userDTO.setUsername("user@gmail.com");
    userDTO.setFullName("Nguyen Van A");
    userDTO.setPassword("123456");
    String msgRegisterSuccess = "Update is successfully";
    //When
    when(localeUtils.getMessageByKey(MessageKeys.UPDATE_SUCCESS, null)).thenReturn(
        msgRegisterSuccess);

    mockMvc.perform(put("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userDTO)))
        //.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.fullName").value(userDTO.getFullName()))
        .andExpect(jsonPath("$.message").value(msgRegisterSuccess));

    //Then
    verify(baseService, times(1)).save(any());

  }

  @Test
  void findAllByPageableASC() throws Exception {
    //Given
    int currentPage = 1;
    int itemPerPage = 12;
    String order = "ASC";
    String orderColumn = "id";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("currentPage", String.valueOf(currentPage));
    params.add("itemPerpage", String.valueOf(itemPerPage));
    params.add("order", order);
    params.add("orderColumn", orderColumn);

    List<UserDTO> userDTOList = LongStream.range(1, 12).mapToObj(i -> {
      UserDTO userDTO = new UserDTO();
      userDTO.setId(i);
      userDTO.setUsername("user" + userDTO.getId() + "@gmail.com");
      userDTO.setPassword("pass" + userDTO.getId());
      return userDTO;
    }).collect(Collectors.toList());

    List<UserEntity> userEntityList = LongStream.range(1, 15).mapToObj(i -> {
      UserEntity userEntity = new UserEntity();
      userEntity.setId(i);
      userEntity.setUsername("user" + userEntity.getId() + "@gmail.com");
      userEntity.setPassword("pass" + userEntity.getId());
      return userEntity;
    }).collect(Collectors.toList());

    //Set content and total page
    Page<UserEntity> page = new PageImpl<>(userEntityList, null, userEntityList.size());

    //When
    when(baseService.findAllByPageable(any())).thenReturn(page);
    when(mappingUtils.mapList(page.getContent(), UserDTO.class)).thenReturn(userDTOList);

    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .params(params))
        //.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currentPage").value(1))
        .andExpect(jsonPath("$.itemPerPage").value(12))
        .andExpect(jsonPath("$.totalRecord").value(14))
        .andExpect(jsonPath("$.lstResult[0].username").value(userDTOList.get(0).getUsername()));

    //Then
    verify(baseService, times(1)).findAllByPageable(any());
  }


  @Test
  void findAllByPageableDESC() throws Exception {
    //Given
    int currentPage = 1;
    int itemPerPage = 12;
    String order = "DESC";
    String orderColumn = "id";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("currentPage", String.valueOf(currentPage));
    params.add("itemPerpage", String.valueOf(itemPerPage));
    params.add("order", order);
    params.add("orderColumn", orderColumn);

    List<UserDTO> userDTOList = LongStream.range(1, 12).mapToObj(i -> {
      UserDTO userDTO = new UserDTO();
      userDTO.setId(12 - i);
      userDTO.setUsername("user" + userDTO.getId() + "@gmail.com");
      userDTO.setPassword("pass" + userDTO.getId());
      return userDTO;
    }).collect(Collectors.toList());

    List<UserEntity> userEntityList = LongStream.range(1, 15).mapToObj(i -> {
      UserEntity userEntity = new UserEntity();
      userEntity.setId(15 - i);
      userEntity.setUsername("user" + userEntity.getId() + "@gmail.com");
      userEntity.setPassword("pass" + userEntity.getId());
      return userEntity;
    }).collect(Collectors.toList());

    //Set content and total page
    Page<UserEntity> page = new PageImpl<>(userEntityList, null, userEntityList.size());

    //When
    when(baseService.findAllByPageable(any())).thenReturn(page);
    when(mappingUtils.mapList(page.getContent(), UserDTO.class)).thenReturn(userDTOList);

    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .params(params))
        //.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currentPage").value(1))
        .andExpect(jsonPath("$.itemPerPage").value(12))
        .andExpect(jsonPath("$.totalRecord").value(14))
        .andExpect(jsonPath("$.lstResult[0].username").value(userDTOList.get(0).getUsername()));

    //Then
    verify(baseService, times(1)).findAllByPageable(any());
  }

  @Test
  void findAllByPageableNoData() throws Exception {
    //Given
    int currentPage = 1;
    int itemPerPage = 12;
    String order = "DESC";
    String orderColumn = "id";

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("currentPage", String.valueOf(currentPage));
    params.add("itemPerpage", String.valueOf(itemPerPage));
    params.add("order", order);
    params.add("orderColumn", orderColumn);

    List<UserDTO> userDTOList = new ArrayList<>();

    List<UserEntity> userEntityList = new ArrayList<>();

    //Set content and total page
    Page<UserEntity> page = new PageImpl<>(userEntityList, null, 0);

    //When
    when(baseService.findAllByPageable(any())).thenReturn(page);
    when(mappingUtils.mapList(page.getContent(), UserDTO.class)).thenReturn(userDTOList);

    mockMvc.perform(get("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .params(params))
        //.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currentPage").value(1))
        .andExpect(jsonPath("$.itemPerPage").value(12))
        .andExpect(jsonPath("$.totalRecord").value(0));

    //Then
    verify(baseService, times(1)).findAllByPageable(any());
  }

  @Test
  void deleteUser() throws Exception {
    //Given
    List<Long> ids = Arrays.asList(1L, 2L, 3L);

    //When
    doNothing().when(baseService).delete(ids);
    when(localeUtils.getMessageByKey(any(), any())).thenReturn(
        "Deleted data successfully");
    // Perform the DELETE request
    mockMvc.perform(delete("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(ids)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(jsonPath("$.message").value("Deleted data successfully"));

    //Then
    verify(baseService, times(1)).delete(ids);
  }

}