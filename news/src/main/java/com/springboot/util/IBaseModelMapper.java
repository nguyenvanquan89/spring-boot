package com.springboot.util;

import org.modelmapper.ModelMapper;

public interface IBaseModelMapper {
    default ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils){
        return mapper;
    }
}
