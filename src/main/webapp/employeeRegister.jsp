<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 10.03.2025
  Time: 2:07
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="ISO-8859-1">
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <script src="js/validation.js" defer></script>
  <title>Register employee</title>
</head>
<body>
<div align="center">
  <h1>Employee register form</h1>
  <form action="controller" method="post">
    <input type="hidden" name="command" value="register"/>

    <p>First Name</p>
    <input type="text" name="firstName" value="${firstName}" required/>
    <c:if test="${not empty firstNameError}">
      <p style="color: red;">${firstNameError}</p>
    </c:if>

    <p>Last Name</p>
    <input type="text" name="lastName" value="${lastName}" required/>
    <c:if test="${not empty lastNameError}">
      <p style="color: red;">${lastNameError}</p>
    </c:if>

    <p>Username</p>
    <input type="text" name="username" value="${username}" required/>
    <c:if test="${not empty usernameError}">
      <p style="color: red;">${usernameError}</p>
    </c:if>

    <p>Password</p>
    <input type="password" name="password" required/>
    <c:if test="${not empty passwordError}">
      <p style="color: red;">${passwordError}</p>
    </c:if>

    <p>Address</p>
    <input type="text" name="address" value="${address}" required/>
    <c:if test="${not empty addressError}">
      <p style="color: red;">${addressError}</p>
    </c:if>

    <p>Email</p>
    <input type="text" name="email" value="${email}" required/>
    <c:if test="${not empty emailError}">
      <p style="color: red;">${emailError}</p>
    </c:if>

    <input type="submit" name="sub" value="Submit"/>
  </form>

  <c:if test="${not empty generalError}">
    <p style="color: red;">${generalError}</p>
  </c:if>
  <c:if test="${not empty emailVerification}">
    <p style="color: red;">${emailVerification}</p>
  </c:if>
</div>
</body>
</html>


