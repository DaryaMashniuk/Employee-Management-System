package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.annotation.AllowedRoles;
import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.AdminService;
import by.mashnyuk.webapplication.service.impl.AdminServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@AllowedRoles({"ADMIN", "SUPER_ADMIN"})
public class AdminPanelCommand implements Command {
    private final AdminService adminService = AdminServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        EmployeeDto currentUser = (EmployeeDto) session.getAttribute("employee");
        if (!currentUser.isAdmin() && !currentUser.isSuperAdmin()) {
            System.out.println("errorrrrrrrrrr");
            return "error.jsp";
        }

        request.setAttribute("users", adminService.getAllUsers());
        return "adminProfile.jsp";
    }
}