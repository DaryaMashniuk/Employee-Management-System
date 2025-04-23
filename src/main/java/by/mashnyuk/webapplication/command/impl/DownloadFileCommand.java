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

import java.io.IOException;

public class DownloadFileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserFileService fileService = UserFileServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            return "login.jsp";
        }

        int userId = (int) session.getAttribute("employeeId");
        int fileId;
        try {
            fileId = Integer.parseInt(request.getParameter("fileId"));
        } catch (NumberFormatException e) {
            return "error.jsp";
        }

        UserFile file = fileService.getFile(fileId, userId);
        if (file == null) {
            return "error.jsp";
        }

        try {
            response.setContentType(file.getFileType());
            response.setHeader("Content-disposition", "attachment; filename=" + file.getFileName());
            response.setContentLengthLong(file.getFileSize());
            response.getOutputStream().write(file.getFileData());

            return "files.jsp"; // Не перенаправляем, ответ уже отправлен
        } catch (IOException e) {
            logger.error("Error downloading file {}: {}", fileId, e.getMessage());
            return "error.jsp";
        }
    }
}
