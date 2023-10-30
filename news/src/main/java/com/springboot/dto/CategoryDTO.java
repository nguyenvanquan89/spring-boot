package com.springboot.dto;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

public class CategoryDTO extends BaseDTO<CategoryDTO> {
	

	@NotBlank(message="{categorydto.name.notblank}")
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	private String name;
	
	@NotBlank(message="{categorydto.code.notblank}")
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	private String code;
	
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	@JsonIgnoreProperties("category")
	private List<NewsDTO> news = new ArrayList<>();

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

	public List<NewsDTO> getNews() {
		return news;
	}

	public void setNews(List<NewsDTO> news) {
		this.news = news;
	}

}
