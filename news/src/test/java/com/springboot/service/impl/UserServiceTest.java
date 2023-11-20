package com.springboot.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.springboot.entity.RoleEntity;
import com.springboot.entity.UserEntity;
import com.springboot.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock
  private RoleService roleService;

  @Mock
  private UserRepository userRepo;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void saveByAdmin() {
    //Given
    UserEntity data = new UserEntity();
    data.setId(1L);
    data.setUsername("editor@gmail.com");
    data.setPassword("123456");

    RoleEntity defaultRole = new RoleEntity();
    defaultRole.setId(1L);
    defaultRole.setCode("EDITOR");
    List<UserEntity> lstUser = new ArrayList<>();
    lstUser.add(data);
    defaultRole.setUsers(lstUser);

    List<RoleEntity> lstRole = new ArrayList<>();
    lstRole.add(defaultRole);
    data.setRoles(lstRole);

    //When
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(roleService.findOneById(anyLong())).thenReturn(defaultRole);
    when(userRepo.save(any(UserEntity.class))).thenReturn(data);

    UserEntity actual = userService.save(data);
    assertEquals(1L, actual.getId());
    assertEquals("editor@gmail.com", actual.getUsername());
    assertEquals("encodedPassword", actual.getPassword());

    //Then
    verify(userRepo, times(1)).save(data);
    verifyNoMoreInteractions(userRepo);

  }

  @Test
  void registerByUser() {
    //Given
    UserEntity data = new UserEntity();
    data.setId(1L);
    data.setUsername("user@gmail.com");
    data.setPassword("123456");

    RoleEntity defaultRole = new RoleEntity();
    defaultRole.setId(1L);
    defaultRole.setCode("USER");
    List<UserEntity> lstUser = new ArrayList<>();
    lstUser.add(data);
    defaultRole.setUsers(lstUser);

    //When
    when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    when(roleService.findOneByCode(any())).thenReturn(defaultRole);
    when(userRepo.save(any(UserEntity.class))).thenReturn(data);

    UserEntity actual = userService.save(data);
    assertEquals(1L, actual.getId());
    assertEquals("user@gmail.com", actual.getUsername());
    assertEquals("encodedPassword", actual.getPassword());
    assertEquals("USER", actual.getRoles().get(0).getCode());

    //Then
    verify(userRepo, times(1)).save(data);
    verifyNoMoreInteractions(userRepo);

  }

  @Test
  void delete() {
    //Given
    List<Long> ids = new ArrayList<>();
    ids.add(1L);
    ids.add(2L);

    //When
    doNothing().when(userRepo).delete(1L);
    doNothing().when(userRepo).delete(2L);
    userService.delete(ids);

    //Then
    verify(userRepo, times(1)).delete(ids.get(0));
    verify(userRepo, times(1)).delete(ids.get(1));

  }

  @Test
  void findAllByPageable() {
    //Given
    List<UserEntity> userEntityList = LongStream.range(1, 11).mapToObj(i -> {
      UserEntity userEntity = new UserEntity();
      userEntity.setId(i);
      userEntity.setUsername("user" + i + "@gmail.com");
      userEntity.setPassword("pass" + i);
      return userEntity;
    }).collect(Collectors.toList());

    //When
    when(userRepo.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(userEntityList));
    Pageable pageable = new PageRequest(0, 5);
    Page<UserEntity> actual = userService.findAllByPageable(pageable);
    assertEquals(10, actual.getTotalElements());

    List<UserEntity> actualEntity = actual.getContent();
    assertEquals(1, actualEntity.get(0).getId());
    assertEquals("user1@gmail.com", actualEntity.get(0).getUsername());

    //Then
    verify(userRepo, times(1)).findAll(pageable);
    verifyNoMoreInteractions(userRepo);
  }

  @Test
  void findAllByPageableNoData() {
    //Given
    List<UserEntity> userEntityList = new ArrayList<>();

    //When
    when(userRepo.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<>(userEntityList));
    Pageable pageable = new PageRequest(0, 5);
    Page<UserEntity> actual = userService.findAllByPageable(pageable);
    assertEquals(0, actual.getTotalElements());

    List<UserEntity> actualEntity = actual.getContent();
    assertEquals(0, actualEntity.size());

    //Then
    verify(userRepo, times(1)).findAll(pageable);
    verifyNoMoreInteractions(userRepo);
  }

  @Test
  void findOneById() {

    //Given
    long id = 1L;
    String passwordEncode = "encodedPassword";
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("admin@gmail.com");
    userEntity.setPassword(passwordEncode);
    userEntity.setFullName("Nguyen Van Quan");

    //When
    when(userRepo.findOne(id)).thenReturn(userEntity);
    when(passwordEncoder.encode(any(String.class))).thenReturn(passwordEncode);
    UserEntity actual = userService.findOneById(id);
    assertEquals("admin@gmail.com", actual.getUsername());
    assertEquals(passwordEncoder.encode("123456"), actual.getPassword());
    assertEquals("Nguyen Van Quan", actual.getFullName());

    //Then
    verify(userRepo, times(1)).findOne(id);
    verifyNoMoreInteractions(userRepo);
  }

  @Test
  void findOneByIdExpectNull() {

    //Given
    long id = 1L;
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("admin@gmail.com");
    userEntity.setPassword("123456");
    userEntity.setFullName("Nguyen Van Quan");

    //When
    when(userRepo.findOne(id)).thenReturn(userEntity);
    UserEntity actual = userService.findOneById(2L);
    assertNull(actual);

    //Then
    verify(userRepo, times(1)).findOne(2L);
    verifyNoMoreInteractions(userRepo);
  }

  @Test
  void existsByUsername() {
    //Given
    String username = "admin@gmail.com";
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername(username);
    userEntity.setPassword("123456");
    userEntity.setFullName("Nguyen Van Quan");
    when(userRepo.findOneByUsername(username)).thenReturn(userEntity);

    //when
    boolean actual = userService.existsByUsername(username);
    assertTrue(actual);
    //Then
    verify(userRepo, times(1)).findOneByUsername(username);
    verifyNoMoreInteractions(userRepo);
  }

  @Test
  void notExistsByUsername() {
    //Given
    String username = "admin";
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("admin@gmail.com");
    userEntity.setPassword("123456");
    userEntity.setFullName("Nguyen Van Quan");
    when(userRepo.findOneByUsername("admin@gmail.com")).thenReturn(userEntity);
    //when
    boolean actual = userService.existsByUsername(username);
    assertFalse(actual);
    //Then
    verify(userRepo, times(1)).findOneByUsername(username);
    verifyNoMoreInteractions(userRepo);
  }

  @Test
  void findOneByUsername() {
    //Given
    String username = "admin@gmail.com";
    UserEntity userEntity = new UserEntity();
    userEntity.setUsername("admin@gmail.com");
    userEntity.setPassword("123456");
    userEntity.setFullName("Nguyen Van Quan");
    when(userRepo.findOneByUsername(anyString())).thenReturn(userEntity);

    //When
    UserEntity actual = userService.findOneByUsername(username);
    assertEquals("admin@gmail.com", actual.getUsername());
    assertEquals("123456", actual.getPassword());
    assertEquals("Nguyen Van Quan", actual.getFullName());

    //Then
    verify(userRepo, times(1)).findOneByUsername(username);
    verifyNoMoreInteractions(userRepo);
  }

  @Test
  void findOneByUsernameExpectNull() {
    //Given
    String username = "admin22@gmail.com";
    when(userRepo.findOneByUsername(anyString())).thenReturn(null);

    //When
    UserEntity actual = userService.findOneByUsername(username);
    assertNull(actual);

    //Then
    verify(userRepo, times(1)).findOneByUsername(username);
    verifyNoMoreInteractions(userRepo);
  }
}