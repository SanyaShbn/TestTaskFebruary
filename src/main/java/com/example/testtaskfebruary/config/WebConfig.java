package com.example.testtaskfebruary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;

/**
 * Configuration class that implements {@link WebMvcConfigurer} to add custom formatters.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adds custom formatters to the registry.
     *
     * @param registry the FormatterRegistry to which formatters can be added
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(LocalDate.class, new LocalDateFormatter());
    }
}