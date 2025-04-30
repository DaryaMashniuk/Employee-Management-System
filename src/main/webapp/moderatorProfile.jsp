<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 30.04.2025
  Time: 23:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Moderator Profile</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f5f5f5;
        }
        .container {
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            max-width: 800px;
            margin: 0 auto;
        }
        .upload-form {
            margin: 20px 0;
            padding: 20px;
            background: #f9f9f9;
            border-radius: 5px;
        }
        .file-list {
            margin-top: 30px;
        }
        .file-item {
            padding: 15px;
            border: 1px solid #ddd;
            margin-bottom: 10px;
            border-radius: 5px;
        }
        .btn {
            background: #4CAF50;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn:hover {
            background: #45a049;
        }
        .alert {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }
        .success {
            background-color: #dff0d8;
            color: #3c763d;
        }
        .error {
            background-color: #f2dede;
            color: #a94442;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>Moderator Profile</h1>

    <c:if test="${not empty successMessage}">
        <div class="alert success">${successMessage}</div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert error">${errorMessage}</div>
    </c:if>

    <div class="user-info">
        <h3>Welcome, ${employee.firstName} ${employee.lastName} (Moderator)</h3>
        <p>Username: ${employee.username}</p>
        <p>Email: ${employee.email}</p>
    </div>

    <div class="upload-form">
        <h3>Upload Global File</h3>
        <form action="controller?command=upload_file" method="post" enctype="multipart/form-data">
            <div>
                <label for="file">Select file:</label>
                <input type="file" id="file" name="file" required>
            </div>
            <div>
                <label for="description">Description:</label>
                <textarea id="description" name="description" rows="3" style="width: 100%"></textarea>
            </div>
            <button type="submit" class="btn">Upload Global File</button>
        </form>
    </div>
    <div style="margin-top: 20px;">
        <a href="controller?command=global_Files" class="btn">See company files</a>
    </div>

    <div style="margin-top: 20px;">
        <a href="controller?command=logout" class="btn">Logout</a>
    </div>
</div>
</body>
</html>