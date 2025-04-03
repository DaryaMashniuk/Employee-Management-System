package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.AuthService;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.AuthServiceImpl;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import by.mashnyuk.webapplication.util.ValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class RegisterCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request) {
        logger.info("RegisterCommand started");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String email = request.getParameter("email");

        AuthService authService = AuthServiceImpl.getInstance();
        Map<String, String> errors = ValidationUtil.validateRegistration(
                firstName, lastName, username, password, address, email);

        if (!errors.isEmpty()) {
            errors.forEach(request::setAttribute);
            return "employeeRegister.jsp";
        } else {
            authService.register(firstName, lastName, username, password, address, email);
            logger.info("Registration successful for user: {}", username);
            request.setAttribute("emailVerification","The letter has been send to your email, please finish your registration");
            return "employeeRegister.jsp";

        }
        }
}