package com.springboot.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.util.IBaseModelMapper;

@JsonInclude(value = Include.NON_NULL)
public class BaseDTO implements IBaseModelMapper {

  @JsonView(value = {Views.CommonView.class})
  private Long id;

  @JsonView(value = {Views.AddNewView.class, Views.SearchView.class})
  private Date createdDate;

  @JsonView(value = {Views.UpdateView.class, Views.SearchView.class})
  private Date modifiedDate;

  @JsonView(value = {Views.AddNewView.class, Views.SearchView.class})
  private String createdBy;

  @JsonView(value = {Views.UpdateView.class, Views.SearchView.class})
  private String modifiedBy;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

}
