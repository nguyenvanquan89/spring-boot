package com.springboot.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;

@JsonInclude(value=Include.NON_NULL)
public class BaseDTO<T> {
	
	@JsonView(value= {Views.CommonView.class})
	private Long id;
	
	@JsonView(value= {Views.AddNewView.class, Views.SearchView.class})
	private Date createdDate;
	
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	private Date modifiedDate;
	
	@JsonView(value= {Views.AddNewView.class, Views.SearchView.class})
	private String createdBy;
	
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	private String modifiedBy;
	
	@JsonView(value= {Views.DeleteView.class})
	private long[] lstId;
	
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.DeleteView.class})
	private String message;
	
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
	public long[] getLstId() {
		return lstId;
	}
	public void setLstId(long[] lstId) {
		this.lstId = lstId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
