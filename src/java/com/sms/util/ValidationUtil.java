package com.sms.util;

import java.sql.Date;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10,11}$");
    
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }
    
    public static boolean isPastDate(Date date) {
        return date != null && date.toLocalDate().isBefore(LocalDate.now());
    }
    
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    public static boolean isValidDate(String dateStr) {
        try {
            Date.valueOf(dateStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    public static boolean isValidLength(String str, int maxLength) {
        return str != null && str.length() <= maxLength;
    }
    
    public static boolean isXssSafe(String input) {
        if (input == null) return true;
        String lower = input.toLowerCase();
        return !lower.contains("<script") && !lower.contains("alert(") &&
               !lower.contains("javascript:") && !lower.contains("onerror=");
    }
    
    public static boolean isSqlInjectionSafe(String input) {
        if (input == null) return true;
        String lower = input.toLowerCase();
        return !lower.contains("or") && !lower.contains("and") &&
               !lower.contains("select") && !lower.contains("insert") &&
               !lower.contains("delete") && !lower.contains("drop") &&
               !lower.contains("'") && !lower.contains("--");
    }
}