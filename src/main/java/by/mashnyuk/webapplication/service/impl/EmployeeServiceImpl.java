package by.mashnyuk.webapplication.service.impl;

import by.mashnyuk.webapplication.dao.EmployeeDao;
import by.mashnyuk.webapplication.dao.impl.EmployeeDaoImpl;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.util.EmailUtil;
import by.mashnyuk.webapplication.util.PasswordUtil;
import by.mashnyuk.webapplication.util.ValidationUtil;
import by.mashnyuk.webapplication.util.VerificationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger log = LogManager.getLogger();
    private EmployeeDao employeeDao = EmployeeDaoImpl.getInstance();
    private static EmployeeServiceImpl instance = new EmployeeServiceImpl();
    private static final String verificationEmailAddress = "http://localhost:8080/WebApplication_war_exploded/controller?command=verify&token=";

    private EmployeeServiceImpl() {}
    public static EmployeeServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authenticate(String username, String password) {
        log.info("Trying to authenticate user: {}", username);

        try {
            boolean match = employeeDao.authenticate(username, password);
            log.info(match);
            log.info("Authentication result for {}: {}", username, match);
            return match;
        } catch (Exception e) {
            log.error("Authentication error for user {}: {}", username, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        return employeeDao.isEmailExists(email);
    }

    @Override
    public boolean verifyEmail(String verificationToken) {
        return employeeDao.verifyEmail(verificationToken);
    }

    @Override
    public int editProfile(String firstName, String lastName, String username, String address, String email) {
        log.info("Editing profile data for user: {}", username);

        if (!ValidationUtil.isValidName(firstName)) {
            log.warn("Invalid first name: {}", firstName);
            return -1;
        }
        if (!ValidationUtil.isValidName(lastName)) {
            log.warn("Invalid last name: {}", lastName);
            return -2;
        }
        if (!ValidationUtil.isValidAddress(address)) {
            log.warn("Invalid address for user: {}", username);
            return -5;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            log.warn("Invalid email for user: {}", username);
            return -6;
        }

        Employee employee = new Employee(firstName, lastName, username, address, email);
        int result = employeeDao.editProfile(employee);

        return result;
    }

    @Override
    public int register(String firstName, String lastName, String username, String password, String address, String email) {
        log.info("Validating registration data for user: {}", username);

        if (!ValidationUtil.isValidName(firstName)) {
            log.warn("Invalid first name: {}", firstName);
            return -1;
        }
        if (!ValidationUtil.isValidName(lastName)) {
            log.warn("Invalid last name: {}", lastName);
            return -2;
        }
        if (!ValidationUtil.isValidUsername(username)) {
            log.warn("Invalid username: {}", username);
            return -3;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            log.warn("Weak password for user: {}", username);
            return -4;
        }
        if (!ValidationUtil.isValidAddress(address)) {
            log.warn("Invalid address for user: {}", username);
            return -5;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            log.warn("Invalid email for user: {}", username);
            return -6;
        }
//        if (isEmailExists(email)) {
//            log.warn("Email already exists: {}", email);
//            return -7;
//        }

        String verificationToken = VerificationUtil.generateVerificationToken();
        String hashedPassword = PasswordUtil.hashPassword(password);

        Employee employee = new Employee(firstName, lastName, username, hashedPassword, address, email);
        int result = employeeDao.register(employee, verificationToken);

        if (result > 0) {
            String verificationLink = verificationEmailAddress + verificationToken;
            String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes());
            verificationLink += "&email=" + encodedEmail;
            EmailUtil.sendVerificationEmail(email, verificationLink);
        }
        return result;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        return employeeDao.getEmployeeByUsername(username);
    }
}
