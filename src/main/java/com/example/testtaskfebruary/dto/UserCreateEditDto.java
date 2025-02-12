package com.example.testtaskfebruary.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class UserCreateEditDto {

    @Email
    String email;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password;

    @NotEmpty(message = "First name cannot be empty")
    String firstname;

    @NotEmpty(message = "Last name cannot be empty")
    String lastname;

    @NotNull(message = "Birth date cannot be null")
    @Past(message = "Birth date must be in the past")
    LocalDate birthDate;

    @NotNull(message = "Role cannot be null")
    String role;
}