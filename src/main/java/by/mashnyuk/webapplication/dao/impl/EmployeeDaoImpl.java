package by.mashnyuk.webapplication.dao.impl;

import by.mashnyuk.webapplication.dao.EmployeeDao;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.dto.impl.EmployeeDtoImpl;
import by.mashnyuk.webapplication.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String FIND_ALL_EMPLOYEES_SQL = "SELECT * FROM employee";
    private static final String FIND_BY_USERNAME_SQL =
            "SELECT id,first_name, last_name, username, address, email, avatar,role FROM employee WHERE username = ?";
    private static final String EDIT_PROFILE =
            "UPDATE employee SET first_name = ?, last_name = ?, address = ?, email = ? WHERE username = ?";
    private static final String UPDATE_AVATAR_SQL =
            "UPDATE employee SET avatar = ? WHERE username = ?";
    private static final String UPDATE_USER_ROLE = "UPDATE employee SET role = ? WHERE id = ?";
    private static final String GET_AVATAR_SQL =
            "SELECT avatar FROM employee WHERE username = ?";
    private static final String FIND_BY_ID_SQL =
            "SELECT id, first_name, last_name, username, address, email, avatar,role FROM employee WHERE id = ?";

    private static final EmployeeDaoImpl instance = new EmployeeDaoImpl();

    private EmployeeDaoImpl() {}

    public static EmployeeDaoImpl getInstance() {
        return instance;
    }

    @Override
    public void editProfile(EmployeeDto employeeDto) {
        LOGGER.info("Editing employee: {}", employeeDto.getUsername());
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(EDIT_PROFILE)) {

            stmt.setString(1, employeeDto.getFirstName());
            stmt.setString(2, employeeDto.getLastName());
            stmt.setString(3, employeeDto.getAddress());
            stmt.setString(4, employeeDto.getEmail());
            stmt.setString(5, employeeDto.getUsername());


            int result = stmt.executeUpdate();
            if (result > 0) {
                LOGGER.info("Employee {} updated successfully", employeeDto.getUsername());
            } else {
                LOGGER.warn("Failed to update employee {}", employeeDto.getUsername());
            }
        } catch (SQLException e) {
            LOGGER.error("Update error for {}: {}", employeeDto.getUsername(), e.getMessage(), e);
        }
    }

    @Override
    public boolean updateEmployeeAvatar(String username, byte[] avatarData) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(UPDATE_AVATAR_SQL)) {

            stmt.setBytes(1, avatarData);
            stmt.setString(2, username);

            boolean success = stmt.executeUpdate() > 0;
            LOGGER.info("Avatar update for {} {}", username, success ? "succeeded" : "failed");
            return success;
        } catch (SQLException e) {
            LOGGER.error("Error updating avatar for {}: {}", username, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean updateEmployeeRole(int userId, String newRole) {
        try(Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(UPDATE_USER_ROLE)) {

            stmt.setString(1, newRole);
            stmt.setInt(2, userId);

            boolean success = stmt.executeUpdate() > 0;
            LOGGER.info("Role update for {} {}", userId, success ? "succeeded" : "failed");
            return success;
        } catch (SQLException e) {
            LOGGER.error("Error updating role for {}: {}", userId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public byte[] getEmployeeAvatar(String username) {
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(GET_AVATAR_SQL)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("avatar");
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting avatar for {}: {}", username, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> employees = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_EMPLOYEES_SQL);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                EmployeeDto employee = EmployeeDtoImpl.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .username(resultSet.getString("username"))
                        .address(resultSet.getString("address"))
                        .email(resultSet.getString("email"))
                        .avatar(resultSet.getBytes("avatar"))
                        .role(resultSet.getString("role"))
                        .build();
                employees.add(employee);
            }
            LOGGER.info("Loaded {} employees from database", employees.size());
        } catch (SQLException e) {
            LOGGER.error("Error retrieving employees: {}", e.getMessage(), e);
        }
        return employees;
    }
    @Override
    public EmployeeDto getEmployeeById(int id) {
        LOGGER.info("Fetching employee by id: {}", id);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return EmployeeDtoImpl.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .username(resultSet.getString("username"))
                        .address(resultSet.getString("address"))
                        .email(resultSet.getString("email"))
                        .avatar(resultSet.getBytes("avatar"))
                        .role(resultSet.getString("role"))
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching employee by id {}: {}", id, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public EmployeeDto getEmployeeByUsername(String username) {
        LOGGER.info("Fetching employee by username: {}", username);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_SQL)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return EmployeeDtoImpl.builder()
                        .id(resultSet.getInt("id")) // Добавляем id
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .username(resultSet.getString("username"))
                        .address(resultSet.getString("address"))
                        .email(resultSet.getString("email"))
                        .avatar(resultSet.getBytes("avatar"))
                        .role(resultSet.getString("role"))
                        .build();
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching employee by username {}: {}", username, e.getMessage(), e);
        }
        return null;
    }
}