package by.mashnyuk.webapplication.dao.impl;

import by.mashnyuk.webapplication.dao.AuthDao;
import by.mashnyuk.webapplication.entity.Employee;
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
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(CHECK_VERIFICATION_SQL)) {
                stmt.setString(1, username);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getBoolean("is_verified");
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Verification check error: {}", e.getMessage(), e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return false;
    }

    @Override
    public int register(Employee employee, String verificationToken) {
        LOGGER.info("Registering employee: {}", employee.getUsername());
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(INSERT_SQL)) {
                stmt.setString(1, employee.getFirstName());
                stmt.setString(2, employee.getLastName());
                stmt.setString(3, employee.getUsername());
                stmt.setString(4, employee.getPassword());
                stmt.setString(5, employee.getAddress());
                stmt.setString(6, employee.getEmail());
                stmt.setString(7, verificationToken);

                int result = stmt.executeUpdate();
                if (result > 0) {
                    LOGGER.info("Employee {} registered successfully", employee.getUsername());
                } else {
                    LOGGER.warn("Failed to register employee {}", employee.getUsername());
                }
                return result;
            }
        } catch (SQLException e) {
            LOGGER.error("Registration error for {}: {}", employee.getUsername(), e.getMessage(), e);
            return 0;
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean authenticate(String username, String password) {
        LOGGER.info("Authenticating user: {}", username);
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(AUTH_SQL)) {
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
            }
        } catch (SQLException e) {
            LOGGER.error("Authentication error for user {}: {}", username, e.getMessage(), e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return false;
    }

    @Override
    public boolean verifyEmail(String verificationToken) {
        LOGGER.info("Verifying email with token: {}", verificationToken);
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(VERIFY_EMAIL_SQL)) {
                stmt.setString(1, verificationToken);
                int updated = stmt.executeUpdate();
                boolean success = updated > 0;
                LOGGER.info("Email verification {} for token {}",
                        success ? "succeeded" : "failed", verificationToken);
                return success;
            }
        } catch (SQLException e) {
            LOGGER.error("Email verification error: {}", e.getMessage(), e);
            return false;
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        LOGGER.info("Checking email existence: {}", email);
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(CHECK_EMAIL_EXISTS_SQL)) {
                stmt.setString(1, email);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Email check error: {}", e.getMessage(), e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return false;
    }
}
