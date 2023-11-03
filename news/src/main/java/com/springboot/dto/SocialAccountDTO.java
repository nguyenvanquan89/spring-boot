package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonView;

public class SocialAccountDTO extends BaseDTO<SocialAccountDTO> {

    @JsonView(value = {Views.CommonView.class})
    private String provider;

    @JsonView(value = {Views.CommonView.class})
    private String providerId;

    @JsonView(value = {Views.CommonView.class})
    private String email;

    @JsonView(value = {Views.CommonView.class})
    private String name;

    @JsonView(value = {Views.CommonView.class})
    private UserDTO user;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
