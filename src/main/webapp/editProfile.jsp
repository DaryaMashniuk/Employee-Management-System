<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 31.03.2025
  Time: 1:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.mashnyuk.webapplication.dto.EmployeeDto" %>
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
    <title>Edit Profile</title>
</head>
<body>
<div class="container">
    <h1>Edit Your Profile</h1>
    <% if (request.getAttribute("generalError") != null) { %>
    <div class="error-message">${generalError}</div>
    <% } %>
    <form action="controller" method="post" enctype="multipart/form-data">
        <input type="hidden" name="command" value="editProfile"/>
        <label for="avatar">Profile picture</label>
        <div class="avatar-upload">
            <img id="avatarPreview" src="${pageContext.request.contextPath}/controller?command=get_avatar&username=<%= employee.getUsername() %>" alt="Avatar Preview" class="avatar-preview">
            <input type="file" id="avatar" name="avatar" accept="image/*" style="display: none;">
            <button type="button" onclick="document.getElementById('avatar').click()">Change Avatar</button>
        </div>

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
<script>
    document.addEventListener('DOMContentLoaded', function() {
        console.log("Script")
        const avatarInput = document.getElementById('avatar');
        if (avatarInput) {
            avatarInput.addEventListener('change', function() {
                if (!this.files || !this.files[0]) return;

                // Проверка размера файла (2MB)
                if (this.files[0].size > 2 * 1024 * 1024) {
                    alert('File is too large! Max size is 2MB.');
                    return;
                }

                const reader = new FileReader();
                reader.onload = function(e) {
                    document.getElementById('avatarPreview').src = e.target.result;
                };
                reader.onerror = function() {
                    console.error('Error reading file');
                };
                reader.readAsDataURL(this.files[0]);
            });
        }
    });
</script>
</body>
</html>
