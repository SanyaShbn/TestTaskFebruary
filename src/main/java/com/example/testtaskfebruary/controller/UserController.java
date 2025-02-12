package com.example.testtaskfebruary.controller;

import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.Role;
import com.example.testtaskfebruary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        Optional<UserReadDto> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user";
        } else {
            return "redirect:/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<UserReadDto> user = userService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("userCreateEditDto", user.get());
            model.addAttribute("roles", Role.values());
            return "edit";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserCreateEditDto userCreateEditDto) {
        userService.updateUser(id, userCreateEditDto);
        return "redirect:/user/" + id;
    }

    @GetMapping("/logout")
    public String logout() {
        // Логика выхода
        return "redirect:/";
    }
}