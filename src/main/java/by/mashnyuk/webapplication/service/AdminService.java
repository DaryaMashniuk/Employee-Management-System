package by.mashnyuk.webapplication.service;

import by.mashnyuk.webapplication.dto.EmployeeDto;

import java.util.List;

public interface AdminService {
    List<EmployeeDto> getAllUsers();
    boolean updateUserRole(int userId, String newRole);
    boolean isRoleChangeAllowed(int currentUserId, int targetUserId, String newRole);
}
