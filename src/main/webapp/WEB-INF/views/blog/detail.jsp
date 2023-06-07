<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    div {
        border: 1px solid black;
    }
</style>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
</head>
<body>
    <div class="container">
        <table class="table table-hover">
            <div class="row">
                <div class="col-1">
                    글번호
                </div>
                <div class="col-1">
                    ${blog.blogId}
                </div>
                <div class="col-2">
                    글제목
                </div>
                <div class="col-4">
                    ${blog.blogTitle}
                </div>
                <div class="col-1">
                    작성자
                </div>
                <div class="col-1">
                    ${blog.writer}
                </div>
                <div class="col-1">
                    조회수
                </div>
                <div class="col-1">
                    ${blog.blogCount}
                </div>
            </div>
            <div class="row">
                <div class="col-1">
                    작성일
                </div>
                <div class="col-5">
                    ${blog.publishedAt}
                </div>
                <div class="col-1">
                    수정일
                </div>
                <div class="col-5">
                    ${blog.updatedAt}
                </div>
            </div>
            <div class="row">
                <div class="col-1">
                    본문
                </div>
                <div class="col-11">
                    ${blog.blogContent}
                </div>
            </div>
        </table>
        <a href="/blog/list" class="btn btn-primary">목록으로 돌아가기</a>
        <form action="/blog/delete" method="POST">
            <input type="hidden" name="blogId" value="${blog.blogId}">
            <input type="submit" class="btn btn-primary" value="삭제">
        </form>
        <form action="/blog/updateform" method="POST">
            <input type="hidden" name="blogId" value="${blog.blogId}">
            <input type="submit" class="btn btn-info" value="수정">
        </form>
    </div>
</body>
</html>