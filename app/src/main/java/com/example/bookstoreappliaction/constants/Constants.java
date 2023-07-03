package com.example.bookstoreappliaction.constants;

public class Constants {
    //APP CONSTANTS
    public static final String DB_NAME = "book-store-db";
    public static final String UPDATE_USER_ID = "update_user";
    public static final String BOOK_DETAIL_ID = "BookID";
    public static final String USER_CART_ID = "CartID";
    //ROLE
    public static final String USER = "US";
    public static final String ADMIN = "AD";
    //SUCCEED MESSAGE
    public static final String ADD_TO_CART_SUCCEED = "Add succeed";

    //ERRORS MESSAGE
    public static final String PHONE_REGISTER_ERROR = "This phone number is already registered";
    public static final String REQUIRE_MESSAGE = "The field cannot be empty";
    public static final String CONFIRM_PASSWORD_ERROR = "Two password are not the same";
    public static final String LOGIN_FAILED_MESSAGE = "Incorrect phone number or password";
    public static final String ADD_TO_CART_FAILED = "Product quantity is not enough";
    public static final String ADD_TO_CART_QUANTITY_INVALID = "Quantity is invalid. (Quantity must be positive integer number)";
}
