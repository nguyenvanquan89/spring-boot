package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.entity.UserEntity;
import com.springboot.util.MappingUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


public class UserDTO extends BaseDTO<UserDTO> {

    @NotNull(message = "{userdto.password.notblank}")
    @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9-]+\\.[a-zA-Z.]{2,5}", message = "{userdto.username.notblank}")
    @JsonView(value = {Views.CommonView.class})
    private String username;

    @NotNull(message = "{userdto.password.notnull}")
    @Size(min = 6, max = 40, message = "{userdto.password.notblank}")
    @JsonView(value = {Views.CommonView.class})
    private String password;

    @NotBlank(message = "{userdto.fullname.notblank}")
    @JsonView(value = {Views.CommonView.class})
    private String fullName;

    @JsonView(value = {Views.CommonView.class})
    private int status;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    private String facebookUserId;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    private String googleUserId;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class, Views.LoginView.class})
    @JsonIgnoreProperties("users")
    private List<RoleDTO> roles;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    @JsonIgnoreProperties("user")
    private List<CommentDTO> comments;



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

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
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


    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(userMap(utils));
        return mapper;
    }

    public PropertyMap<UserEntity, UserDTO> userMap(MappingUtils utils) {
        return new PropertyMap<UserEntity, UserDTO>() {
            @Override
            protected void configure() {
                Converter<UserEntity, List<RoleDTO>> mapUser = new AbstractConverter<UserEntity, List<RoleDTO>>() {
                    @Override
                    protected List<RoleDTO> convert(UserEntity userEntity) {
                        return utils.mapList(new ArrayList<>(userEntity.getRoles()), RoleDTO.class);
                    }
                };
                //Mapping role to user
                using(mapUser).map(source, destination.getRoles());
            }
        };
    }
}
