package by.mashnyuk.webapplication.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response);
//    void refresh();
}
