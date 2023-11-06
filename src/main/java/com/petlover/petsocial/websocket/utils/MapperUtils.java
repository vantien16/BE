package com.petlover.petsocial.websocket.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.petlover.petsocial.model.entity.User;
import com.petlover.petsocial.websocket.dto.OnlineUserDto;
import org.modelmapper.ModelMapper;

public class MapperUtils {

    public static <S, T> List<T> mapperList(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static <S, T> T mapperObject(S source, Class<T> targetObject) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(source, targetObject);
    }

    public static List<OnlineUserDto> mapperList(List<User> source) {
        return source.stream().map(user -> {
            OnlineUserDto onlineUserDto = new OnlineUserDto();
            onlineUserDto.setId(user.getId());
            onlineUserDto.setName(user.getName()); // Map username
            onlineUserDto.setStatus("OFFLINE"); // Set status as OFFLINE
            return onlineUserDto;
        }).collect(Collectors.toList());
    }


}
