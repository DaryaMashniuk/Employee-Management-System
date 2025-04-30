package by.mashnyuk.webapplication.filter;

import by.mashnyuk.webapplication.annotation.AllowedRoles;
import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.command.CommandType;
import by.mashnyuk.webapplication.entity.Employee;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/controller")
public class RoleCheckFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        Employee user = (Employee) request.getAttribute("currentUser");

        if (user != null) {
            String command = request.getParameter("command");
            Command cmd = CommandType.define(command);

            AllowedRoles annotation = cmd.getClass().getAnnotation(AllowedRoles.class);
            if (annotation != null && !hasRequiredRole(user, annotation.value())) {
                ((HttpServletResponse) res).sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(request, res);
    }

    private boolean hasRequiredRole(Employee user, String[] allowedRoles) {
        for (String role : allowedRoles) {
            if (user.getRole().equals(role)) {
                return true;
            }
        }
        return false;
    }
}