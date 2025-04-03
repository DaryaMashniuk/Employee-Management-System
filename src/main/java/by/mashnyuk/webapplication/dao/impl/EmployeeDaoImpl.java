package by.mashnyuk.webapplication.dao.impl;

import by.mashnyuk.webapplication.dao.EmployeeDao;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.pool.ConnectionPool;
import by.mashnyuk.webapplication.util.PasswordUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String FIND_ALL_EMPLOYEES_SQL = "SELECT * FROM employee";
    private static final String FIND_BY_USERNAME_SQL =
            "SELECT first_name, last_name, username, address, email, avatar_path FROM employee WHERE username = ?";
    private static final String EDIT_PROFILE =
            "UPDATE employee SET first_name = ?, last_name = ?, address = ?, email = ? WHERE username = ?";
    private static final String UPDATE_AVATAR_SQL =
            "UPDATE employee SET avatar_path = ? WHERE username = ?";
    private static final EmployeeDaoImpl instance = new EmployeeDaoImpl();
    private EmployeeDaoImpl() {}
    public static EmployeeDaoImpl getInstance() {
        return instance;
    }


    @Override
    public int editProfile(Employee employee) {
        LOGGER.info("Editing employee: {}", employee.getUsername());
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement stmt = connection.prepareStatement(EDIT_PROFILE)) {
                stmt.setString(1, employee.getFirstName());
                stmt.setString(2, employee.getLastName());
                stmt.setString(3, employee.getAddress());
                stmt.setString(4, employee.getEmail());
                stmt.setString(5, employee.getUsername());

                int result = stmt.executeUpdate();
                if (result > 0) {
                    LOGGER.info("Employee {} updated successfully", employee.getUsername());
                } else {
                    LOGGER.warn("Failed to update employee {}", employee.getUsername());
                }
                return result;
            }
        } catch (SQLException e) {
            LOGGER.error("Update error for {}: {}", employee.getUsername(), e.getMessage(), e);
            return 0;
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
    }

    @Override
    public int updateAvatar(String username, String avatarPath) {
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try(PreparedStatement statement = connection.prepareStatement(UPDATE_AVATAR_SQL)) {
                LOGGER.info("Updating avatar for {}", username);
                statement.setString(1, avatarPath);
                statement.setString(2, username);
                return statement.executeUpdate();
            }
        } catch (SQLException e) {
                LOGGER.error("Update error for {}: {}", username, e.getMessage(), e);
        } finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return 0;
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        LOGGER.info("Fetching employee by username: {}", username);
        Employee employee = null;
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try(PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME_SQL))  {

                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()){
                    return new Employee(
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("username"),
                            resultSet.getString("address"),
                            resultSet.getString("email"),
                            resultSet.getString("avatar_path")
                    );
                }
            } catch (SQLException e) {
                LOGGER.error("Error fetching employee by username {}: {}", username, e.getMessage(), e);
            }
        }finally {
            if (connection != null) {
                ConnectionPool.getInstance().releaseConnection(connection);
            }
        }
        return employee;
    }


    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement statement = connection.prepareStatement(FIND_ALL_EMPLOYEES_SQL);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Employee employee = new Employee(
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("username"),
                            resultSet.getString("address"),
                            resultSet.getString("email")
                    );
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
