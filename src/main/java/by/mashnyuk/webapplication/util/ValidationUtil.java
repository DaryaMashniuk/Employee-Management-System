package by.mashnyuk.webapplication.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{5,20}$"); // Логин: 5-20 символов, буквы, цифры, _
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-ZА-ЯЁ][a-zа-яё]{1,30}$"); // Имя и фамилия: первая буква заглавная
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"); // Пароль: 8+ символов, буква, цифра, спецсимвол
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$"); // Email: формат example@example.com

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty();
    }
}

