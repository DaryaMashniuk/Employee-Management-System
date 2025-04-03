package by.mashnyuk.webapplication.dao;

import by.mashnyuk.webapplication.entity.Employee;

import java.util.List;

public interface EmployeeDao  {
    List<Employee> getAllEmployees();
    Employee getEmployeeByUsername(String username);
    int editProfile(Employee employee);
    int updateAvatar(String username, String avatarPath);
}
