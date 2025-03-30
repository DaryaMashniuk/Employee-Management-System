package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        EmployeeService employeeService = EmployeeServiceImpl.getInstance();
        String page;
        if ( employeeService.authenticate(username, password) ) {
            logger.info("User logged in");
            request.getSession().setAttribute("username", username);
            Employee employee = employeeService.getEmployeeByUsername(username);
            request.setAttribute("employee", employee);
            page = "main.jsp";
        } else {
            request.setAttribute("login_msg","Invalid login or password");
            page = "index.jsp";
        }
        return page;
    }
}





