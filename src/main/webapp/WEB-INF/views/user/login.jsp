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
    <div class=".container">
        <form action="/login" method="POST"><br>
            <div class="col-3">
                <!-- 세션 기반 에서는 아이디는 loginId 입력 -->
                <!--아이디는 username, 비밀번호는 password로 고정-->
                <input type="text" class="form-control" name="loginId" placeholder="아이디">
            </div><br>
            <div class="col-3">
                <input type="password" class="form-control" name="password" placeholder="비밀번호">
            </div><br>
            <input type="submit" class="btn btn-primary" value="로그인">
            <a href="/signup" class="btn btn-primary">회원가입</a>
        </form>
    </div>
</body>
</html>