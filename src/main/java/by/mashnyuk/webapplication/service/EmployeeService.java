package by.mashnyuk.webapplication.service;

import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.entity.Employee;
import jakarta.servlet.http.Part;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    List<EmployeeDto> getAllEmployees();
    EmployeeDto getEmployeeByUsername(String username);
    Map<String,String> editProfile(EmployeeDto employeeDto);
//    String updateEmployeeAvatar(Part filePart, String username, String appPath);
    boolean updateEmployeeAvatar(String username, Part filePart);
    byte[] getEmployeeAvatar(String username);
}
