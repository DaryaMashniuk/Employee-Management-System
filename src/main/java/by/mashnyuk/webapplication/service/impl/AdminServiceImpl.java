package by.mashnyuk.webapplication.service.impl;

import by.mashnyuk.webapplication.dao.EmployeeDao;
import by.mashnyuk.webapplication.dao.impl.EmployeeDaoImpl;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.service.AdminService;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final EmployeeDao employeeDao = EmployeeDaoImpl.getInstance();

    private static final AdminService instance = new AdminServiceImpl();

    public static AdminService getInstance() {
        return instance;
    }
    @Override
    public List<EmployeeDto> getAllUsers() {
        return employeeDao.getAllEmployees();
    }

    @Override
    public boolean updateUserRole(int userId, String newRole) {
        return employeeDao.updateEmployeeRole(userId, newRole);
    }

    @Override
    public boolean isRoleChangeAllowed(int currentUserId, int targetUserId, String newRole) {
        EmployeeDto currentUser = employeeDao.getEmployeeById(currentUserId);
        EmployeeDto targetUser = employeeDao.getEmployeeById(targetUserId);

        // SUPER_ADMIN нельзя изменить
        if ("SUPER_ADMIN".equals(targetUser.getRole())) {
            return false;
        }

        // ADMIN может назначать только USER и MODERATOR
        if ("ADMIN".equals(currentUser.getRole())) {
            return !"ADMIN".equals(newRole) && !"SUPER_ADMIN".equals(newRole);
        }

        // SUPER_ADMIN может все
        return "SUPER_ADMIN".equals(currentUser.getRole());
    }
}
