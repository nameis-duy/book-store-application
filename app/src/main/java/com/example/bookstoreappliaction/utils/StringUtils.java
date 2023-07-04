package com.example.bookstoreappliaction.utils;

public class StringUtils {
    public static String FormatPhone(String phone) {
        phone = phone.trim();
        phone = phone.substring(0, 4) + " " + phone.substring(4, 7) + " " + phone.substring(7);
        phone = "+84 " + phone.substring(1);
        return phone;
    }

    public static String removeEmptySpace(String name) {
        name = name.toLowerCase();
        name = name.trim();
        name = name.replace(" ", "%");
        return name;
    }
}
