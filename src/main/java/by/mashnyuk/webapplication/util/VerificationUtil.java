package by.mashnyuk.webapplication.util;

import java.util.Random;
import java.util.UUID;

public class VerificationUtil {
    public static String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
