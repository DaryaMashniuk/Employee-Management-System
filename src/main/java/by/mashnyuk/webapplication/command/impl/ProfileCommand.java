package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProfileCommand implements Command {
    private final EmployeeService employeeService = EmployeeServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authenticated") == null) {
            return "login.jsp";
        }

        String username = (String) session.getAttribute("username");
        EmployeeDto employee = employeeService.getEmployeeByUsername(username);
        request.setAttribute("employee", employee);

        return "main.jsp";
    }
}
