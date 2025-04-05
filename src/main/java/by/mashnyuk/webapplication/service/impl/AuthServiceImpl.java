package by.mashnyuk.webapplication.service.impl;

import by.mashnyuk.webapplication.dao.AuthDao;
import by.mashnyuk.webapplication.dao.impl.AuthDaoImpl;
import by.mashnyuk.webapplication.dto.impl.EmployeeDtoImpl;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.AuthService;
import by.mashnyuk.webapplication.util.EmailUtil;
import by.mashnyuk.webapplication.util.PasswordUtil;
import by.mashnyuk.webapplication.util.ValidationUtil;
import by.mashnyuk.webapplication.util.VerificationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Base64;
import java.util.Map;

public class AuthServiceImpl implements AuthService {
    private static final Logger log = LogManager.getLogger();
    private AuthDao authDao = AuthDaoImpl.getInstance();
    private static AuthServiceImpl instance = new AuthServiceImpl();
    private static final String verificationEmailAddress = "http://localhost:8080/WebApplication_war_exploded/controller?command=verify&token=";

    private AuthServiceImpl() {}
    public static AuthServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean authenticate(String username, String password) {
        try {
            boolean match = authDao.authenticate(username, password);
            log.info("Authentication result for {}: {}", username, match);
            return match;
        } catch (Exception e) {
            log.error("Authentication error for user {}: {}", username, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        return authDao.isEmailExists(email);
    }

    @Override
    public boolean verifyEmail(String verificationToken) {
        return authDao.verifyEmail(verificationToken);
    }

    @Override
    public Map<String, String> register(EmployeeDtoImpl employeeDto) {
        log.info("Validating registration data for user: {}", employeeDto.getUsername());
        Map<String, String> errors = ValidationUtil.validateRegistration(
                        employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getUsername(), employeeDto.getPassword(), employeeDto.getAddress(), employeeDto.getEmail());
//        if (isEmailExists(email)) {
//            log.warn("Email already exists: {}", email);
//            return -7;
//        }

        if (!errors.isEmpty()) {
            return errors;
        } else {
            String verificationToken = VerificationUtil.generateVerificationToken();
            int result = authDao.register(employeeDto, verificationToken);
            if (result > 0) {
                String verificationLink = verificationEmailAddress + verificationToken;
                String encodedEmail = Base64.getEncoder().encodeToString(employeeDto.getEmail().getBytes());
                verificationLink += "&email=" + encodedEmail;
                EmailUtil.sendVerificationEmail(employeeDto.getEmail(), verificationLink);
            }
            return errors;
        }
    }
}
