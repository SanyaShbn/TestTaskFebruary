package com.example.testtaskfebruary.controller;

import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.service.UserService;
import com.example.testtaskfebruary.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userCreateEditDto", UserCreateEditDto.builder().build());
        return "/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserCreateEditDto userCreateDto) {
        userService.saveUser(userCreateDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model) {
        Optional<UserReadDto> optionalUser = userService.findByEmail(email);
        if (optionalUser.isPresent() && PasswordUtil.checkPassword(password, optionalUser.get().getPassword())) {
            model.addAttribute("user", optionalUser.get());
            return "redirect:/user/" + optionalUser.get().getId();
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }
}