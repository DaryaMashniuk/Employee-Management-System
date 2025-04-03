package by.mashnyuk.webapplication.service.impl;

import by.mashnyuk.webapplication.dao.AuthDao;
import by.mashnyuk.webapplication.dao.impl.AuthDaoImpl;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.AuthService;
import by.mashnyuk.webapplication.util.EmailUtil;
import by.mashnyuk.webapplication.util.PasswordUtil;
import by.mashnyuk.webapplication.util.VerificationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Base64;

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
    public int register(String firstName, String lastName, String username, String password, String address, String email) {
        log.info("Validating registration data for user: {}", username);

//        if (isEmailExists(email)) {
//            log.warn("Email already exists: {}", email);
//            return -7;
//        }

        String verificationToken = VerificationUtil.generateVerificationToken();
        String hashedPassword = PasswordUtil.hashPassword(password);

        Employee employee = new Employee(firstName, lastName, username, hashedPassword, address, email);
        int result = authDao.register(employee, verificationToken);

        if (result > 0) {
            String verificationLink = verificationEmailAddress + verificationToken;
            String encodedEmail = Base64.getEncoder().encodeToString(email.getBytes());
            verificationLink += "&email=" + encodedEmail;
            EmailUtil.sendVerificationEmail(email, verificationLink);
        }
        return result;
    }
}
