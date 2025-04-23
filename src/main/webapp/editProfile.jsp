<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.mashnyuk.webapplication.dto.EmployeeDto" %>
<%
    EmployeeDto employee = (EmployeeDto) request.getAttribute("employee");
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
    <form action="controller" method="post">
        <input type="hidden" name="command" value="editProfile"/>

        <label for="firstName">First Name</label>
        <input type="text" id="firstName" name="firstName" value="<%= employee.getFirstName() %>" required/>

        <label for="lastName">Last Name</label>
        <input type="text" id="lastName" name="lastName" value="<%= employee.getLastName() %>" required/>

        <label for="username">Username</label>
        <input type="text" id="username" name="username" value="<%= employee.getUsername() %>" required readonly/>

        <label for="address">Address</label>
        <input type="text" id="address" name="address" value="<%= employee.getAddress() %>" required/>

        <label for="email">Email</label>
        <input type="email" id="email" name="email" value="<%= employee.getEmail() %>" required/>

        <label for="description">Description</label>
        <textarea id="description" name="description" class="description-textarea"><%=
        employee.getDescription() != null ? employee.getDescription() : "" %></textarea>

        <input type="submit" value="Save Changes" class="submit-button"/>
    </form>
    <a href="main.jsp" class="back-link">Back to Profile</a>
</div>
</body>
</html>