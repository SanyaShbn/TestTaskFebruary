package com.example.testtaskfebruary.dto;

import com.example.testtaskfebruary.entity.Role;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserReadDto {
    Long id;
    String email;
    String password;
    String firstname;
    String lastname;
    LocalDate birthDate;
    Role role;
}