package com.example.testtaskfebruary.controller;

import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.Role;
import com.example.testtaskfebruary.exception.EmailAlreadyExistsException;
import com.example.testtaskfebruary.service.UserService;
import com.example.testtaskfebruary.util.PasswordUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controller for handling authentication-related requests.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Handles requests to the home page.
     *
     * @return the view name for the home page
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * Displays the registration form.
     *
     * @param model the model to pass attributes to the view
     * @return the view name for the registration form
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        if (!model.containsAttribute("userCreateEditDto")) {
            model.addAttribute("userCreateEditDto", UserCreateEditDto.builder().build());
        }
        model.addAttribute("roles", Role.values());
        return "register";
    }

    /**
     * Handles user registration.
     *
     * @param userCreateEditDto the DTO containing user information
     * @param bindingResult the result of validation
     * @param redirectAttributes the attributes for a redirect scenario
     * @return the view name for redirection after registration
     */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Validated UserCreateEditDto userCreateEditDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userCreateEditDto", userCreateEditDto);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/register";
        }
        try {
            userService.saveUser(userCreateEditDto);
            return "redirect:/login";
        } catch (EmailAlreadyExistsException e){
            redirectAttributes.addFlashAttribute("userCreateEditDto", userCreateEditDto);
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Displays the login form.
     *
     * @return the view name for the login form
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    /**
     * Handles user login.
     *
     * @param email the email address entered by the user
     * @param password the password entered by the user
     * @param model the model to pass attributes to the view
     * @param session the HTTP session
     * @return the view name for redirection after login
     */
    @PostMapping("/login")
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model,
                            HttpSession session) {
        Optional<UserReadDto> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent() && PasswordUtil.checkPassword(password, optionalUser.get().getPassword())) {
            UserReadDto currentSessionUser = optionalUser.get();
            session.setAttribute("user", currentSessionUser);
            model.addAttribute("user", currentSessionUser);
            return "redirect:/user/" + optionalUser.get().getId();
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    /**
     * Handles user logout.
     *
     * @param session the HTTP session
     * @return the view name for redirection after logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}