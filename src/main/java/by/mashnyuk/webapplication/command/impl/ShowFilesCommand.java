package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.entity.UserFile;
import by.mashnyuk.webapplication.service.UserFileService;
import by.mashnyuk.webapplication.service.impl.UserFileServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowFilesCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserFileService fileService = UserFileServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Show files command");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            return "login.jsp";
        }
        logger.info("Show files command session");
        int userId = (int) session.getAttribute("employeeId");
        List<UserFile> files = fileService.getUserFiles(userId);
        request.setAttribute("userFiles", files);

        logger.info("Show files command session" + files);
        // Очищаем сообщения после показа
        if (session.getAttribute("successMessage") != null) {
            request.setAttribute("successMessage", session.getAttribute("successMessage"));
            session.removeAttribute("successMessage");
        }
        if (session.getAttribute("errorMessage") != null) {
            request.setAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }

        return "files.jsp";
    }
}
