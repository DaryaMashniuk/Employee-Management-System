package by.mashnyuk.webapplication.service;

import by.mashnyuk.webapplication.entity.UserFile;
import jakarta.servlet.http.Part;
import java.util.List;

public interface UserFileService {
    boolean saveFile(int userId, Part filePart, String description);
    UserFile getFile(int fileId, int userId);
    List<UserFile> getUserFiles(int userId);
    boolean deleteFile(int fileId, int userId);
    boolean updateFileDescription(int fileId, int userId, String description);
    String getFileSizeInMB(long size);
}
