<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>Login</title>
</head>
<body>
<h1>Login
</h1>
<br/>
<form action="controller" method="post">
    <input type="hidden" name="command" value="login"/>
    <label>
        Login:
        <input type="text" name="username" value="" required/>
    </label>
    <label>
        Password:
        <input type="password" name="password" value="" required/>
    </label>
    <input type="submit" name="sub" value="submit"/>
    ${login_msg}
    <a href="employeeRegister.jsp">Зарегистрироваться</a>
</form>
</body>
</html>