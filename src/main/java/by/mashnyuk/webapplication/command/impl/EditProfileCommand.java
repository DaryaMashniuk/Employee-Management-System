package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class EditProfileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            String username = (String) request.getSession().getAttribute("username");
            Employee employee = EmployeeServiceImpl.getInstance().getEmployeeByUsername(username);
            request.setAttribute("employee", employee);
            return "editProfile.jsp";
        } else {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String username = request.getParameter("username");
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
            if (address == null || address.isEmpty()) {
                errors.put("addressError", "Address is required.");
            }
            if (email == null || email.isEmpty()) {
                errors.put("emailError", "Email is required.");
            }

            if (!errors.isEmpty()) {
                errors.forEach(request::setAttribute);
                request.setAttribute("employee", new Employee(firstName, lastName, username, address, email));
                return "editProfile.jsp";
            }

            int result = employeeService.editProfile(firstName, lastName, username, address, email);

            if (result > 0) {
                Employee updatedEmployee = employeeService.getEmployeeByUsername(username);
                request.setAttribute("employee", updatedEmployee);
                logger.info(updatedEmployee);
                logger.info("Profile successfully edited for user: {}", username);
                return "main.jsp";
            } else {
                switch (result) {
                    case -1 -> errors.put("firstNameError", "Invalid first name.");
                    case -2 -> errors.put("lastNameError", "Invalid last name.");
                    case -5 -> errors.put("addressError", "Invalid address.");
                    case -6 -> errors.put("emailError", "Invalid email.");
                    default -> errors.put("generalError", "Update failed.");
                }

                errors.forEach(request::setAttribute);
                request.setAttribute("employee", new Employee(firstName, lastName, username, address, email));
                return "editProfile.jsp";
            }
        }
    }
}
