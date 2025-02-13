package com.example.testtaskfebruary.unit.controller;

import com.example.testtaskfebruary.controller.UserController;
import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.Role;
import com.example.testtaskfebruary.exception.DaoException;
import com.example.testtaskfebruary.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final Long USER_ID = 1L;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private UserController userController;

    private UserReadDto userReadDto;

    @BeforeEach
    void setUp() {
        userReadDto = UserReadDto.builder()
                .id(USER_ID).build();
    }

    @Test
    void testGetUser_UserExists() {
        when(session.getAttribute("user")).thenReturn(userReadDto);
        when(userService.findById(USER_ID)).thenReturn(Optional.of(userReadDto));

        String viewName = userController.getUser(USER_ID, model, session);

        verify(model).addAttribute("user", userReadDto);
        assertEquals("user", viewName);
    }

    @Test
    void testGetUser_UserNotFound() {
        when(session.getAttribute("user")).thenReturn(userReadDto);
        when(userService.findById(USER_ID)).thenReturn(Optional.empty());

        String viewName = userController.getUser(USER_ID, model, session);

        assertEquals("redirect:/error", viewName);
    }

    @Test
    void testShowEditForm_UserExists() {
        when(session.getAttribute("user")).thenReturn(userReadDto);
        when(userService.findById(USER_ID)).thenReturn(Optional.of(userReadDto));

        String viewName = userController.showEditForm(USER_ID, model, session);

        verify(model).addAttribute("userCreateEditDto", userReadDto);
        verify(model).addAttribute("roles", Role.values());
        assertEquals("edit", viewName);
    }

    @Test
    void testUpdateUser_BindingErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = userController.updateUser(USER_ID, UserCreateEditDto.builder().build(),
                bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute(eq("errors"), any());
        assertEquals("redirect:/user/edit/" + USER_ID, viewName);
    }

    @Test
    void testUpdateUser_DaoException() throws DaoException {
        when(bindingResult.hasErrors()).thenReturn(false);
        UserCreateEditDto userCreateEditDto = UserCreateEditDto.builder().build();

        doThrow(new DaoException("Error updating user")).when(userService).updateUser(eq(USER_ID), any());

        String viewName = userController.updateUser(USER_ID, userCreateEditDto, bindingResult,
                redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("errors", "Error updating user");
        assertEquals("redirect:/user/edit/" + USER_ID, viewName);
    }

    @Test
    void testChangePassword_EmptyPassword() {
        String viewName = userController.changePassword(USER_ID, "", session);

        assertEquals("redirect:/user/edit/" + USER_ID, viewName);
    }

    @Test
    void testChangePassword_Success() {
        String newPassword = "newPassword123";

        String viewName = userController.changePassword(USER_ID, newPassword, session);

        verify(userService).updatePassword(USER_ID, newPassword);
        verify(session).invalidate();
        assertEquals("redirect:/logout", viewName);
    }
}