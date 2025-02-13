package com.example.testtaskfebruary.mapper;

import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.User;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between {@link User} entities and various DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Converts a {@link User} entity to a {@link UserReadDto}.
     *
     * @param user the {@link User} entity to convert, not null
     * @return the converted {@link UserReadDto}
     */
    UserReadDto userToUserReadDto(User user);

    /**
     * Converts a {@link UserCreateEditDto} to a {@link User} entity.
     *
     * @param userCreateEditDto the {@link UserCreateEditDto} to convert, not null
     * @return the converted {@link User} entity
     */
    User userCreateEditDtoToUser(UserCreateEditDto userCreateEditDto);
}
