<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Create New Product</title>
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
    <div class="container">
        <h2>Create New Product</h2>
        <form action="app" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="CREATE_PRODUCT">
            <input type="hidden" name="supplierId" value="${sessionScope.userId}">
            <div class="form-group">
                <label>Product Name:</label>
                <input type="text" name="name" required>
            </div>
            <div class="form-group">
                <label>Description:</label>
                <textarea name="description" required></textarea>
            </div>
            <div class="form-group">
                <label>Price:</label>
                <input type="number" step="0.01" name="price" required>
            </div>
            <div class="form-group">
                <label>Stock Quantity:</label>
                <input type="number" name="stockQuantity" required min="0">
            </div>
            <div class="form-group">
                <label>Discount (%):</label>
                <input type="number" name="discount" min="0" max="100" value="0">
            </div>
            <div class="form-group">
                <label>Product Image:</label>
                <input type="file" name="productImage" accept="image/*" >
            </div>
            <button type="submit">Create Product</button>
        </form>
    </div>
</body>
</html>