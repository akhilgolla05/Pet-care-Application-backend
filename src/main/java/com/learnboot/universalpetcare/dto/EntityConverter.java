package com.learnboot.universalpetcare.dto;

import com.learnboot.universalpetcare.models.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityConverter<T, D> {

    private final ModelMapper modelMapper;


    public D convertEntityToDto(T entity, Class<D> dtoClass) {
        return modelMapper.map(entity,dtoClass);
    }

    public T convertDtoToEntity(D dto, Class<T> entityClass) {
        return modelMapper.map(dto,entityClass);
    }
}
