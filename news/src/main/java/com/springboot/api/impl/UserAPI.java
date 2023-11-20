package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IUserAPI;
import com.springboot.dto.UserDTO;
import com.springboot.dto.Views;
import com.springboot.entity.UserEntity;
import com.springboot.exceptions.DuplicateItemException;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.service.IUserService;
import com.springboot.util.Constants;
import com.springboot.util.LocaleUtils;
import com.springboot.util.MessageKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserAPI extends BaseAPI<UserDTO, UserEntity> implements IUserAPI {


  private final IUserService userService;

  private final LocaleUtils localeUtils;

  private static final Logger loggerUserAPI = LoggerFactory.getLogger(UserAPI.class);

  @Autowired
  public UserAPI(IUserService userService, LocaleUtils localeUtils) {
    this.userService = userService;
    this.localeUtils = localeUtils;
  }

  @Override
  @GetMapping("/{id}")
  @JsonView(Views.SearchView.class)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDTO> findById(
      @PathVariable("id") Long id,
      UserDTO dto) throws ResourceNotFoundException {
    return super.findById(id, dto);
  }

  @Override
  @PostMapping("/register")
  @JsonView(Views.AddNewView.class)
  public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody UserDTO dto,
      UserEntity entity) throws Exception {
    String userName = dto.getUsername();
    if (userService.existsByUsername(userName)) {
      Object[] obj = {userName};
      String msg = localeUtils.getMessageByKey(MessageKeys.USERNAME_EXIST, obj);
      loggerUserAPI.error(Constants.ERROR_MESSAGE_LOGGER, msg);
      throw new DuplicateItemException(msg);
    }
    //Replace message response to client
    ResponseEntity<Map<String, Object>> response = super.create(dto, entity);
    Map<String, Object> body = response.getBody();
    body.replace("message", localeUtils.getMessageByKey(MessageKeys.USER_REGISTER_SUCCESS, null));
    return response;
  }

  @Override
  @PutMapping
  @JsonView(Views.UpdateView.class)
  public ResponseEntity<Map<String, Object>> update(@Valid @RequestBody UserDTO dto,
      UserEntity entity) {
    return super.update(dto, entity);
  }

  @Override
  @GetMapping
  @JsonView(Views.SearchView.class)
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Map<String, Object>> findAll(
      @RequestParam(defaultValue = "12") int itemPerPage,
      @RequestParam(defaultValue = "1") int currentPage,
      @RequestParam(required = false, defaultValue = "DESC") String order,
      @RequestParam(required = false, defaultValue = "modifiedDate") String orderColumn,
      UserDTO dto) {
    return super.findAll(itemPerPage, currentPage, order, orderColumn, dto);
  }

}
