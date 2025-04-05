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
    private static final String UPLOAD_DIRECTORY = "/src/main/webapp/";
    private static final int MAX_SIZE = 2 * 1024 * 1024;
    private static final Logger logger = LogManager.getLogger();

    public static String upload(Part filePart, String username, String appPath, String currentAvatarPath) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            logger.warn("No file uploaded");
            return currentAvatarPath; // Возвращаем старый путь, если загрузка не произошла
        }
        logger.info("Запрос на загрузку аватарки: " + (filePart != null ? "есть файл" : "нет файла"));
        logger.info("Размер файла: " + (filePart != null ? filePart.getSize() : "0") + " байт");

        if (filePart.getSize() > MAX_SIZE) {
            logger.warn("File too large: " + filePart.getSize());
            throw new IOException("File too large (max 2MB)");
        }
        if (!filePart.getContentType().startsWith("image/")) {
            logger.warn("Invalid file type: " + filePart.getContentType());
            throw new IOException("Only images allowed");
        }

        // Удаляем старый аватар только после успешной проверки нового файла
        if (currentAvatarPath != null && !currentAvatarPath.equals("/images/default-avatar.png")) {
            deleteOldAvatar(currentAvatarPath, appPath);
        }

        Path uploadPath = Paths.get(appPath.replace("target\\WebApplication-1.0-SNAPSHOT\\", UPLOAD_DIRECTORY), UPLOAD_DIR);
        logger.info("Путь загрузки: " + uploadPath);

        if (!Files.exists(uploadPath)) {
            logger.warn("❌ Папка не существует, создаём...");
            Files.createDirectories(uploadPath);
            logger.info("✅ Папка создана!");
        } else {
            logger.info("✅ Папка уже существует.");
        }


        String ext = filePart.getSubmittedFileName()
                .substring(filePart.getSubmittedFileName().lastIndexOf("."));
        String filename = username + "_" + System.currentTimeMillis() + ext;

        Path filePath = uploadPath.resolve(filename);
        logger.info("Сохраняем файл в: " + filePath);

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            logger.info("✅ Файл успешно загружен!");
        } catch (IOException e) {
            logger.error("❌ Ошибка при загрузке файла", e);
        }


        return "./" + UPLOAD_DIR + "/" + filename;
    }


    private static void deleteOldAvatar(String avatarPath, String appPath) throws IOException {
        Path oldFile = Paths.get(appPath.replace("target\\WebApplication-1.0-SNAPSHOT\\", UPLOAD_DIRECTORY), avatarPath.substring(1));
        Files.deleteIfExists(oldFile);
    }
}