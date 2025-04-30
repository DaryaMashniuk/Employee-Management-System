package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.UserFileService;
import by.mashnyuk.webapplication.service.impl.UserFileServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UploadFileCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    private final UserFileService fileService = UserFileServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);

        int userId = (int) session.getAttribute("employeeId");
        Employee employee = (Employee) session.getAttribute("employee");
        try {
            Part filePart = request.getPart("file");
            String description = request.getParameter("description");

            if (employee.isModerator()) {
                if (fileService.saveGlobalFile(filePart, description)) {
                    session.setAttribute("successMessage", "File uploaded successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to upload file");
                }
            } else {
                if (fileService.saveFile(userId, filePart, description, false)) {
                    session.setAttribute("successMessage", "File uploaded successfully");
                } else {
                    session.setAttribute("errorMessage", "Failed to upload file");
                }
            }

        } catch (Exception e) {
            logger.error("Error uploading file", e);
            session.setAttribute("errorMessage", "Error uploading file: " + e.getMessage());
        }
        return "redirect:controller?command=show_files";
    }
}