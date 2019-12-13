<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Login</title>
</head>
<body>
param id: <%= request.getParameter("id") %><br />
sess id: <%= session.getAttribute("id") %><br />

param password: <%= request.getParameter("password") %><br />
sess password: <%= session.getAttribute("password") %><br />
<a href="mypage">mypage</a>
</body>
</html>