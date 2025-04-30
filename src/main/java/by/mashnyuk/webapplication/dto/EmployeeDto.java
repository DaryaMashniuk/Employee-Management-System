package by.mashnyuk.webapplication.dto;

public interface EmployeeDto {
    int getId();
    String getFirstName();
    String getLastName();
    String getUsername();
    String getAddress();
    String getEmail();
    String getRole();
    byte[] getAvatar();
    boolean isAdmin();
    boolean isSuperAdmin();
    boolean isModerator();
}
