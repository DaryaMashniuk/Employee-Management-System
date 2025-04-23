package by.mashnyuk.webapplication.dao.impl;

import by.mashnyuk.webapplication.dao.UserFileDao;
import by.mashnyuk.webapplication.entity.UserFile;
import by.mashnyuk.webapplication.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserFileDaoImpl implements UserFileDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SAVE_FILE_SQL = "INSERT INTO user_files (user_id, file_name, file_size, file_type, file_data, description) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_FILE_SQL = "SELECT * FROM user_files WHERE id = ? AND user_id = ?";
    private static final String GET_USER_FILES_SQL = "SELECT id, file_name, file_size, file_type, upload_date, description FROM user_files WHERE user_id = ? ORDER BY upload_date DESC";
    private static final String DELETE_FILE_SQL = "DELETE FROM user_files WHERE id = ? AND user_id = ?";
    private static final String UPDATE_DESCRIPTION_SQL = "UPDATE user_files SET description = ? WHERE id = ? AND user_id = ?";

    private static final UserFileDaoImpl instance = new UserFileDaoImpl();
    public static UserFileDaoImpl getInstance() {return instance;}

    @Override
    public boolean saveFile(UserFile file) {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_FILE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, file.getUserId());
            stmt.setString(2, file.getFileName());
            stmt.setLong(3, file.getFileSize());
            stmt.setString(4, file.getFileType());
            stmt.setBytes(5, file.getFileData());
            stmt.setString(6, file.getDescription());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    file.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            LOGGER.error("Error saving file for user {}: {}", file.getUserId(), e.getMessage());
            return false;
        }
    }

    @Override
    public UserFile getFileById(int fileId, int userId) {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_FILE_SQL)) {

            stmt.setInt(1, fileId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    UserFile file = new UserFile();
                    file.setId(rs.getInt("id"));
                    file.setUserId(rs.getInt("user_id"));
                    file.setFileName(rs.getString("file_name"));
                    file.setFileSize(rs.getLong("file_size"));
                    file.setFileType(rs.getString("file_type"));
                    file.setFileData(rs.getBytes("file_data"));
                    file.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
                    file.setDescription(rs.getString("description"));
                    return file;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting file {} for user {}: {}", fileId, userId, e.getMessage());
        }
        return null;
    }

    @Override
    public List<UserFile> getUserFiles(int userId) {
        List<UserFile> files = new ArrayList<>();
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT id, file_name, file_size, file_type, upload_date, description " +
                             "FROM user_files WHERE user_id = ? ORDER BY upload_date DESC")) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserFile file = new UserFile();
                file.setId(rs.getInt("id"));
                file.setUserId(userId);
                file.setFileName(rs.getString("file_name"));
                file.setFileSize(rs.getLong("file_size"));
                file.setFileType(rs.getString("file_type"));
                file.setUploadDate(rs.getTimestamp("upload_date").toLocalDateTime());
                file.setDescription(rs.getString("description"));
                files.add(file);

                LOGGER.debug("Loaded file: {}", file.getFileName());
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting files for user {}: {}", userId, e.getMessage());
        }
        return files;
    }

    @Override
    public boolean deleteFile(int fileId, int userId) {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_FILE_SQL)) {

            stmt.setInt(1, fileId);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error deleting file {} for user {}: {}", fileId, userId, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateFileDescription(int fileId, int userId, String description) {
        try (Connection conn = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_DESCRIPTION_SQL)) {

            stmt.setString(1, description);
            stmt.setInt(2, fileId);
            stmt.setInt(3, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating description for file {}: {}", fileId, e.getMessage());
            return false;
        }
    }
}