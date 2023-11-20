package com.springboot.config;

import com.springboot.util.MappingUtils;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  /**
   * Configuration model mapper to map entity and dto
   *
   * @return
   */
  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public MappingUtils mappingUtils() {
    return new MappingUtils();
  }
}
