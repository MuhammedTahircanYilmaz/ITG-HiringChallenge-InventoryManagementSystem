<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Supplier Dashboard</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        // Check authentication on page load
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
    <nav class="navbar">
        <div class="nav-links">
            <a href="app?action=GET_SUPPLIER_BY_ID&id=${sessionScope.userId}">Profile</a>
            <a href="create-product.jsp">Create Product</a>
            <a href="app?action=GET_PRODUCTS_BY_SUPPLIER&supplierId=${sessionScope.userId}">My Products</a>
            <a href="app?action=GET_BILLS_BY_SUPPLIER&status=COMPLETED">Past Sales</a>
            <a href="app?action=GET_BILLS_BY_SUPPLIER&status=PENDING">Pending Bills</a>
            <a href="#" onclick="localStorage.clear(); window.location.href='login.jsp'">Logout</a>
        </div>
    </nav>
    <div class="container">
        <!-- Main content will be populated by servlets -->
    </div>
</body>
</html>