package com.example.testtaskfebruary.unit.controller;

import com.example.testtaskfebruary.controller.UserController;
import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private static final Long USER_ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUser_Success() throws Exception {
        UserReadDto userReadDto = UserReadDto.builder()
                .id(USER_ID).build();

        when(userService.findById(anyLong())).thenReturn(Optional.of(userReadDto));

        mockMvc.perform(get("/user/" + USER_ID).sessionAttr("user", userReadDto))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testGetUser_Unauthorized() throws Exception {
        mockMvc.perform(get("/user/" + USER_ID))
                .andExpect(redirectedUrl("/error"));
    }

    @Test
    public void testShowEditForm_Success() throws Exception {
        UserReadDto userReadDto = UserReadDto.builder()
                .id(USER_ID).build();

        when(userService.findById(anyLong())).thenReturn(Optional.of(userReadDto));

        mockMvc.perform(get("/user/edit/" + USER_ID).sessionAttr("user", userReadDto))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userCreateEditDto"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    void testUpdateUser_Successful() throws Exception {
        when(bindingResult.hasErrors()).thenReturn(false);

        mockMvc.perform(post("/user/edit/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "test@example.com")
                        .param("firstname", "Test User"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/1"));

        verify(userService, times(1)).updateUser(eq(1L), any(UserCreateEditDto.class));
    }

    @Test
    public void testChangePassword_Success() throws Exception {
        UserReadDto userReadDto = UserReadDto.builder()
                .id(USER_ID).build();

        doNothing().when(userService).updatePassword(anyLong(), anyString());

        mockMvc.perform(post("/user/change-password/" + USER_ID)
                        .param("password", "newPassword")
                        .sessionAttr("user", userReadDto))
                .andExpect(redirectedUrl("/logout"));

        verify(userService).updatePassword(anyLong(), anyString());
    }

    @Test
    public void testChangePassword_EmptyPassword() throws Exception {
        mockMvc.perform(post("/user/change-password/"  +USER_ID).param("password", ""))
                .andExpect(redirectedUrl("/user/edit/" + USER_ID));

        verify(userService, never()).updatePassword(anyLong(), anyString());
    }
}