package com.example.testtaskfebruary.unit.controller;

import com.example.testtaskfebruary.controller.AuthController;
import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;

import com.example.testtaskfebruary.exception.EmailAlreadyExistsException;
import com.example.testtaskfebruary.service.UserService;
import com.example.testtaskfebruary.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testHome() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void testShowRegistrationForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("/register"))
                .andExpect(model().attributeExists("userCreateEditDto"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    void testRegisterUser() throws Exception {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder().build();
        doNothing().when(userService).saveUser(any(UserCreateEditDto.class));

        mockMvc.perform(post("/register")
                        .flashAttr("userCreateEditDto", userCreateEditDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).saveUser(any(UserCreateEditDto.class));
    }

    @Test
    void testRegisterUserEmailAlreadyExists() throws Exception {
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder().build();
        doThrow(new EmailAlreadyExistsException("Email already exists")).when(userService).saveUser(any(UserCreateEditDto.class));

        mockMvc.perform(post("/register")
                        .flashAttr("userCreateEditDto", userCreateEditDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));

        verify(userService, times(1)).saveUser(any(UserCreateEditDto.class));
    }

    @Test
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testLoginUser() throws Exception {
        String email = "test@example.com";
        String password = "password";
        UserReadDto userReadDto = UserReadDto.builder().id(1L).email(email).password(PasswordUtil.hashPassword(password)).build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(userReadDto));
        when(PasswordUtil.checkPassword(password, userReadDto.getPassword())).thenReturn(true);

        mockMvc.perform(post("/login")
                        .param("email", email)
                        .param("password", password)
                        .sessionAttr("user", userReadDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/" + userReadDto.getId()));

        verify(userService, times(1)).findByEmail(email);
    }

    @Test
    void testLoginUserInvalidCredentials() throws Exception {
        String email = "test@example.com";
        String password = "password";

        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .param("email", email)
                        .param("password", password))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));

        verify(userService, times(1)).findByEmail(email);
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
