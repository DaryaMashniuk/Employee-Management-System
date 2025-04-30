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
    GET_AVATAR(new GetAvatarCommand()),
    UPLOAD_FILE(new UploadFileCommand()),
    SHOW_FILES(new ShowFilesCommand()),
    DOWNLOAD_FILE(new DownloadFileCommand()),
    ADMIN_PANEL(new AdminPanelCommand()),
    UPDATE_USER_ROLE(new UpdateUserRoleCommand()),
    GLOBAL_FILES(new GlobalFilesCommand()),
    MODERATOR_PROFILE(new ModeratorProfileCommand()),
    DELETE_FILE(new DeleteFileCommand());

    Command command;
    CommandType(Command command) {
        this.command = command;
    }
    public static Command define(String commandStr) {
        CommandType current = CommandType.valueOf(commandStr.toUpperCase());
        return current.command;
    }
}