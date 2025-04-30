<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 30.04.2025
  Time: 11:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Admin Panel</title>
    <style>
        .user-table { width: 100%; border-collapse: collapse; }
        .user-table th, .user-table td { padding: 8px; border: 1px solid #ddd; }
        .user-table tr:nth-child(even) { background-color: #f2f2f2; }
        .role-form { display: inline; }
    </style>
</head>
<body>
<h1>Admin Panel - User Management</h1>

<c:if test="${not empty successMessage}">
    <div style="color: green;">${successMessage}</div>
</c:if>

<c:if test="${not empty errorMessage}">
    <div style="color: red;">${errorMessage}</div>
</c:if>

<table class="user-table">
    <tr>
        <th>ID</th>
        <th>Username</th>
        <th>Email</th>
        <th>Current Role</th>
        <th>Change Role</th>
    </tr>
    <c:forEach items="${users}" var="user">
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.role}</td>
            <td>
                <form class="role-form" action="controller?command=update_user_role" method="post">
                    <input type="hidden" name="userId" value="${user.id}">
                    <select name="role">
                        <option value="USER" ${user.role == 'USER' ? 'selected' : ''}>User</option>
                        <option value="MODERATOR" ${user.role == 'MODERATOR' ? 'selected' : ''}>Moderator</option>
                        <c:if test="${employee.role == 'SUPER_ADMIN'}">
                            <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                        </c:if>
                    </select>
                    <button type="submit">Update</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="index.jsp">Back to Main</a>
</body>
</html>
