package com.example.testtaskfebruary.unit.controller;

import com.example.testtaskfebruary.controller.AuthController;
import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.Role;
import com.example.testtaskfebruary.exception.EmailAlreadyExistsException;
import com.example.testtaskfebruary.service.UserService;
import com.example.testtaskfebruary.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final Long USER_ID = 1L;

    private static final String USER_EMAIL = "test@gmail.com";

    private static final String USER_PASSWORD = "password123";

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    private UserCreateEditDto userCreateEditDto;

    private UserReadDto userReadDto;

    @BeforeEach
    void setUp() {
        userCreateEditDto = UserCreateEditDto.builder().build();
        userReadDto = UserReadDto.builder()
                .id(USER_ID)
                .email(USER_EMAIL)
                .password(BCrypt.hashpw(USER_PASSWORD, BCrypt.gensalt()))
                .build();
    }

    @Test
    void testHome() {
        String viewName = authController.home();
        assertEquals("home", viewName);
    }

    @Test
    void testShowRegistrationForm() {
        String viewName = authController.showRegistrationForm(model);
        verify(model).addAttribute(eq("userCreateEditDto"), any(UserCreateEditDto.class));
        verify(model).addAttribute(eq("roles"), eq(Role.values()));
        assertEquals("register", viewName);
    }

    @Test
    void testRegisterUser_BindingErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = authController.registerUser(userCreateEditDto, bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("userCreateEditDto", userCreateEditDto);
        verify(redirectAttributes).addFlashAttribute("errors", bindingResult.getAllErrors());
        assertEquals("redirect:/register", viewName);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() throws EmailAlreadyExistsException {
        when(bindingResult.hasErrors()).thenReturn(false);
        doThrow(new EmailAlreadyExistsException("Email already exists")).when(userService).saveUser(userCreateEditDto);

        String viewName = authController.registerUser(userCreateEditDto, bindingResult, redirectAttributes);

        verify(redirectAttributes).addFlashAttribute("userCreateEditDto", userCreateEditDto);
        verify(redirectAttributes).addFlashAttribute("error", "Email already exists");
        assertEquals("redirect:/register", viewName);
    }

    @Test
    void testRegisterUser_Success() throws EmailAlreadyExistsException {
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = authController.registerUser(userCreateEditDto, bindingResult, redirectAttributes);

        verify(userService).saveUser(userCreateEditDto);
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testShowLoginForm() {
        String viewName = authController.showLoginForm();
        assertEquals("login", viewName);
    }

    @Test
    void testLoginUser_Success() {
        when(userService.findByEmail(USER_EMAIL)).thenReturn(Optional.of(userReadDto));
        try (MockedStatic<PasswordUtil> mockedStatic = mockStatic(PasswordUtil.class)) {
            mockedStatic.when(() -> PasswordUtil.checkPassword(USER_PASSWORD, userReadDto.getPassword())).thenReturn(true);

            String viewName = authController.loginUser(USER_EMAIL, USER_PASSWORD, model, session);

            verify(session).setAttribute("user", userReadDto);
            verify(model).addAttribute("user", userReadDto);
            assertEquals("redirect:/user/" + USER_ID, viewName);
        }
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        String email = "test@example.com";
        String password = "wrongpassword";

        UserReadDto userReadDto = UserReadDto.builder()
                .id(USER_ID)
                .email(email)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .build();

        when(userService.findByEmail(email)).thenReturn(Optional.of(userReadDto));

        try (MockedStatic<PasswordUtil> mockedStatic = mockStatic(PasswordUtil.class)) {
            mockedStatic.when(() -> PasswordUtil.checkPassword(password, userReadDto.getPassword())).thenReturn(false);

            String viewName = authController.loginUser(email, password, model, session);

            verify(model).addAttribute("error", "Invalid email or password");
            assertEquals("login", viewName);
        }
    }

    @Test
    void testLogout() {
        String viewName = authController.logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/", viewName);
    }

}

