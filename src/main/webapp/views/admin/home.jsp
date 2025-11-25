<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.1/dist/css/bootstrap.min.css">
    <title>Admin Dashboard</title>
</head>
<body>
    <nav class="navbar navbar-dark bg-dark mb-4">
        <span class="navbar-brand mb-0 h1">Admin Dashboard</span>
        <span class="text-light">Xin chào: ${sessionScope.account.fullname}</span>
        <a href="<c:url value='/logout'/>" class="btn btn-sm btn-outline-light">Đăng xuất</a>
    </nav>

    <div class="container">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h3>Quản lý Tất cả Danh mục</h3>
            <a href="<c:url value='/admin/category/add'/>" class="btn btn-primary">
                + Thêm Category Mới
            </a>
        </div>

        <div class="table-responsive">
            <table class="table table-bordered table-striped">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Tên Danh mục</th>
                        <th>Icon/Ảnh</th>
                        <th>Người tạo</th> <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listCate}" var="cate">
                        <tr>
                            <td>${cate.cateId}</td>
                            <td>${cate.cateName}</td>
                            <td>
                                <c:if test="${cate.icons.startsWith('http')}">
                                    <img src="${cate.icons}" width="50" height="50" style="object-fit: cover;">
                                </c:if>
                                <c:if test="${!cate.icons.startsWith('http')}">
                                    <img src="<c:url value='/image?fname=${cate.icons}'/>" width="50" height="50" alt="${cate.icons}">
                                </c:if>
                            </td>
                            <td>
                                <span class="badge badge-info">${cate.user.fullname}</span>
                            </td>
                            <td>
                                <a href="<c:url value='/admin/category/edit?id=${cate.cateId}'/>" class="btn btn-warning btn-sm">Sửa</a>
                                <a href="<c:url value='/admin/category/delete?id=${cate.cateId}'/>" 
                                   class="btn btn-danger btn-sm"
                                   onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>