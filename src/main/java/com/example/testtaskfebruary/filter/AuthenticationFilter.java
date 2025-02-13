package com.example.testtaskfebruary.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter for authenticating user requests.
 */
public class AuthenticationFilter implements Filter {

    private static final String LOGIN_URI = "/login";

    private static final String USER_ATTRIBUTE = "user";

    /**
     * Filters incoming requests to check if the user is logged in.
     *
     * @param request the ServletRequest object, not null
     * @param response the ServletResponse object, not null
     * @param chain the FilterChain object, not null
     * @throws IOException if an I/O error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String loginURI = httpRequest.getContextPath() + LOGIN_URI;

        boolean loggedIn = session != null && session.getAttribute(USER_ATTRIBUTE) != null;
        boolean loginRequest = httpRequest.getRequestURI().equals(loginURI);

        if (loggedIn || loginRequest) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(loginURI);
        }
    }

}