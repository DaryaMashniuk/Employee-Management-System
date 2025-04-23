<%@ page import="by.mashnyuk.webapplication.entity.UserFile" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%
    List<UserFile> files =(List<UserFile>) request.getAttribute("userFiles");
%>

<html>
<head>
    <title>My Files</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 0;
            padding: 20px;
            background-color: #f5f5f5;
        }
        .container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            color: #333;
            text-align: center;
        }
        .upload-form {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .upload-form input[type="file"] {
            margin-bottom: 10px;
        }
        .upload-form textarea {
            width: 100%;
            padding: 8px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .upload-form button {
            background: #4CAF50;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
        }
        .upload-form button:hover {
            background: #45a049;
        }
        .file-list {
            margin: 20px 0;
        }
        .file-item {
            padding: 15px;
            border: 1px solid #ddd;
            margin-bottom: 10px;
            border-radius: 5px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            background: white;
        }
        .file-info {
            flex-grow: 1;
        }
        .file-actions a {
            margin-left: 10px;
            text-decoration: none;
            padding: 5px 10px;
            border-radius: 3px;
        }
        .download-btn {
            background: #2196F3;
            color: white;
        }
        .download-btn:hover {
            background: #0b7dda;
        }
        .delete-btn {
            background: #f44336;
            color: white;
        }
        .delete-btn:hover {
            background: #da190b;
        }
        .alert {
            padding: 10px;
            margin-bottom: 15px;
            border-radius: 4px;
        }
        .success {
            background-color: #dff0d8;
            color: #3c763d;
            border: 1px solid #d6e9c6;
        }
        .error {
            background-color: #f2dede;
            color: #a94442;
            border: 1px solid #ebccd1;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: #333;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>My Files</h1>

    <c:if test="${not empty successMessage}">
        <div class="alert success">${successMessage}</div>
    </c:if>

    <c:if test="${not empty errorMessage}">
        <div class="alert error">${errorMessage}</div>
    </c:if>

    <div class="upload-form">
        <h3>Upload New File</h3>
        <form action="controller?command=upload_file" method="post" enctype="multipart/form-data">
            <input type="file" name="file" required>
            <br>
            <textarea name="description" placeholder="File description" rows="3"></textarea>
            <br>
            <button type="submit">Upload File</button>
        </form>
    </div>

    <div class="file-list">
        <h3>Your Files</h3>
        <c:choose>
            <c:when test="${not empty userFiles}">
                <c:forEach items="${userFiles}" var="file">
                    <div class="file-item">
                        <div class="file-info">
                            <strong>${file.fileName}</strong><br>
                            <c:if test="${not empty file.description}">
                                ${file.description}<br>
                            </c:if>
                            Uploaded: ${file.uploadDate}
                        </div>
                        <div class="file-actions">
                            <a href="controller?command=download_file&fileId=${file.id}" class="download-btn">Download</a>
                            <a href="controller?command=delete_file&fileId=${file.id}"
                               class="delete-btn" onclick="return confirm('Are you sure you want to delete this file?')">Delete</a>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>No files uploaded yet.</p>
            </c:otherwise>
        </c:choose>
    </div>

    <a href="main.jsp" class="back-link">← Back to Profile</a>
</div>
</body>
</html>