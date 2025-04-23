package by.mashnyuk.webapplication.dto.impl;
import by.mashnyuk.webapplication.dto.EmployeeDto;
import by.mashnyuk.webapplication.util.ValidationUtil;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;


public class EmployeeDtoImpl implements EmployeeDto {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String email;
    private byte[] avatar;

    private EmployeeDtoImpl() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{
        private final EmployeeDtoImpl dto = new EmployeeDtoImpl();

        public Builder id(int id) {
            dto.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            dto.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            dto.lastName = lastName;
            return this;
        }

        public Builder username(String username) {
            dto.username = username;
            return this;
        }

        public Builder password(String password) {
            dto.password = password;
            return this;
        }

        public Builder address(String address) {
            dto.address = address;
            return this;
        }

        public Builder email(String email) {
            dto.email = email;
            return this;
        }

        public Builder avatar(byte[] avatar) {
            dto.avatar = avatar;
            return this;
        }


        public EmployeeDtoImpl build() {
            return dto;
        }
    }
    @Override public int getId() { return id; }
    @Override public String getFirstName() { return firstName; }
    @Override public String getLastName() { return lastName; }
    @Override public String getUsername() { return username; }
    public String getPassword() { return password; }
    @Override public String getAddress() { return address; }
    @Override public String getEmail() { return email; }
    @Override
    public byte[] getAvatar() {
        return avatar;
    }

}
