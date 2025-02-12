package com.example.testtaskfebruary.controller;

import com.example.testtaskfebruary.dto.UserCreateEditDto;
import com.example.testtaskfebruary.dto.UserReadDto;
import com.example.testtaskfebruary.entity.Role;
import com.example.testtaskfebruary.exception.DaoException;
import com.example.testtaskfebruary.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model, HttpSession session) {
        try {
            checkUserSession(session, id);
            Optional<UserReadDto> optionalUser = userService.findById(id);
            if (optionalUser.isPresent()) {
                model.addAttribute("user", optionalUser.get());
                return "user";
            } else {
                return "redirect:/error";
            }
        } catch (IllegalArgumentException | DaoException e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        try {
            checkUserSession(session, id);
            Optional<UserReadDto> optionalUser = userService.findById(id);
            if (optionalUser.isPresent()) {
                if (!model.containsAttribute("userCreateEditDto")) {
                    model.addAttribute("userCreateEditDto", optionalUser.get());
                }
                model.addAttribute("roles", Role.values());
                return "edit";
            } else {
                return "redirect:/error";
            }
        } catch (IllegalArgumentException | DaoException e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute @Validated UserCreateEditDto userCreateEditDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/edit/" + id;
        }
        try {
            userService.updateUser(id, userCreateEditDto);
            return "redirect:/user/" + id;
        } catch (DaoException e){
            redirectAttributes.addFlashAttribute("errors", e.getMessage());
            return "redirect:/user/edit/" + id;
        }
    }

    @PostMapping("/change-password/{id}")
    public String changePassword(@PathVariable Long id, @RequestParam String password, HttpSession session) {
        if (password == null || password.isEmpty()) {
            return "redirect:/user/edit/" + id;
        }
        userService.updatePassword(id, password);
        session.invalidate();
        return "redirect:/logout";
    }

    private void checkUserSession(HttpSession session, Long id) {
        UserReadDto user = (UserReadDto) session.getAttribute("user");
        if (user == null || !user.getId().equals(id)) {
            throw new IllegalArgumentException("Unauthorized access");
        }
    }

}