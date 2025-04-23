package by.mashnyuk.webapplication.dao;

import by.mashnyuk.webapplication.dto.EmployeeDto;

import java.util.List;

public interface EmployeeDao  {
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeByUsername(String username);
    EmployeeDto getEmployeeById(int id);
    void editProfile(EmployeeDto employeeDto);
    boolean updateEmployeeAvatar(String username, byte[] avatarData);
    byte[] getEmployeeAvatar(String username);
}
