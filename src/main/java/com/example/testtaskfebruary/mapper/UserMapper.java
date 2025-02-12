package com.example.testtaskfebruary.mapper;

import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserReadDto userToUserReadDto(User user);

    User userCreateEditDtoToUser(UserCreateEditDto userCreateEditDto);
}
