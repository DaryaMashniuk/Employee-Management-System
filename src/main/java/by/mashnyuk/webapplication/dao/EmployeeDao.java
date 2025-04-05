package by.mashnyuk.webapplication.dao;

import by.mashnyuk.webapplication.dto.EmployeeDto;

import java.util.List;

public interface EmployeeDao  {
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeByUsername(String username);
    void editProfile(EmployeeDto employeeDto);
//    int updateEmployeeAvatar(String username, String avatarPath);
//    String getEmployeeAvatarPath(String username);
    boolean updateEmployeeAvatar(String username, byte[] avatarData);
    byte[] getEmployeeAvatar(String username);
}
