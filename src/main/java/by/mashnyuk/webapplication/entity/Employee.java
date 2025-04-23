package by.mashnyuk.webapplication.entity;

import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.dto.impl.EmployeeDtoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Employee implements AbstractEntity {
    private static final Logger logger = LogManager.getLogger();
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String email;
    private byte[] avatar;

    public Employee(EmployeeDtoImpl dto) {
        logger.info("Creating Employee from DTO");
        this.id = dto.getId();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.address = dto.getAddress();
        this.email = dto.getEmail();
        this.avatar = dto.getAvatar();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getAvatar() {
        return avatar;
    }
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}