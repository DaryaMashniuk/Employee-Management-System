package by.mashnyuk.webapplication.service;

import by.mashnyuk.webapplication.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    Employee getEmployeeByUsername(String username);
    int editProfile(String firstName, String lastName, String username, String address, String email);
    int updateAvatar(String username, String avatarPath);
}
