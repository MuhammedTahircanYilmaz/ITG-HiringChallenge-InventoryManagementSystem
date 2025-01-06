<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Retailer Dashboard</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        // Check authentication on page load
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
    <nav class="navbar">
        <div class="search-container">
            <form action="app" method="get">
                <input type="hidden" name="action" value="GET_ALL_PRODUCTS">
                <input type="text" name="query" placeholder="Search products...">
                <button type="submit">Search</button>
            </form>
        </div>
        <div class="nav-links">
            <a href="app?action=GET_RETAILER_BY_ID&id=${sessionScope.userId}">Profile</a>
            <a href="app?action=GET_BILLS_BY_RETAILER&status=PENDING">Current Purchases</a>
            <a href="app?action=GET_BILLS_BY_RETAILER&status=COMPLETED">Past Purchases</a>
            <a href="#" onclick="localStorage.clear(); window.location.href='login.jsp'">Logout</a>
        </div>
    </nav>
    <div class="container">
    </div>
</body>
</html>