package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.service.UserFileService;
import by.mashnyuk.webapplication.service.impl.UserFileServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteFileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserFileService fileService = UserFileServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("employeeId") == null) {
            logger.warn("Unauthorized attempt to delete file");
            return "login.jsp";
        }

        try {
            int userId = (int) session.getAttribute("employeeId");
            int fileId = Integer.parseInt(request.getParameter("fileId"));

            logger.info("Attempting to delete file ID {} for user ID {}", fileId, userId);

            if (fileService.deleteFile(fileId, userId)) {
                session.setAttribute("successMessage", "File deleted successfully");
                logger.info("File {} deleted successfully by user {}", fileId, userId);
            } else {
                session.setAttribute("errorMessage", "File not found or deletion failed");
                logger.warn("Failed to delete file {} for user {}", fileId, userId);
            }
        } catch (NumberFormatException e) {
            logger.error("Invalid file ID format", e);
            session.setAttribute("errorMessage", "Invalid file ID");
        } catch (Exception e) {
            logger.error("Error deleting file", e);
            session.setAttribute("errorMessage", "Error deleting file: " + e.getMessage());
        }

        return "redirect:controller?command=show_files";
    }
}