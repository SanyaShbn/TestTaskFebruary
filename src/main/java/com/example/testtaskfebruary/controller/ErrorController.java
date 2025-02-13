package com.example.testtaskfebruary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling error-related requests.
 */
@Controller
public class ErrorController {

    /**
     * Handles requests to the error page.
     *
     * @return the view name for the error page
     */
    @GetMapping("/error")
    public String showErrorPage() {
        return "error";
    }
}