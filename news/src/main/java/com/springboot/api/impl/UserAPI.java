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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        return super.create(dto, entity);
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
            @RequestParam(name = "itemPerPage", required = false) String itemPerPage,
            @RequestParam(name = "currentPage", required = false) String currentPage,
            UserDTO dto) {
        return super.findAll(itemPerPage, currentPage, dto);
    }

}
