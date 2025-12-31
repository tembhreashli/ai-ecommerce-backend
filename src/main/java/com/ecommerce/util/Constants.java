package com.ecommerce.util;

/**
 * Application Constants
 * Contains all constant values used across the application
 */
public final class Constants {

    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // API Base Paths
    public static final String API_BASE_PATH = "/api";
    public static final String AUTH_BASE_PATH = "/auth";
    public static final String USER_BASE_PATH = "/users";
    public static final String PRODUCT_BASE_PATH = "/products";
    public static final String CART_BASE_PATH = "/cart";
    public static final String ORDER_BASE_PATH = "/orders";

    // Security Constants
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long TOKEN_EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    // Pagination Constants
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "ASC";

    // User Role Constants
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_CUSTOMER = "CUSTOMER";
    public static final String ROLE_VENDOR = "VENDOR";

    // Order Status Constants
    public static final String ORDER_STATUS_PENDING = "PENDING";
    public static final String ORDER_STATUS_CONFIRMED = "CONFIRMED";
    public static final String ORDER_STATUS_PROCESSING = "PROCESSING";
    public static final String ORDER_STATUS_SHIPPED = "SHIPPED";
    public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
    public static final String ORDER_STATUS_CANCELLED = "CANCELLED";
    public static final String ORDER_STATUS_RETURNED = "RETURNED";

    // Payment Status Constants
    public static final String PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYMENT_STATUS_COMPLETED = "COMPLETED";
    public static final String PAYMENT_STATUS_FAILED = "FAILED";
    public static final String PAYMENT_STATUS_REFUNDED = "REFUNDED";
    public static final String PAYMENT_STATUS_CANCELLED = "CANCELLED";

    // Product Status Constants
    public static final String PRODUCT_STATUS_ACTIVE = "ACTIVE";
    public static final String PRODUCT_STATUS_INACTIVE = "INACTIVE";
    public static final String PRODUCT_STATUS_OUT_OF_STOCK = "OUT_OF_STOCK";
    public static final String PRODUCT_STATUS_DISCONTINUED = "DISCONTINUED";

    // Validation Messages
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final String VALIDATION_USERNAME_REQUIRED = "Username is required";
    public static final String VALIDATION_EMAIL_REQUIRED = "Email is required";
    public static final String VALIDATION_PASSWORD_REQUIRED = "Password is required";
    public static final String VALIDATION_INVALID_EMAIL = "Email format is invalid";
    public static final String VALIDATION_PASSWORD_MIN_LENGTH = "Password must be at least " + PASSWORD_MIN_LENGTH + " characters";

    // Error Messages
    public static final String ERROR_USER_NOT_FOUND = "User not found";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Product not found";
    public static final String ERROR_ORDER_NOT_FOUND = "Order not found";
    public static final String ERROR_CART_NOT_FOUND = "Cart not found";
    public static final String ERROR_UNAUTHORIZED = "Unauthorized access";
    public static final String ERROR_INSUFFICIENT_STOCK = "Insufficient stock available";
    public static final String ERROR_INVALID_CREDENTIALS = "Invalid username or password";

    // Success Messages
    public static final String SUCCESS_USER_CREATED = "User created successfully";
    public static final String SUCCESS_PRODUCT_CREATED = "Product created successfully";
    public static final String SUCCESS_ORDER_CREATED = "Order created successfully";
    public static final String SUCCESS_CART_UPDATED = "Cart updated successfully";

    // Redis Cache Keys
    public static final String CACHE_KEY_USER = "user:";
    public static final String CACHE_KEY_PRODUCT = "product:";
    public static final String CACHE_KEY_CART = "cart:";
    public static final String CACHE_KEY_ORDER = "order:";

    // Cache TTL (Time To Live) in seconds
    public static final long CACHE_TTL_USER = 3600; // 1 hour
    public static final long CACHE_TTL_PRODUCT = 7200; // 2 hours
    public static final long CACHE_TTL_CART = 1800; // 30 minutes
    public static final long CACHE_TTL_ORDER = 3600; // 1 hour

    // File Upload Constants
    public static final long MAX_FILE_SIZE = 10485760; // 10MB in bytes
    public static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};
    public static final String UPLOAD_DIR = "uploads/";

    // Email Templates
    public static final String EMAIL_WELCOME = "welcome";
    public static final String EMAIL_ORDER_CONFIRMATION = "order-confirmation";
    public static final String EMAIL_PASSWORD_RESET = "password-reset";
    public static final String EMAIL_ORDER_SHIPPED = "order-shipped";

    // Date Format
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_ZONE = "UTC";
}
