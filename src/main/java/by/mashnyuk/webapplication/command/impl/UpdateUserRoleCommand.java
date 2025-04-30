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
public class UpdateUserRoleCommand implements Command {
    private final AdminService adminService = AdminServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        EmployeeDto currentUser = (EmployeeDto) session.getAttribute("employee");
        int currentUserId = currentUser.getId();

        try {
            int targetUserId = Integer.parseInt(request.getParameter("userId"));
            String newRole = request.getParameter("role");

            if (adminService.isRoleChangeAllowed(currentUserId, targetUserId, newRole)) {
                if (adminService.updateUserRole(targetUserId, newRole)) {
                    session.setAttribute("successMessage", "Role updated successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to update role");
                }
            } else {
                session.setAttribute("errorMessage", "Role change not allowed");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error updating role");
        }

        return "redirect:controller?command=admin_panel";
    }
}