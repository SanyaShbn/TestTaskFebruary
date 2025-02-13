package com.example.testtaskfebruary.config;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Formatter for {@link LocalDate} objects, allowing them to be printed and parsed in the format "yyyy-MM-dd".
 */
public class LocalDateFormatter implements Formatter<LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Parses a text string to produce a {@link LocalDate} object.
     *
     * @param text the text to parse, not null
     * @param locale the locale to use, not null
     * @return the parsed {@link LocalDate} object
     */
    @Override
    public LocalDate parse(String text, Locale locale) {
        return LocalDate.parse(text, FORMATTER);
    }

    /**
     * Prints a {@link LocalDate} object to produce a formatted string.
     *
     * @param object the {@link LocalDate} object to format, not null
     * @param locale the locale to use, not null
     * @return the formatted string
     */
    @Override
    public String print(LocalDate object, Locale locale) {
        return FORMATTER.format(object);
    }
}