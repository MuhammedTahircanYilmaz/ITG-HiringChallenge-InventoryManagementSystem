<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Products</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        window.onload = function() {
            const token = localStorage.getItem('token');
            const role = localStorage.getItem('userRole');
            if (!token || role !== 'SUPPLIER') {
                window.location.href = 'login.jsp';
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>My Products</h2>
        <a href="create-product.jsp" class="add-button">Add New Product</a>
        <div class="products-grid">
            <c:forEach var="product" items="${products}">
                <div class="product-card">
                    <img src="app?action=GET_IMAGE_BY_PRODUCT_ID&productId=${product.getId()}"
                         alt="${product.getName()}" class="product-image">
                    <h3>${product.getName()}</h3>
                    <p class="price">$${product.getPrice()}</p>
                    <p class="stock">Stock: ${product.getStockQuantity()}</p>
                    <c:if test="${product.getDiscount() > 0}">
                        <p class="discount">${product.getDiscount()}% OFF!</p>
                    </c:if>
                    <div class="button-group">
                        <a href="app?action=GET_PRODUCT_BY_ID&productId=${product.getId()}"
                           class="edit-button">Edit</a>
                        <form action="app" method="post" class="delete-form">
                            <input type="hidden" name="action" value="DELETE_PRODUCT">
                            <input type="hidden" name="productId" value="${product.getId()}">
                            <button type="submit" class="delete-button">Delete</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>