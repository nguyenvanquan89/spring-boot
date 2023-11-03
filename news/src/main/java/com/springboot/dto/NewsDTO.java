package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class NewsDTO extends BaseDTO<NewsDTO> {

    @NotBlank(message = "{newsdto.title.notblank}")
    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    private String title;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    private String thumbnail;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    private String shortDescription;

    @NotBlank(message = "{newsdto.content.notblank}")
    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    private String content;
	
	/*@NotNull(message="{newsdto.categoryid.notblank}")
	@Digits(message="{newsdto.categoryid.notdigist}", fraction = 0, integer = 2)
	@JsonView(value= {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
	private Long categoryId;*/

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    @JsonIgnoreProperties("news")
    private CategoryDTO category;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    @JsonIgnoreProperties("news")
    private List<CommentDTO> comments;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

}
