<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<body>
    <form action="/signup" method="post" class="col-3"><br>
        아이디<input type="text" class="form-control" name="loginId" required/><br>
        이메일<input type="email" class="form-control" name="email" required/><br>
        비밀번호<input type="password" class="form-control" name="password" required/><br>
        <input type="submit" class="btn btn-primary" value="가입하기">
        <a href="/login" class="btn btn-primary">가입취소</a>
    </form>
</body>
</html>