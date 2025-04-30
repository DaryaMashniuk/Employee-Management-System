package by.mashnyuk.webapplication.dao;

import by.mashnyuk.webapplication.entity.UserFile;
import java.util.List;

public interface UserFileDao {
    boolean saveFile(UserFile file);
    UserFile getFileById(int fileId, int userId);
    List<UserFile> getUserFiles(int userId);
    boolean deleteFile(int fileId, int userId);
    List<UserFile> getFilesByGlobalFlag(boolean isGlobal);
}