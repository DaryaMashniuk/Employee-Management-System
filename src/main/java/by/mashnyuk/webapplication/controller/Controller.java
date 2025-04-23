package by.mashnyuk.webapplication.controller;

import java.io.*;

import by.mashnyuk.webapplication.command.Command;
import by.mashnyuk.webapplication.command.CommandType;
import by.mashnyuk.webapplication.pool.ConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "controller", value = "/controller")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class Controller extends HttpServlet {
    private String message;
    private final static Logger log = LogManager.getLogger();

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        try {
            String commandStr = request.getParameter("command");
            log.info("Received GET command: " + commandStr);
            Command command = CommandType.define(commandStr);
            String page = command.execute(request,response);
            if (page == null) {
                return;
            }

            if (page.startsWith("redirect:")) {
                String redirectPath = page.substring("redirect:".length());
                response.sendRedirect(redirectPath);
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (Exception e) {
            log.error("Error in GET request", e);
            response.sendRedirect("error.jsp");
        }

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String commandStr = request.getParameter("command");
            log.info("Received command: " + commandStr);
            Command command = CommandType.define(commandStr);
            String page = command.execute(request,response);
            if (page != null && page.startsWith("redirect:")) {
                String redirectPath = page.substring("redirect:".length());
                response.sendRedirect(redirectPath);
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (Exception e) {
            log.error("Error in Controller", e);
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().shutdown();
        System.out.println("🔴 Все соединения закрыты перед завершением работы.");
    }
}