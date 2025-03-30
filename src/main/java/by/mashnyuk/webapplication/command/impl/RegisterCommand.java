package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
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

        EmployeeService employeeService = EmployeeServiceImpl.getInstance();
        Map<String, String> errors = new HashMap<>();

        if (firstName == null || firstName.isEmpty()) {
            errors.put("firstNameError", "First name is required.");
        }
        if (lastName == null || lastName.isEmpty()) {
            errors.put("lastNameError", "Last name is required.");
        }
        if (username == null || username.isEmpty()) {
            errors.put("usernameError", "Username is required.");
        }
        if (password == null || password.isEmpty()) {
            errors.put("passwordError", "Password is required.");
        }
        if (address == null || address.isEmpty()) {
            errors.put("addressError", "Address is required.");
        }
        if (email == null || email.isEmpty()) {
            errors.put("emailError", "Email is required.");
        }

        if (!errors.isEmpty()) {
            errors.forEach(request::setAttribute);
            return "employeeRegister.jsp";
        }

        int result = employeeService.register(firstName, lastName, username, password, address, email);

        if (result > 0) {
            logger.info("Registration successful for user: {}", username);
            request.setAttribute("emailVerification","The letter has been send to your email, please finish your registration");
            return "employeeRegister.jsp";
        } else {
            switch (result) {
                case -1 -> errors.put("firstNameError", "Invalid first name.");
                case -2 -> errors.put("lastNameError", "Invalid last name.");
                case -3 -> errors.put("usernameError", "Invalid username.");
                case -4 -> errors.put("passwordError", "Weak password.");
                case -5 -> errors.put("addressError", "Invalid address.");
                case -6 -> errors.put("emailError", "Invalid email.");
                case -7 -> errors.put("emailError", "Email already exists.");
                default -> errors.put("generalError", "Registration failed.");
            }

            errors.forEach(request::setAttribute);
            return "employeeRegister.jsp";
        }
    }
}