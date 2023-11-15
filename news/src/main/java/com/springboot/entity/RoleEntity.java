package com.springboot.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Role")
public class RoleEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    /**
     * delete role then delete all user have relationship
     */
    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private List<UserEntity> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

}
