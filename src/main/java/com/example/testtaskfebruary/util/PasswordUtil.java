package com.example.testtaskfebruary.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
public class PasswordUtil {

    /**
     * Hashes a plain text password using BCrypt.
     *
     * @param plainPassword the plain text password to hash, not null
     * @return the hashed password
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Checks if a plain text password matches a hashed password.
     *
     * @param plainPassword the plain text password to check, not null
     * @param hashedPassword the hashed password to compare against, not null
     * @return true if the passwords match, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}