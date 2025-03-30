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
    private static final String INSERT_USERS_SQL =
            "INSERT INTO employee (first_name, last_name, username, password, address, email,verification_token) VALUES (?, ?, ?, ?, ?, ?,?)";
    private static final String GET_ALL_EMPLOYEES = "SELECT * FROM employee";
    private static final String AUTH_QUERY = "SELECT password, is_verified FROM employee WHERE username = ?";
    private static final String GET_USER= "SELECT * FROM employee WHERE username = ?";
    private static final String VERIFY_EMAIL = "UPDATE employee SET is_verified = TRUE WHERE verification_token = ?";
    private static final EmployeeDaoImpl instance = new EmployeeDaoImpl();
    private static final String  IS_VERIFIED= "SELECT is_verified FROM employee WHERE username = ?";
    private EmployeeDaoImpl() {}
    public static EmployeeDaoImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authenticate(String username, String password) {
        LOGGER.info("Authenticating user: {}", username);
        try (
                Connection connection = ConnectionPool.getInstance().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(AUTH_QUERY);) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                boolean isVerified = resultSet.getBoolean("is_verified");
                if (!isVerified) {
                    LOGGER.warn("User {} not verified", username);
                    return false;
                }

                String storedHash = resultSet.getString("password");
                boolean match = PasswordUtil.checkPassword(password, storedHash);
                LOGGER.info("Password match for {}: {}", username, match);
                return match;
            } else {
                LOGGER.warn("User {} not found in database", username);
            }
        } catch (SQLException e) {
            LOGGER.error("Database error during authentication for user {}: {}", username, e.getMessage(), e);
        }
        return false;
    }



    @Override
    public int register(Employee employee, String verificationToken) {
        LOGGER.info("Registering employee: " + employee.getUsername());
        ConnectionPool pool = ConnectionPool.getInstance();
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            preparedStatement.setString(3, employee.getUsername());
            preparedStatement.setString(4, employee.getPassword());
            preparedStatement.setString(5, employee.getAddress());
            preparedStatement.setString(6, employee.getEmail());
            preparedStatement.setString(7, verificationToken );

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                LOGGER.info("Employee {} successfully registered.", employee.getUsername());
            } else {
                LOGGER.warn("Failed to register employee {}", employee.getUsername());
            }
            return result;

        } catch (SQLException e) {
            LOGGER.error("Error while adding employee to database: " + e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public boolean checkIfEmailIsVerified(String username) {
        LOGGER.info("Checking verification status for user: {}", username);
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(IS_VERIFIED)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean("is_verified");
            }
        } catch (SQLException e) {
            LOGGER.error("Error checking verification status: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        LOGGER.info("Fetching employee by username: {}", username);

        try( Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_USER))  {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return new Employee(
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("username"),
                        resultSet.getString("address"),
                        resultSet.getString("email")
                );
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching employee by username {}: {}", username, e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean isEmailExists(String email) {
        LOGGER.info("Checking if email exists: {}", email);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(AUTH_QUERY)){

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            LOGGER.error("Error checking email existence: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean verifyEmail(String verificationToken) {
        LOGGER.info("Verifying email with token: {}", verificationToken);

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(VERIFY_EMAIL)) {

            preparedStatement.setString(1, verificationToken);
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            LOGGER.error("Error verifying email: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_EMPLOYEES);
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
        } catch (SQLException e) {
            LOGGER.error("Error retrieving employees: " + e.getMessage(), e);
        }
        return employees;
    }

}
