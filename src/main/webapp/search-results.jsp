<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Search Results</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Search Results</h2>
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
                    <a href="app?action=GET_PRODUCT_BY_ID&productId=${product.getId()}"
                       class="view-button">View Details</a>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>