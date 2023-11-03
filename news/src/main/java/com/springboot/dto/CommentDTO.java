package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.NotBlank;

public class CommentDTO extends BaseDTO<CommentDTO> {

    @NotBlank(message = "{commentdto.content.notblank}")
    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    private String content;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    @JsonIgnoreProperties("comments")
    private UserDTO user;

    @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
    @JsonIgnoreProperties("comments")
    private NewsDTO news;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public NewsDTO getNews() {
        return news;
    }

    public void setNews(NewsDTO news) {
        this.news = news;
    }

}
