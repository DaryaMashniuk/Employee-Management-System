package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GetAvatarCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final EmployeeService employeeService = EmployeeServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        if (username == null || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        try {
            byte[] avatarData = employeeService.getEmployeeAvatar(username);

            if (avatarData != null && avatarData.length > 0) {
                response.setContentType("image/jpeg");
                response.setContentLength(avatarData.length);
                response.getOutputStream().write(avatarData);
            } else {
                logger.info(request.getContextPath()+ "/images/default-avatar.png");
                response.sendRedirect(request.getContextPath()+ "/images/default-avatar.png");

            }
        } catch (IOException e) {
            logger.error("Error serving avatar for {}", username, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        return null;
    }
}