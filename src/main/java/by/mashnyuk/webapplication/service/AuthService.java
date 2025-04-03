package by.mashnyuk.webapplication.service;

import java.util.Map;

public interface AuthService {
    boolean authenticate(String username, String password);
    int register(String firstName, String lastName, String username, String password, String address, String email);
    boolean isEmailExists(String email);
    boolean verifyEmail(String verificationToken);
}
