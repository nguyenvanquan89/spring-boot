package com.springboot.util;

import com.springboot.util.MappingUtils;
import org.modelmapper.ModelMapper;

public interface IBaseModelMapper {
    default ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils){
        return mapper;
    }
}
