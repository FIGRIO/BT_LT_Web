<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <title>Manager Dashboard</title>
</head>
<body>
    <nav class="navbar navbar-dark bg-info mb-4">
        <span class="navbar-brand mb-0 h1">Manager Dashboard</span>
        <span class="text-light">Xin chào: ${sessionScope.account.fullname}</span>
        <a href="<c:url value='/logout'/>" class="btn btn-sm btn-outline-light">Đăng xuất</a>
    </nav>

    <div class="container">
        <div class="alert alert-success" role="alert">
            Đây là khu vực quản lý riêng của bạn. Bạn chỉ thấy và thao tác trên các Category do chính bạn tạo ra.
        </div>

        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3>Danh mục Của Tôi</h3>
            <a href="<c:url value='/manager/category/add'/>" class="btn btn-success">
                + Thêm Category Của Tôi
            </a>
        </div>

        <table class="table table-bordered table-hover">
            <thead class="bg-light">
                <tr>
                    <th>ID</th>
                    <th>Tên Danh mục</th>
                    <th>Icon</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${listCate}" var="cate">
                    <tr>
                        <td>${cate.cateId}</td>
                        <td>${cate.cateName}</td>
                        <td>
                            <c:if test="${cate.icons.startsWith('http')}">
                                <img src="${cate.icons}" width="50" height="50">
                            </c:if>
                            <c:if test="${!cate.icons.startsWith('http')}">
                                <img src="<c:url value='/image?fname=${cate.icons}'/>" width="50" height="50">
                            </c:if>
                        </td>
                        <td>
                            <a href="<c:url value='/manager/category/edit?id=${cate.cateId}'/>" class="btn btn-warning btn-sm">Sửa</a>
                            <a href="<c:url value='/manager/category/delete?id=${cate.cateId}'/>" 
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('Xóa category này?');">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                
                <c:if test="${empty listCate}">
                    <tr>
                        <td colspan="4" class="text-center text-muted">Bạn chưa tạo danh mục nào.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</body>
</html>