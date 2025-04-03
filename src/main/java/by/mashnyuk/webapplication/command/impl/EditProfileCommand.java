package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import by.mashnyuk.webapplication.util.AvatarUploader;
import by.mashnyuk.webapplication.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class EditProfileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request) {
        if( "GET".equals(request.getMethod()) ) {
            return handleGet(request);
        } else {
            return handlePost(request);
        }
    }

    private String handleGet(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute("username");
        Employee employee = EmployeeServiceImpl.getInstance().getEmployeeByUsername(username);
        request.setAttribute("employee", employee);
        return "editProfile.jsp";
    }

    public String handlePost(HttpServletRequest request) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String username = request.getParameter("username");
            String address = request.getParameter("address");
            String email = request.getParameter("email");

        String appPath = request.getServletContext().getRealPath("");
        Part avatarPart = null;
        String avatarPath = null;

            EmployeeService employeeService = EmployeeServiceImpl.getInstance();
            Map<String, String> errors = ValidationUtil.validateProfileUpdate(
                    firstName, lastName, address, email);

            if (!errors.isEmpty()) {
                logger.error(errors);
                errors.forEach(request::setAttribute);
                request.setAttribute("employee", new Employee(firstName, lastName, username, address, email));
                return "editProfile.jsp";
            } else {

                employeeService.editProfile(firstName, lastName, username, address, email);

                Employee updatedEmployee = employeeService.getEmployeeByUsername(username);
                try {
                    logger.info("avatar");
                    avatarPart = request.getPart("avatar");
                    avatarPath = AvatarUploader.upload(avatarPart, username, appPath,updatedEmployee.getAvatarPath());
                } catch (IOException | ServletException e) {
                    throw new RuntimeException(e);
                }
                if (avatarPath != null) {
                    employeeService.updateAvatar(username, avatarPath);
                }
                request.setAttribute("employee", updatedEmployee);
                logger.info(updatedEmployee);
                logger.info("Profile successfully edited for user: {}", username);
                return "main.jsp";
            }
    }
}
