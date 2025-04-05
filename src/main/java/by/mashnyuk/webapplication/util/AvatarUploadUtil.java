package by.mashnyuk.webapplication.util;

import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class AvatarUploadUtil {
    private static final Logger logger = LogManager.getLogger();
    private static final String UPLOAD_DIR = "uploads/avatars";
    private static final String WEBAPP_DIR = "/src/main/webapp/";
    private static final int MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB

    public static String uploadAvatar(Part filePart, String username, String appPath, String currentAvatarPath) throws IOException {
        validateFile(filePart);

        // Удаляем старый аватар после успешной валидации нового
        if (shouldDeleteOldAvatar(currentAvatarPath)) {
            deleteOldAvatar(currentAvatarPath, appPath);
        }

        // Подготавливаем директорию для загрузки
        Path uploadPath = prepareUploadDirectory(appPath);

        // Генерируем уникальное имя файла
        String fileName = generateFileName(username, filePart);
        Path filePath = uploadPath.resolve(fileName);

        // Сохраняем файл
        saveFile(filePart, filePath);

        return "./" + UPLOAD_DIR + "/" + fileName;
    }

    private static void validateFile(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            logger.warn("No file uploaded");
            throw new IOException("No file uploaded");
        }

        if (filePart.getSize() > MAX_FILE_SIZE) {
            logger.warn("File too large: {} bytes", filePart.getSize());
            throw new IOException("File too large (max 2MB)");
        }

        if (!filePart.getContentType().startsWith("image/")) {
            logger.warn("Invalid file type: {}", filePart.getContentType());
            throw new IOException("Only images allowed");
        }
    }

    private static boolean shouldDeleteOldAvatar(String currentAvatarPath) {
        return currentAvatarPath != null &&
                !currentAvatarPath.equals("/images/default-avatar.png") &&
                !currentAvatarPath.isEmpty();
    }

    private static void deleteOldAvatar(String avatarPath, String appPath) throws IOException {
        Path oldFile = getFullFilePath(avatarPath, appPath);
        if (Files.exists(oldFile)) {
            Files.delete(oldFile);
            logger.info("Deleted old avatar: {}", oldFile);
        }
    }

    private static Path prepareUploadDirectory(String appPath) throws IOException {
        Path uploadPath = getUploadBasePath(appPath);

        if (!Files.exists(uploadPath)) {
            logger.info("Creating upload directory: {}", uploadPath);
            Files.createDirectories(uploadPath);
        }

        return uploadPath;
    }

    private static Path getUploadBasePath(String appPath) {
        return Paths.get(appPath.replace("target\\WebApplication-1.0-SNAPSHOT\\", WEBAPP_DIR), UPLOAD_DIR);
    }

    private static Path getFullFilePath(String avatarPath, String appPath) {
        return Paths.get(appPath.replace("target\\WebApplication-1.0-SNAPSHOT\\", WEBAPP_DIR),
                avatarPath.substring(1));
    }

    private static String generateFileName(String username, Part filePart) {
        String originalName = filePart.getSubmittedFileName();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        return username + "_" + System.currentTimeMillis() + extension;
    }

    private static void saveFile(Part filePart, Path filePath) throws IOException {
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("File saved successfully: {}", filePath);
        }
    }
}