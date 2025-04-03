package by.mashnyuk.webapplication.service.impl;

import by.mashnyuk.webapplication.dao.EmployeeDao;
import by.mashnyuk.webapplication.dao.impl.EmployeeDaoImpl;
import by.mashnyuk.webapplication.entity.Employee;
import by.mashnyuk.webapplication.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger log = LogManager.getLogger();
    private EmployeeDao employeeDao = EmployeeDaoImpl.getInstance();
    private static EmployeeServiceImpl instance = new EmployeeServiceImpl();

    private EmployeeServiceImpl() {}
    public static EmployeeServiceImpl getInstance() {
        return instance;
    }

    @Override
    public int editProfile(String firstName, String lastName, String username, String address, String email) {
        log.info("Editing profile data for user: {}", username);
            Employee employee = new Employee(firstName, lastName, username, address, email);
            return employeeDao.editProfile(employee);
    }
    @Override
    public List<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }

    @Override
    public Employee getEmployeeByUsername(String username) {
        return employeeDao.getEmployeeByUsername(username);
    }

    @Override
    public int updateAvatar(String username, String avatarPath) {
        return employeeDao.updateAvatar(username, avatarPath);
    }
}
