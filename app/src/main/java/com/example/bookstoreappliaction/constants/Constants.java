package com.example.bookstoreappliaction.constants;

public class Constants {
    //APP CONSTANTS
    public static final String APP_NAME = "Book Store";
    public static final String DB_NAME = "book-store-db";
    public static final String USER_ID = "UserId";
    public static final String LOGIN = "Login";
    public static final String UPDATE_USER_ID = "update_user";
    public static final String BOOK_DETAIL_ID = "BookID";
    public static final String USER_CART_ID = "CartID";
    public static final String ORDER_ID = "OrderID";
    public static final String ORDER_TOTAL = "OrderTotal";
    //ROLE
    public static final String USER = "US";
    public static final String ADMIN = "AD";
    //NOTIFICATION
    public static final String NOTIFICATION_CHANNEL_ID = "ChannelID";
    public static final String NOTIFICATION_AFTER_LOGIN = "You have %d unpaid products in your cart, please pay quickly to complete the procedure and get the best experience.";
    public static final String NOTIFICATION_AFTER_CHECKOUT_SUCCEED = "Successful payment for %d products, thank you for shopping at our store.";
    //SUCCEED MESSAGE
    public static final String ADD_TO_CART_SUCCEED = "Add succeed";
    public static final String CHECKOUT_SUCCEED = "Payment success. Thank you for your purchase.";
    public static final String USER_UPDATE_SUCCEED = "Update Succeed";

    //ERRORS MESSAGE
    public static final String PHONE_REGISTER_ERROR = "This phone number is already registered";
    public static final String REQUIRE_MESSAGE = "The field cannot be empty";
    public static final String CONFIRM_PASSWORD_ERROR = "Two password are not the same";
    public static final String LOGIN_FAILED_MESSAGE = "Incorrect phone number or password";
    public static final String ADD_TO_CART_FAILED = "Product quantity is not enough";
    public static final String ADD_TO_CART_QUANTITY_INVALID = "Quantity is invalid. (Quantity must be positive integer number)";
    public static final String CHECKOUT_ERROR = "Checkout failed. Please Try again.";
    public static final String USER_NEW_PASSWORD_EQUALS_CURRENT_PASSWORD_ERROR = "The new password cannot be the same as the old password";
    public static final String USER_INCORRECT_PASSWORD_ERROR = "Your password is incorrect. Please try again.";
}
