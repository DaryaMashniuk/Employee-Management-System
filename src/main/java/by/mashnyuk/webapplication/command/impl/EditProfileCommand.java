package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.dto.impl.EmployeeDtoImpl;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class EditProfileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final EmployeeService employeeService = EmployeeServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("authenticated") == null) {
            return "login.jsp";
        }

        if ("GET".equals(request.getMethod())) {
            return handleGet(request, session);
        } else {
            return handlePost(request, session);
        }
    }

    private String handleGet(HttpServletRequest request, HttpSession session) {
        return "editProfile.jsp";
    }

    private String handlePost(HttpServletRequest request, HttpSession session) {
        String username = (String) session.getAttribute("username");

        EmployeeDto employeeDto = EmployeeDtoImpl.builder()
                .firstName(request.getParameter("firstName"))
                .lastName(request.getParameter("lastName"))
                .username(username)
                .address(request.getParameter("address"))
                .email(request.getParameter("email"))
                .build();

        Map<String, String> errors = employeeService.editProfile(employeeDto);

        try {
            Part filePart = request.getPart("avatar");
            if (filePart != null && filePart.getSize() > 0) {
                employeeService.updateEmployeeAvatar(username, filePart);
            }
        } catch (Exception e) {
            logger.error("Error uploading avatar", e);
            request.setAttribute("avatarError", "Error uploading avatar");
        }

        if (!errors.isEmpty()) {
            errors.forEach(request::setAttribute);
            return "editProfile.jsp";
        }

        return "redirect:main.jsp";
    }
}