package by.mashnyuk.webapplication.command;

import by.mashnyuk.webapplication.command.impl.*;

public enum CommandType {
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    VERIFY(new VerifyEmailCommand()),
    DEFAULT(new DefaultCommand()),
    EDITPROFILE(new EditProfileCommand());

    Command command;
    CommandType(Command command) {
        this.command = command;
    }
    public static Command define(String commandStr) {
        CommandType current = CommandType.valueOf(commandStr.toUpperCase());
        return current.command;
    }
}
