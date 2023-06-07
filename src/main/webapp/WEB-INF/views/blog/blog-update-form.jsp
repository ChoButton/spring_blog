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
    <div class="container">
            <form action="/blog/update" method="POST">
            <div class="row">
                <div class="col-3">
                    <label for="writer" class="form-label">글쓴이</label>
                    <input type="text" class="form-control" id="writer" value="${blog.writer}" placeholder="글쓴이" name="writer" readonly>
                </div>
                <div class="col-3">
                    <label for="title" class="form-label">제목</label>
                    <input type="text" class="form-control" id="title" value="${blog.blogTitle}" placeholder="제목" name="blogTitle">
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <label for="content" class="form-label">본문</label>
                    <textarea class="form-control" id="content" rows="10" name="blogContent">${blog.blogContent}</textarea> <!--textarea는 value 속성이 없어 내용에 넣어줘야함-->
                </div>
            </div>
            <div class="row">
                <div class="col-6">
                    <form>
                        <input type="hidden" name="blogId" value="${blog.blogId}">
                        <input type="submit" class="btn btn-info" value="수정">
                    </form>
                </div>
            </div>
        </form>
        <a href="/blog/list" class="btn btn-primary">목록으로 돌아가기</a>
        
    </div>
</body>
</html>