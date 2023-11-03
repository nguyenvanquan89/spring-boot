package com.springboot.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "User")
public class UserEntity extends BaseEntity {

    @Column(name = "username", updatable = false, nullable = false)
    private String username;

    @Column(name = "passWord", nullable = false)
    private String password;

    @Column(name = "fullName")
    private String fullName;

    @Column(nullable = false)
    private int status;

    @Column(name = "facebookUserId")
    private String facebookUserId;

    @Column(name = "googleUserId")
    private String googleUserId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "roleId"})})
    private List<RoleEntity> roles;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SocialAccountEntity> socialAccounts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TokenEntity> tokens;


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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<SocialAccountEntity> getSocialAccounts() {
        return socialAccounts;
    }

    public void setSocialAccounts(List<SocialAccountEntity> socialAccounts) {
        this.socialAccounts = socialAccounts;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    public String getGoogleUserId() {
        return googleUserId;
    }

    public void setGoogleUserId(String googleUserId) {
        this.googleUserId = googleUserId;
    }

    public List<TokenEntity> getTokens() {
        return tokens;
    }

    public void setTokens(List<TokenEntity> tokens) {
        this.tokens = tokens;
    }


}
