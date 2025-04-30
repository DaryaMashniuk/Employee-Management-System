package by.mashnyuk.webapplication.service;

import by.mashnyuk.webapplication.entity.UserFile;
import jakarta.servlet.http.Part;
import java.util.List;

public interface UserFileService {
    boolean saveFile(int userId, Part filePart, String description, boolean isGlobal);
    UserFile getFile(int fileId, int userId);
    List<UserFile> getUserFiles(int userId);
    boolean deleteFile(int fileId, int userId);
    String getFileSizeInMB(long size);
    List<UserFile> getGlobalFiles();
    boolean saveGlobalFile(Part filePart, String description);
}
