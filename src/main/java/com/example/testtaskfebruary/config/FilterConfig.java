package com.example.testtaskfebruary.config;

import com.example.testtaskfebruary.filter.AuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up filters in the application.
 */
@Configuration
public class FilterConfig {

    private static final String PROTECTED_URL_PATTERN = "/user/*";

    /**
     * Creates a FilterRegistrationBean for the AuthenticationFilter.
     *
     * @return a FilterRegistrationBean configured with the AuthenticationFilter.
     */
    @Bean
    public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
        FilterRegistrationBean<AuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns(PROTECTED_URL_PATTERN);
        return registrationBean;
    }
}