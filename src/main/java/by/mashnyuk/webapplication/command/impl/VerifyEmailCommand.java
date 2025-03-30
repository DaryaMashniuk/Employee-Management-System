package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.service.impl.EmployeeServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerifyEmailCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request) {
        logger.info("VerifyEmailCommand");
        String verificationToken = request.getParameter("token");

        EmployeeService employeeService = EmployeeServiceImpl.getInstance();
        boolean isVerified = employeeService.verifyEmail(verificationToken);

        if (isVerified) {
            logger.info("Email verified successfully with token: {}", verificationToken);
            return "index.jsp";
        } else {
            logger.warn("Failed to verify email with token: {}", verificationToken);
            request.setAttribute("error", "Invalid verification token.");
            return "verificationError.jsp";
        }
    }
}