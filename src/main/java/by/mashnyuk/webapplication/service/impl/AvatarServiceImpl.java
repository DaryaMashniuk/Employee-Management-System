package by.mashnyuk.webapplication.service.impl;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AvatarServiceImpl {
    private static final Logger log = LogManager.getLogger();
    private static final String UPLOAD_DIR = "uploads/avatars";
    private static final int MAX_FILE_SIZE = 2 * 1024 * 1024; // 2MB
    private final ServletContext servletContext;

    public AvatarServiceImpl(ServletContext context) {
        this.servletContext = context;
    }

    public String uploadAvatar(Part filePart, String username) throws IOException {
        validateFile(filePart);

        String fileName = generateFileName(username, filePart);
        String uploadPath = servletContext.getRealPath("/uploads/avatars");

        return "/" + UPLOAD_DIR + "/" + fileName;
    }

    private void validateFile(Part filePart) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            throw new IOException("File is empty");
        }
        if (filePart.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File too large (max 2MB)");
        }
        if (!filePart.getContentType().startsWith("image/")) {
            throw new IOException("Only images are allowed");
        }
    }

    private String generateFileName(String username, Part filePart) {
        String ext = filePart.getSubmittedFileName()
                .substring(filePart.getSubmittedFileName().lastIndexOf("."));
        return username + "_" + System.currentTimeMillis() + ext;
    }

    private Path prepareUploadDirectory() throws IOException {
        Path uploadPath = Paths.get(servletContext.getRealPath("/uploads/avatars"), UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        return uploadPath;
    }

    private void saveFile(Part filePart, Path filePath) throws IOException {
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
