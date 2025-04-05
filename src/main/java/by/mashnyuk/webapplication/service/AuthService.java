package by.mashnyuk.webapplication.service;

import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.dto.impl.EmployeeDtoImpl;

import java.util.Map;

public interface AuthService {
    boolean authenticate(String username, String password);
    boolean isEmailExists(String email);
    boolean verifyEmail(String verificationToken);
    Map<String,String> register(EmployeeDtoImpl employeeDto);
//    boolean checkIfEmailIsVerified(String username);
}
