package by.mashnyuk.webapplication.dao;

import by.mashnyuk.webapplication.entity.Employee;

public interface AuthDao {
    boolean isEmailExists(String email);
    boolean verifyEmail(String verificationToken);
    boolean checkIfEmailIsVerified(String username);
    boolean authenticate(String username, String password);
    int register(Employee employee, String verificationToken);

}
