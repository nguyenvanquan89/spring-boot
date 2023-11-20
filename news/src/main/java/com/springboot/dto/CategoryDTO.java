package com.springboot.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.springboot.entity.CategoryEntity;
import com.springboot.util.MappingUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO extends BaseDTO {

  @NotBlank(message = "{categorydto.name.notblank}")
  @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
  private String name;

  @NotBlank(message = "{categorydto.code.notblank}")
  @JsonView(value = {Views.AddNewView.class, Views.UpdateView.class, Views.SearchView.class})
  private String code;

  @JsonView(value = {Views.UpdateView.class, Views.SearchView.class})
  @JsonIgnoreProperties(value = "category")
  private List<NewsDTO> news;

  @JsonView(value = {Views.SearchView.class})
  private Integer totalNews;

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

  public Integer getTotalNews() {
    return totalNews;
  }

  public void setTotalNews(Integer totalNews) {
    this.totalNews = totalNews;
  }

  /**
   * Mapper object category dto
   *
   * @param mapper ModelMapper
   * @param utils  MappingUtils
   * @return
   */
  @Override
  public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
    mapper.addMappings(categoryMap(utils));
    return mapper;
  }

  /**
   * model map deep to get all news of category
   *
   * @param utils MappingUtils
   * @return
   */
  public PropertyMap<CategoryEntity, CategoryDTO> categoryMap(MappingUtils utils) {
    return new PropertyMap<CategoryEntity, CategoryDTO>() {
      @Override
      protected void configure() {
        Converter<CategoryEntity, Integer> getSizeNews =
            new AbstractConverter<CategoryEntity, Integer>() {
              @Override
              protected Integer convert(CategoryEntity categoryEntity) {
                return categoryEntity.getNews().size();
              }
            };
        //get News is child of category
        Converter<CategoryEntity, List<NewsDTO>> mapNews =
            new AbstractConverter<CategoryEntity, List<NewsDTO>>() {
              @Override
              protected List<NewsDTO> convert(CategoryEntity categoryEntity) {
                return utils.mapList(new ArrayList<>(categoryEntity.getNews()), NewsDTO.class);
              }
            };
        //using(getSizeNews).map(source, destination.getTotalNews());
        //using(mapNews).map(source, destination.getNews());

      }
    };
  }
}
