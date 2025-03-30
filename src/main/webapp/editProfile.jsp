<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 31.03.2025
  Time: 1:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.mashnyuk.webapplication.entity.Employee" %>
<%
    Employee employee = (Employee) request.getAttribute("employee");
%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>Edit Profile</title>
</head>
<body>
<div class="container">
    <h1>Edit Your Profile</h1>
    <% if (request.getAttribute("generalError") != null) { %>
    <div class="error-message">${generalError}</div>
    <% } %>
    <form action="controller" method="post">
        <input type="hidden" name="command" value="editProfile"/>

        <label for="firstName">First Name</label>
        <input type="text" id="firstName" name="firstName" value="<%= employee.getFirstName() %>" required/>
        <% if (request.getAttribute("firstNameError") != null) { %>
        <div class="error-message">${firstNameError}</div>
        <% } %>

        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" name="lastName" value="<%= employee.getLastName() %>" required/>
        <% if (request.getAttribute("lastNameError") != null) { %>
        <div class="error-message">${lastNameError}</div>
        <% } %>

        <label for="username">Username</label>
        <input type="text" id="username" name="username" value="<%= employee.getUsername() %>" required readonly/>
        <% if (request.getAttribute("usernameError") != null) { %>
        <div class="error-message">${usernameError}</div>
        <% } %>

        <label for="address">Address</label>
        <input type="text" id="address" name="address" value="<%= employee.getAddress() %>" required/>
        <% if (request.getAttribute("addressError") != null) { %>
        <div class="error-message">${addressError}</div>
        <% } %>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<%= employee.getEmail() %>" required/>
        <% if (request.getAttribute("emailError") != null) { %>
        <div class="error-message">${emailError}</div>
        <% } %>

        <input type="submit" value="Save Changes" class="submit-button"/>
    </form>
    <a href="main.jsp" class="back-link">Back to Profile</a>
</div>
</body>
</html>
