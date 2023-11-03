package com.springboot.util;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.spi.ErrorMessage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MappingUtils {

    /**
     * Get model mapper by class target
     *
     * @param target class target
     * @param <T>    generic class implement IBaseModelMapper
     * @return object model mapper
     */
    public <T extends IBaseModelMapper> ModelMapper getMapper(Class<T> target) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true)
                .setPropertyCondition(context ->
                        !(context.getSource() instanceof PersistentCollection)
                );

        return updateMapping(modelMapper, target);
    }

    /**
     * @param mapper model mapper
     * @param dto    class DTO
     * @param <T>    generic class implement IBaseModelMapper
     * @return object model mapper
     */
    public <T extends IBaseModelMapper> ModelMapper updateMapping(
            ModelMapper mapper, Class<T> dto) {
        try {
            Constructor<T> constructor = dto.getConstructor();
            T instance = constructor.newInstance();
            return instance.updateModelMapper(mapper, this);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException e) {
            List<ErrorMessage> errorMessages = new ArrayList<>();
            errorMessages.add(new ErrorMessage(dto.getName()));
            throw new MappingException(errorMessages);
        }
    }

    /**
     * @param source List source
     * @param target class target
     * @param <S>    class extend object
     * @param <T>    class extend IBaseModelMapper
     * @return List target
     */
    public <S, T extends IBaseModelMapper> List<T> mapList(List<S> source, Class<T> target) {
        ModelMapper modelMapper = getMapper(target);

        return source
                .stream().map(el -> modelMapper.map(el, target))
                .collect(Collectors.toList());
    }

    /**
     * @param source object source
     * @param target destination type
     * @param <T>    class extend IBaseModelMapper
     * @param <S>    class extend Object
     * @return Object extend IBaseModelMapper after mapped
     */
    public <T extends IBaseModelMapper, S> T map(S source, Class<T> target) {
        ModelMapper modelMapper = getMapper(target);

        return modelMapper.map(source, target);
    }

    /**
     * @param source object source
     * @param target destination type
     * @param <T>    class extend IBaseModelMapper
     * @param <S>    class extend Object
     */
    public <T extends IBaseModelMapper, S> void map(S source, T target) {
        ModelMapper modelMapper = getMapper(target.getClass());
        modelMapper.map(source, target);
    }

    /**
     * @param source List source
     * @param target destination type
     * @param <S>    class extend IBaseModelMapper
     * @param <T>    class extend Object
     * @return List object destination
     */
    public <S extends IBaseModelMapper, T> List<T> mapListFromDTO(
            List<S> source, Class<T> target) {
        ModelMapper modelMapper = getMapper(source.get(0).getClass());

        return source
                .stream().map(el -> modelMapper.map(el, target))
                .collect(Collectors.toList());
    }

    /**
     * @param source source object
     * @param target destination type
     * @param <S>    class extend IBaseModelMapper
     * @param <T>    class extend Object
     * @return object destination
     */
    public <S extends IBaseModelMapper, T> T mapFromDTO(S source, Class<T> target) {
        ModelMapper modelMapper = getMapper(source.getClass());

        return modelMapper.map(source, target);
    }

    /**
     * @param source source object
     * @param target destination type
     * @param <S>    class extends IBaseModelMapper
     * @param <T>    class extend object
     */
    public <S extends IBaseModelMapper, T> void mapFromDTO(S source, T target) {
        ModelMapper modelMapper = getMapper(source.getClass());
        modelMapper.map(source, target);
    }
}
