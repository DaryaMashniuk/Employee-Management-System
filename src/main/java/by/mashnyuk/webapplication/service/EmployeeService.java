package by.mashnyuk.webapplication.service;

import by.mashnyuk.webapplication.entity.Employee;

import java.util.List;

public interface EmployeeService {
    boolean authenticate(String username, String password);
    int register(String firstName, String lastName, String username, String password, String address, String email);
    List<Employee> getAllEmployees();
    Employee getEmployeeByUsername(String username);
    boolean isEmailExists(String email);
    boolean verifyEmail(String verificationToken);
    int editProfile(String firstName, String lastName, String username, String address, String email);
}
