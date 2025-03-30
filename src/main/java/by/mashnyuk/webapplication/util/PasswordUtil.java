package by.mashnyuk.webapplication.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String hashPassword(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        return hashed;
    }


    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
