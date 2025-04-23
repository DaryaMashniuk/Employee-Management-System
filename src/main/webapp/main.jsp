<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.mashnyuk.webapplication.dto.EmployeeDto" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%
    EmployeeDto employee = (EmployeeDto) session.getAttribute("employee");
    if (employee == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
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
             src="${pageContext.request.contextPath}/controller?command=get_avatar&username=${employee.username}"
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
        <form action="controller" method="get" class="logout-form">
            <input type="hidden" name="command" value="show_files"/>
            <button type="submit" class="showFiles-button">Employee files</button>
        </form>
    </div>
</div>
</body>
</html>