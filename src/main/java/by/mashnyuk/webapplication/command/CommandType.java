package by.mashnyuk.webapplication.command;

import by.mashnyuk.webapplication.command.impl.*;

public enum CommandType {
    REGISTER(new RegisterCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    VERIFY(new VerifyEmailCommand()),
    DEFAULT(new DefaultCommand()),
    PROFILE(new ProfileCommand()),
    EDITPROFILE(new EditProfileCommand()),
    GET_AVATAR(new GetAvatarCommand());

    Command command;
    CommandType(Command command) {
        this.command = command;
    }
    public static Command define(String commandStr) {
        CommandType current = CommandType.valueOf(commandStr.toUpperCase());
        return current.command;
    }
}
