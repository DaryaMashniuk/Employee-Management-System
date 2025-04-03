package by.mashnyuk.webapplication.util;

import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AvatarUploader {
    private static final String UPLOAD_DIR = "uploads/avatars";
    private static final int MAX_SIZE = 2 * 1024 * 1024;
    private static final Logger logger = LogManager.getLogger();

    public static String upload(Part filePart, String username, String appPath,String currentAvatarPath) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            logger.warn("filePart is null or empty");
            return null;
        }

        if (currentAvatarPath != null && !currentAvatarPath.equals("/images/default-avatar.png")) {
            logger.warn("current avatar path is " + currentAvatarPath);
            deleteOldAvatar(currentAvatarPath, appPath);
        }

        if (filePart.getSize() > MAX_SIZE) {
            logger.warn("file size is " + filePart.getSize());
            throw new IOException("File too large (max 2MB)");
        }
        if (!filePart.getContentType().startsWith("image/")) {
            logger.warn("file type is " + filePart.getContentType());
            throw new IOException("Only images allowed");
        }

        Path uploadPath = Paths.get(appPath, UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            logger.warn("upload path does not exist");
            Files.createDirectories(uploadPath);
        }

        String ext = filePart.getSubmittedFileName()
                .substring(filePart.getSubmittedFileName().lastIndexOf("."));
        String filename = username + "_" + System.currentTimeMillis() + ext;

        Path filePath = uploadPath.resolve(filename);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/" + UPLOAD_DIR + "/" + filename;
    }

    private static void deleteOldAvatar(String avatarPath, String appPath) throws IOException {
        Path oldFile = Paths.get(appPath, avatarPath.substring(1));
        Files.deleteIfExists(oldFile);
    }
}