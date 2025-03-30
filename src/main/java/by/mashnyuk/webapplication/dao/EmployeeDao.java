package by.mashnyuk.webapplication.dao;

import by.mashnyuk.webapplication.entity.Employee;

import java.util.List;

public interface EmployeeDao  {
    boolean authenticate(String username, String password);
    int register(Employee employee, String verificationToken);
    List<Employee> getAllEmployees();
    Employee getEmployeeByUsername(String username);
    boolean isEmailExists(String email);
    boolean verifyEmail(String verificationToken);
    boolean checkIfEmailIsVerified(String username);
}
