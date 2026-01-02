package com.ecommerce.util;

import com.ecommerce.exception.BadRequestException;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Utility class for common validation operations.
 */
public class ValidationUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    private ValidationUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Validate that a string is not null or empty.
     *
     * @param value the string to validate
     * @param fieldName the name of the field being validated
     * @throws BadRequestException if the value is null or empty
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new BadRequestException(fieldName + " cannot be empty");
        }
    }

    /**
     * Validate that an object is not null.
     *
     * @param value the object to validate
     * @param fieldName the name of the field being validated
     * @throws BadRequestException if the value is null
     */
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new BadRequestException(fieldName + " cannot be null");
        }
    }

    /**
     * Validate email format.
     *
     * @param email the email to validate
     * @throws BadRequestException if the email format is invalid
     */
    public static void validateEmail(String email) {
        if (!StringUtils.hasText(email)) {
            throw new BadRequestException("Email cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("Invalid email format");
        }
    }

    /**
     * Validate phone number format.
     *
     * @param phone the phone number to validate
     * @throws BadRequestException if the phone format is invalid
     */
    public static void validatePhone(String phone) {
        if (!StringUtils.hasText(phone)) {
            throw new BadRequestException("Phone number cannot be empty");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new BadRequestException("Invalid phone number format");
        }
    }

    /**
     * Validate that a number is positive.
     *
     * @param value the number to validate
     * @param fieldName the name of the field being validated
     * @throws BadRequestException if the value is not positive
     */
    public static void validatePositive(Number value, String fieldName) {
        if (value == null || value.doubleValue() <= 0) {
            throw new BadRequestException(fieldName + " must be positive");
        }
    }

    /**
     * Validate that a number is non-negative.
     *
     * @param value the number to validate
     * @param fieldName the name of the field being validated
     * @throws BadRequestException if the value is negative
     */
    public static void validateNonNegative(Number value, String fieldName) {
        if (value == null || value.doubleValue() < 0) {
            throw new BadRequestException(fieldName + " must be non-negative");
        }
    }

    /**
     * Validate string length.
     *
     * @param value the string to validate
     * @param fieldName the name of the field being validated
     * @param minLength the minimum allowed length
     * @param maxLength the maximum allowed length
     * @throws BadRequestException if the length is invalid
     */
    public static void validateLength(String value, String fieldName, int minLength, int maxLength) {
        if (!StringUtils.hasText(value)) {
            throw new BadRequestException(fieldName + " cannot be empty");
        }
        if (value.length() < minLength || value.length() > maxLength) {
            throw new BadRequestException(fieldName + " length must be between " + minLength + " and " + maxLength);
        }
    }

    /**
     * Validate password strength.
     *
     * @param password the password to validate
     * @throws BadRequestException if the password is weak
     */
    public static void validatePassword(String password) {
        validateLength(password, "Password", 8, 100);
        
        if (!password.matches(".*[A-Z].*")) {
            throw new BadRequestException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new BadRequestException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new BadRequestException("Password must contain at least one digit");
        }
    }

    /**
     * Validate that a value is within a range.
     *
     * @param value the value to validate
     * @param fieldName the name of the field being validated
     * @param min the minimum allowed value
     * @param max the maximum allowed value
     * @throws BadRequestException if the value is out of range
     */
    public static void validateRange(Number value, String fieldName, double min, double max) {
        if (value == null) {
            throw new BadRequestException(fieldName + " cannot be null");
        }
        double doubleValue = value.doubleValue();
        if (doubleValue < min || doubleValue > max) {
            throw new BadRequestException(fieldName + " must be between " + min + " and " + max);
        }
    }
}
