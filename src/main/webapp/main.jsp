<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="by.mashnyuk.webapplication.entity.Employee" %>

<%--<%--%>
<%--    List<Employee> employees = (List<Employee>) request.getAttribute("employees");--%>
<%--%>--%>
<%
    Employee employee = (Employee) request.getAttribute("employee");
%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>Profile</title>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Welcome to Your Profile, ${sessionScope.username}</h1>
        <form action="controller" method="get" class="logout-form">
            <input type="hidden" name="command" value="logout"/>
            <button type="submit" name="sub" value="submit" class="logout-button">Logout</button>
        </form>
    </div>

    <h2>Your Information</h2>
    <div class="profile-info">
        <p><strong>First Name:</strong> <%= employee.getFirstName() %></p>
        <p><strong>Last Name:</strong> <%= employee.getLastName() %></p>
        <p><strong>Username:</strong> <%= employee.getUsername() %></p>
        <p><strong>Address:</strong> <%= employee.getAddress() %></p>
        <p><strong>Contact:</strong> <%= employee.getEmail()%></p>
    </div>

    <div class="action-buttons">
        <form action="editProfile" method="get">
            <button type="submit" class="edit-button">Edit Profile</button>
        </form>
    </div>
</div>
</body>
</html>




<%--    <h2>Registered Users</h2>--%>
<%--    <table>--%>
<%--        <thead>--%>
<%--        <tr>--%>
<%--            <th>First Name</th>--%>
<%--            <th>Last Name</th>--%>
<%--            <th>Username</th>--%>
<%--            <th>Address</th>--%>
<%--            <th>Contact</th>--%>
<%--        </tr>--%>
<%--        </thead>--%>
<%--        <tbody>--%>
<%--        <%--%>
<%--            if (employees != null && !employees.isEmpty()) {--%>
<%--                for (Employee employee : employees) {--%>
<%--        %>--%>
<%--        <tr>--%>
<%--            <td><%= employee.getFirstName() %></td>--%>
<%--            <td><%= employee.getLastName() %></td>--%>
<%--            <td><%= employee.getUsername() %></td>--%>
<%--            <td><%= employee.getAddress() %></td>--%>
<%--            <td><%= employee.getContact() %></td>--%>
<%--        </tr>--%>
<%--        <%--%>
<%--            }--%>
<%--        } else {--%>
<%--        %>--%>
<%--        <tr>--%>
<%--            <td colspan="5">No employees found.</td>--%>
<%--        </tr>--%>
<%--        <%--%>
<%--            }--%>
<%--        %>--%>
<%--        </tbody>--%>
<%--    </table>--%>