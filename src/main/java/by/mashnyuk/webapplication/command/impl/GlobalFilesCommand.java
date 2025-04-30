package by.mashnyuk.webapplication.command.impl;


import by.mashnyuk.webapplication.annotation.AllowedRoles;
import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.UserFileService;
import by.mashnyuk.webapplication.service.impl.UserFileServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@AllowedRoles({"USER","MODERATOR", "SUPER_ADMIN"})
public class GlobalFilesCommand implements Command {
    private final UserFileService fileService = UserFileServiceImpl.getInstance();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        EmployeeDto user = (EmployeeDto) session.getAttribute("employee");
        if (user.isAdmin()) {
            return "error.jsp";
        }

        request.setAttribute("globalFiles", fileService.getGlobalFiles());
        return "globalFiles.jsp";
    }
}