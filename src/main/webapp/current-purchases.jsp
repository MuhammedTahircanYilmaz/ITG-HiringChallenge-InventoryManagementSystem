<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Current Purchases</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        window.onload = function() {
            const token = localStorage.getItem('token');
            const role = localStorage.getItem('userRole');
            if (!token || role !== 'RETAILER') {
                window.location.href = 'login.jsp';
            }
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Current Purchases</h2>
        <div class="bills-list">
            <c:forEach var="bill" items="${bills}">
                <div class="bill-card">
                    <div class="bill-header">
                        <h3>Order #${bill.id}</h3>
                        <span class="status ${bill.status.toLowerCase()}">${bill.getStatus()}</span>
                    </div>
                    <div class="bill-details">
                        <p>Product: ${bill.getProductName()}</p>
                        <p>Quantity: ${bill.getQuantity()}</p>
                        <p>Total Price: $${bill.getTotalPrice()}</p>
                        <p>Order Date: ${bill.getOrderDate()}</p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>