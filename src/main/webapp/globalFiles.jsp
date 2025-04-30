<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 30.04.2025
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>Global Files</title>
    <style>
        .file-list { margin: 20px 0; }
        .file-item { padding: 10px; border: 1px solid #ddd; margin-bottom: 10px; }
    </style>
</head>
<body>
<h1>Global Files</h1>

<div class="file-list">
    <c:forEach items="${globalFiles}" var="file">
        <div class="file-item">
            <strong>${file.fileName}</strong><br>
                ${file.description}<br>
            Size: ${fileService.getFileSizeInMB(file.fileSize)}<br>
            Uploaded by: ${file.userId == 0 ? 'System' : file.userId}<br>
            <a href="controller?command=download_file&fileId=${file.id}">Download</a>
        </div>
    </c:forEach>
</div>

<a href="main.jsp">Back to Main</a>
</body>
</html>
