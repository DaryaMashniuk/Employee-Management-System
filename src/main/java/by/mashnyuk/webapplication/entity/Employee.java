package by.mashnyuk.webapplication.entity;

public class Employee implements AbstractEntity {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String address;
    private String email;

    public Employee(String firstName, String lastName, String username, String password, String address, String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.address = address;
        this.email = email;

    }
    public Employee(String firstName, String lastName, String username, String address, String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.address = address;
        this.email = email;

    }

    public Employee(String firstName, String lastName, String address, String email) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;

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
}