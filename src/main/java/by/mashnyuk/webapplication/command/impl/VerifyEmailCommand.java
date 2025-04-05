package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.service.AuthService;
import by.mashnyuk.webapplication.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerifyEmailCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.info("VerifyEmailCommand");
        String verificationToken = request.getParameter("token");

        AuthService authService = AuthServiceImpl.getInstance();
        boolean isVerified = authService.verifyEmail(verificationToken);

        if (isVerified) {
            logger.info("Email verified successfully with token: {}", verificationToken);
            return "login.jsp";
        } else {
            logger.warn("Failed to verify email with token: {}", verificationToken);
            request.setAttribute("error", "Invalid verification token.");
            return "verificationError.jsp";
        }
    }
}