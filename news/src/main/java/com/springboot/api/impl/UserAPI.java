package com.springboot.api.impl;

import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.api.IUserAPI;
import com.springboot.dto.UserDTO;
import com.springboot.dto.Views;
import com.springboot.entity.UserEntity;
import com.springboot.exceptions.DuplicateItemException;
import com.springboot.exceptions.ResourceNotFoundException;
import com.springboot.service.IUserService;
import com.springboot.util.LocaleUtils;
import com.springboot.util.MessageKeys;
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

    @Autowired
    private IUserService userService;

    @Autowired
    private LocaleUtils localeUtils;

    @Override
    @GetMapping("/{id}")
    @JsonView(Views.SearchView.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findById(
            @PathVariable("id") Long id,
            UserDTO dto, UserEntity entity) throws ResourceNotFoundException {
        return super.findById(id, dto, entity);
    }

    @Override
    @PostMapping("/register")
    @JsonView(Views.AddNewView.class)
    public ResponseEntity<?> create(@Valid @RequestBody UserDTO dto,
                                    UserEntity entity) throws Exception {
        String userName = dto.getUsername();
        if (userService.existsByUsername(userName)) {
            Object[] obj = {userName};
            throw new DuplicateItemException(localeUtils.getMessageByKey(MessageKeys.USERNAME_EXIST, obj));
        }
        //Replace message response to client
        ResponseEntity<?> response = super.create(dto, entity);
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        body.replace("message", localeUtils.getMessageByKey(MessageKeys.USER_REGISTER_SUCCESS, null));
        return response;
    }

    @Override
    @PutMapping
    @JsonView(Views.UpdateView.class)
    public ResponseEntity<?> update(@Valid @RequestBody UserDTO dto,
                                    UserEntity entity) {
        return super.update(dto, entity);
    }

    @Override
    @GetMapping
    @JsonView(Views.SearchView.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> findAll(
            @RequestParam(defaultValue = "12") int itemPerPage,
            @RequestParam(defaultValue = "1") int currentPage,
            UserDTO dto) {
        return super.findAll(itemPerPage, currentPage, dto);
    }

}
