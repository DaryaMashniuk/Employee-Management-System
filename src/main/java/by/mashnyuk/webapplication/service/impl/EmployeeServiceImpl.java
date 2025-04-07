package by.mashnyuk.webapplication.service.impl;

import by.mashnyuk.webapplication.dao.EmployeeDao;
import by.mashnyuk.webapplication.dao.impl.EmployeeDaoImpl;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.service.EmployeeService;
import by.mashnyuk.webapplication.util.ImageCompressor;
import by.mashnyuk.webapplication.util.ValidationUtil;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger log = LogManager.getLogger();
    private EmployeeDao employeeDao = EmployeeDaoImpl.getInstance();
    private static EmployeeServiceImpl instance = new EmployeeServiceImpl();

    private EmployeeServiceImpl() {}
    public static EmployeeServiceImpl getInstance() {
        return instance;
    }

    @Override
    public Map<String,String> editProfile(EmployeeDto employeeDto) {
        log.info("Editing profile data for user: {}", employeeDto.getUsername());
        Map<String, String> errors = ValidationUtil.validateProfileUpdate(
                employeeDto.getFirstName(), employeeDto.getLastName(), employeeDto.getAddress(), employeeDto.getEmail());

        if(!errors.isEmpty()) {
            return errors;
        } else {
            employeeDao.editProfile(employeeDto);
            return errors;
        }
    }
    @Override
    public boolean updateEmployeeAvatar(String username, Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return false;
        }

        try (InputStream input = filePart.getInputStream()) {
            byte[] originalData = input.readAllBytes();
            byte[] compressedData = ImageCompressor.compressImage(originalData);
            return employeeDao.updateEmployeeAvatar(username, compressedData);
        } catch (IOException e) {
            log.error("Error processing avatar for {}", username, e);
            return false;
        }
    }

    @Override
    public byte[] getEmployeeAvatar(String username) {
        return employeeDao.getEmployeeAvatar(username);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }

    @Override
    public EmployeeDto getEmployeeByUsername(String username) {
        return employeeDao.getEmployeeByUsername(username);
    }
}
