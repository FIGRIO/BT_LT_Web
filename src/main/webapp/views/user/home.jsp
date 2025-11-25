<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <title>Trang Chủ</title>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand" href="#">IoTStar System</a>
            <div class="ml-auto">
                <c:if test="${not empty sessionScope.account}">
                    <span class="mr-3">Hi, ${sessionScope.account.fullname}</span>
                    <a href="<c:url value='/logout'/>" class="btn btn-outline-secondary btn-sm">Đăng xuất</a>
                </c:if>
                <c:if test="${empty sessionScope.account}">
                    <a href="<c:url value='/login'/>" class="btn btn-primary btn-sm">Đăng nhập</a>
                </c:if>
            </div>
        </div>
    </nav>

    <div class="container">
        <h2 class="text-center mb-4">Danh Sách Thể Loại</h2>
        
        <div class="row">
            <c:forEach items="${listCate}" var="cate">
                <div class="col-md-3 mb-4">
                    <div class="card h-100 shadow-sm">
                         <div class="card-body text-center d-flex flex-column justify-content-center align-items-center">
                            <c:if test="${cate.icons.startsWith('http')}">
                                <img src="${cate.icons}" class="img-fluid mb-3" style="max-height: 80px;">
                            </c:if>
                            <c:if test="${!cate.icons.startsWith('http')}">
                                <img src="<c:url value='/image?fname=${cate.icons}'/>" class="img-fluid mb-3" style="max-height: 80px;">
                            </c:if>
                            
                            <h5 class="card-title">${cate.cateName}</h5>
                            <p class="card-text"><small class="text-muted">By: ${cate.user.fullname}</small></p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>