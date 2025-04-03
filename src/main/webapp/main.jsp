<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="by.mashnyuk.webapplication.entity.Employee" %>
<%
    Employee employee = (Employee) request.getAttribute("employee");
%>

<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>Your Profile</title>
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Welcome to Your Profile, ${sessionScope.username}</h1>
        <form action="controller" method="get" class="logout-form">
            <input type="hidden" name="command" value="logout"/>
            <button type="submit" class="logout-button">Logout</button>
        </form>
    </div>

    <h2>Your Information</h2>
    <div class="profile-info">
        <img id="avatarPreview"
             src="${employee.avatarPath}"
             alt="Avatar Preview"
             class="avatar-preview">
        <p><strong>First Name:</strong> <%= employee.getFirstName() %></p>
        <p><strong>Last Name:</strong> <%= employee.getLastName() %></p>
        <p><strong>Username:</strong> <%= employee.getUsername() %></p>
        <p><strong>Address:</strong> <%= employee.getAddress() %></p>
        <p><strong>Contact:</strong> <%= employee.getEmail() %></p>
    </div>

    <div class="action-buttons">
        <form action="controller" method="get" class="logout-form">
            <input type="hidden" name="command" value="editProfile"/>
            <button type="submit" class="edit-button">Edit Profile</button>
        </form>
    </div>
</div>
</body>
</html>