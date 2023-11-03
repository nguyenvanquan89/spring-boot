package com.springboot.dto;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class JwtRequest {

    @NotNull(message = "{userdto.username.notblank}")
    @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}", message = "{userdto.username.notblank}")
    private String username;

    @NotNull(message = "{userdto.password.notnull}")
    @Size(min = 6, max = 40, message = "{userdto.password.notblank}")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
