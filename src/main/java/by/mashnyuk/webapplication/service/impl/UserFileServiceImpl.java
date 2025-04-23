package by.mashnyuk.webapplication.service.impl;

import by.mashnyuk.webapplication.dao.UserFileDao;
import by.mashnyuk.webapplication.dao.impl.UserFileDaoImpl;
import by.mashnyuk.webapplication.entity.UserFile;
import by.mashnyuk.webapplication.service.UserFileService;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class UserFileServiceImpl implements UserFileService {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private final UserFileDao userFileDao = UserFileDaoImpl.getInstance();

    private static final UserFileService instance = new UserFileServiceImpl();
    public static UserFileService getInstance() {return instance;}

    @Override
    public boolean saveFile(int userId, Part filePart, String description) {
        if (filePart == null || filePart.getSize() == 0) {
            return false;
        }

        if (filePart.getSize() > MAX_FILE_SIZE) {
            LOGGER.warn("File too large: {} bytes", filePart.getSize());
            return false;
        }

        try {
            UserFile file = new UserFile(
                    userId,
                    filePart.getSubmittedFileName(),
                    filePart.getSize(),
                    filePart.getContentType(),
                    filePart.getInputStream().readAllBytes(),
                    description
            );
            return userFileDao.saveFile(file);
        } catch (IOException e) {
            LOGGER.error("Error saving file for user {}: {}", userId, e.getMessage());
            return false;
        }
    }

    @Override
    public UserFile getFile(int fileId, int userId) {
        return userFileDao.getFileById(fileId, userId);
    }

    @Override
    public List<UserFile> getUserFiles(int userId) {
        return userFileDao.getUserFiles(userId);
    }

    @Override
    public boolean deleteFile(int fileId, int userId) {
        return userFileDao.deleteFile(fileId, userId);
    }

    @Override
    public boolean updateFileDescription(int fileId, int userId, String description) {
        return userFileDao.updateFileDescription(fileId, userId, description);
    }

    @Override
    public String getFileSizeInMB(long size) {
        return String.format("%.2f MB", (double) size / (1024 * 1024));
    }
}