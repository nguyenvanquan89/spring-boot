package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.Date;

public class TokenDTO extends BaseDTO<TokenDTO> {

    @JsonView(value = {Views.CommonView.class})
    private String token;

    @JsonView(value = {Views.CommonView.class})
    private String tokenType;

    @JsonView(value = {Views.CommonView.class})
    private Date expirationDate;

    @JsonView(value = {Views.CommonView.class})
    private int revoked;

    @JsonView(value = {Views.CommonView.class})
    private int expired;

    @JsonView(value = {Views.CommonView.class})
    private UserDTO user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getRevoked() {
        return revoked;
    }

    public void setRevoked(int revoked) {
        this.revoked = revoked;
    }

    public int getExpired() {
        return expired;
    }

    public void setExpired(int expired) {
        this.expired = expired;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
