package by.mashnyuk.webapplication.dao.impl;

import by.mashnyuk.webapplication.dao.AuthDao;
import by.mashnyuk.webapplication.dto.impl.EmployeeDtoImpl;
import by.mashnyuk.webapplication.pool.ConnectionPool;
import by.mashnyuk.webapplication.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDaoImpl implements AuthDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String INSERT_SQL =
            "INSERT INTO employee (first_name, last_name, username, password, address, email, verification_token) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String AUTH_SQL =
            "SELECT password, is_verified FROM employee WHERE username = ?";
    private static final String VERIFY_EMAIL_SQL =
            "UPDATE employee SET is_verified = TRUE WHERE verification_token = ?";
    private static final String CHECK_VERIFICATION_SQL =
            "SELECT is_verified FROM employee WHERE username = ?";
    private static final String CHECK_EMAIL_EXISTS_SQL =
            "SELECT COUNT(*) FROM employee WHERE email = ?";

    private static final AuthDaoImpl instance = new AuthDaoImpl();

    private AuthDaoImpl() {}

    public static AuthDaoImpl getInstance() {
        return instance;
    }

    @Override
    public boolean checkIfEmailIsVerified(String username) {
        LOGGER.info("Checking verification status for: {}", username);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(CHECK_VERIFICATION_SQL)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_verified");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Verification check error for {}: {}", username, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public int register(EmployeeDtoImpl employeeDto, String verificationToken) {
        LOGGER.info("Registering employee: {}", employeeDto.getUsername());
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_SQL)) {

            stmt.setString(1, employeeDto.getFirstName());
            stmt.setString(2, employeeDto.getLastName());
            stmt.setString(3, employeeDto.getUsername());
            stmt.setString(4, employeeDto.getPassword());
            stmt.setString(5, employeeDto.getAddress());
            stmt.setString(6, employeeDto.getEmail());
            stmt.setString(7, verificationToken);

            int result = stmt.executeUpdate();
            if (result > 0) {
                LOGGER.info("Employee {} registered successfully", employeeDto.getUsername());
            } else {
                LOGGER.warn("Failed to register employee {}", employeeDto.getUsername());
            }
            return result;
        } catch (SQLException e) {
            LOGGER.error("Registration error for {}: {}", employeeDto.getUsername(), e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        LOGGER.info("Authenticating user: {}", username);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(AUTH_SQL)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    if (!rs.getBoolean("is_verified")) {
                        LOGGER.warn("User {} not verified", username);
                        return false;
                    }

                    String storedHash = rs.getString("password");
                    boolean match = PasswordUtil.checkPassword(password, storedHash);
                    LOGGER.info("Password match for {}: {}", username, match);
                    return match;
                }
                LOGGER.warn("User {} not found", username);
            }
        } catch (SQLException e) {
            LOGGER.error("Authentication error for user {}: {}", username, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean verifyEmail(String verificationToken) {
        LOGGER.info("Verifying email with token: {}", verificationToken);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(VERIFY_EMAIL_SQL)) {

            stmt.setString(1, verificationToken);
            int updated = stmt.executeUpdate();
            boolean success = updated > 0;
            LOGGER.info("Email verification {} for token {}",
                    success ? "succeeded" : "failed", verificationToken);
            return success;
        } catch (SQLException e) {
            LOGGER.error("Email verification error: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        LOGGER.info("Checking email existence: {}", email);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(CHECK_EMAIL_EXISTS_SQL)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Email check error for {}: {}", email, e.getMessage(), e);
        }
        return false;
    }
}