package by.mashnyuk.webapplication.dao;

import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.dto.impl.EmployeeDtoImpl;
import by.mashnyuk.webapplication.entity.Employee;

public interface AuthDao {
    boolean isEmailExists(String email);
    boolean verifyEmail(String verificationToken);
    boolean checkIfEmailIsVerified(String username);
    boolean authenticate(String username, String password);
    int register(EmployeeDtoImpl employeeDto, String verificationToken);

}
