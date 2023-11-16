package com.springboot.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Category")
public class CategoryEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<NewsEntity> news;

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

    public List<NewsEntity> getNews() {
        return news;
    }

    public void setNews(List<NewsEntity> news) {
        this.news = news;
    }

}
