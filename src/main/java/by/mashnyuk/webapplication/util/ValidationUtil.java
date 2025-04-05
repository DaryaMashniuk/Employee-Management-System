package by.mashnyuk.webapplication.util;

import jakarta.servlet.http.Part;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{5,20}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-ZА-ЯЁ][a-zа-яё]{1,30}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");
    private static final int MAX_FILE_SIZE = 2 * 1024 * 1024;

    public static Map<String, String> validateRegistration(String firstName, String lastName,
                                                           String username, String password,
                                                           String address, String email) {
        Map<String, String> errors = new HashMap<>();

        if (!isValidName(firstName)) errors.put("firstNameError", "Invalid first name");
        if (!isValidName(lastName)) errors.put("lastNameError", "Invalid last name");
        if (!isValidUsername(username)) errors.put("usernameError", "Invalid username");
        if (!isValidPassword(password)) errors.put("passwordError", "Weak password");
        if (!isValidAddress(address)) errors.put("addressError", "Address required");
        if (!isValidEmail(email)) errors.put("emailError", "Invalid email");

        return errors;
    }

    public static void validateAvatarFile(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IOException("Файл аватара пустой");
        }
        if (filePart.getSize() > MAX_FILE_SIZE) {
            throw new IOException("Файл слишком большой (максимум 2MB)");
        }
        if (!filePart.getContentType().startsWith("image/")) {
            throw new IOException("Допустимы только изображения");
        }
    }
    public static Map<String, String> validateProfileUpdate(String firstName, String lastName,
                                                            String address, String email) {
        Map<String, String> errors = new HashMap<>();

        if (!isValidName(firstName)) errors.put("firstNameError", "Invalid first name");
        if (!isValidName(lastName)) errors.put("lastNameError", "Invalid last name");
        if (!isValidAddress(address)) errors.put("addressError", "Address required");
        if (!isValidEmail(email)) errors.put("emailError", "Invalid email");

        return errors;
    }
    public static boolean isValidUsername(String username) {
        return username != null && !username.isEmpty() && USERNAME_PATTERN.matcher(username).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.isEmpty() && NAME_PATTERN.matcher(name).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && !password.isEmpty() && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidAddress(String address) {
        return address != null && !address.isEmpty() && !address.trim().isEmpty();
    }
}

