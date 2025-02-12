package com.example.testtaskfebruary.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserCreateEditDto {
    String email;
    String password;
    String firstname;
    String lastname;
    LocalDate birthDate;
    String role;
}
