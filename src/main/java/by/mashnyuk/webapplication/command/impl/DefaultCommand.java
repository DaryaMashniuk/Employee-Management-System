package by.mashnyuk.webapplication.command.impl;

import by.mashnyuk.webapplication.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "";
    }
}
