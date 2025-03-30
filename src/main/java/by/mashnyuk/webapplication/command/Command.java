package by.mashnyuk.webapplication.command;

import jakarta.servlet.http.HttpServletRequest;

public interface Command {
    String execute(HttpServletRequest request);
//    void refresh();
}
