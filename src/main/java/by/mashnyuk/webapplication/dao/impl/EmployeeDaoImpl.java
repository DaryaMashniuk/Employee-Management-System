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
            "SELECT first_name, last_name, username, address, email, avatar FROM employee WHERE username = ?";
    private static final String EDIT_PROFILE =
            "UPDATE employee SET first_name = ?, last_name = ?, address = ?, email = ? WHERE username = ?";
    private static final String UPDATE_AVATAR_SQL =
            "UPDATE employee SET avatar = ? WHERE username = ?";
    private static final String GET_AVATAR_SQL =
            "SELECT avatar FROM employee WHERE username = ?";
    private static final EmployeeDaoImpl instance = new EmployeeDaoImpl();
    private EmployeeDaoImpl() {}
    public static EmployeeDaoImpl getInstance() {
        return instance;
    }


    @Override
    public void editProfile(EmployeeDto employeeDto) {
        LOGGER.info("Editing employee: {}", employeeDto.getUsername());
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(EDIT_PROFILE)) {
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
            }
        } catch (SQLException e) {
            LOGGER.error("Update error for {}: {}", employeeDto.getUsername(), e.getMessage(), e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public boolean updateEmployeeAvatar(String username, byte[] avatarData) {
        Connection connection = null;
        try{
            connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_AVATAR_SQL)) {

            stmt.setBytes(1, avatarData);
            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            LOGGER.error("Error updating avatar for {}: {}", username, e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public byte[] getEmployeeAvatar(String username) {
        Connection connection = null;
        try{
            connection = ConnectionPool.getInstance().getConnection();

        try (PreparedStatement stmt = connection.prepareStatement(GET_AVATAR_SQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("avatar");
            }
            }
        } catch (SQLException e) {
            LOGGER.error("Error getting avatar for {}: {}", username, e.getMessage());
        }  finally {
        if (connection != null) {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }
        return null;
    }

    @Override
    public EmployeeDto getEmployeeByUsername(String username) {
        LOGGER.info("Fetching employee by username: {}", username);
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try(PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_SQL)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()){
                    return EmployeeDtoImpl.builder()
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .username(resultSet.getString("username"))
                            .address(resultSet.getString("address"))
                            .email(resultSet.getString("email"))
                            .avatar(resultSet.getBytes("avatar"))
                            .build();
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching employee by username {}: {}", username, e.getMessage(), e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return null;
    }


    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> employees = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_EMPLOYEES_SQL);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    EmployeeDto employee = EmployeeDtoImpl.builder()
                            .firstName(resultSet.getString("first_name"))
                            .lastName(resultSet.getString("last_name"))
                            .username(resultSet.getString("username"))
                            .address(resultSet.getString("address"))
                            .email(resultSet.getString("email"))
                            .build();
                    employees.add(employee);
                }
                LOGGER.info("Loaded {} employees from database", employees.size());
            }
        } catch (SQLException e) {
            LOGGER.error("Error retrieving employees: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return employees;
    }

}
