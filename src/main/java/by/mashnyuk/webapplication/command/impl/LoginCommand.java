package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.AuthService;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.AuthServiceImpl;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        EmployeeService employeeService = EmployeeServiceImpl.getInstance();
        AuthService authService = AuthServiceImpl.getInstance();
        String page;
        if ( authService.authenticate(username, password) ) {
            logger.info("User logged in");
            HttpSession session = request.getSession();
            // Удаляем старую сессию и создаем новую для защиты от session fixation
            session.invalidate();
            session = request.getSession(true);

            // Устанавливаем атрибуты сессии
            EmployeeDto employee = employeeService.getEmployeeByUsername(username);
            session.setAttribute("employee", employee);
            session.setAttribute("username", username);
            session.setAttribute("employeeId", employee.getId());
            session.setAttribute("authenticated", true);

            // Устанавливаем время жизни сессии (30 минут)
            session.setMaxInactiveInterval(30 * 60);
            page = "redirect:controller?command=profile";
        } else {
            request.setAttribute("login_msg","Invalid login or password");
            page = "login.jsp";
        }
        return page;
    }
}





