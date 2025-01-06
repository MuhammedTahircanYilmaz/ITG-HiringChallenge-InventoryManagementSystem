<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Product Details</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <div class="product-details">
            <img src="app?action=GET_IMAGE_BY_PRODUCT_ID&productId=${product.getId()}" alt="${product.getName()}" class="product-image">
            <h2>${product.name}</h2>
            <p>${product.description}</p>
            <p>Price: $${product.price}</p>
            <p>Stock: ${product.stockQuantity}</p>
            <p>Discount: ${product.discount}%</p>
            <p>Supplier: ${product.}

            <% if (session.getAttribute("userRole").equals("RETAILER")) { %>
                <form action="app" method="post">
                    <input type="hidden" name="action" value="CREATE_BILL">
                    <input type="hidden" name="productId" value="${product.getId()}">
                    <input type="hidden" name="retailerId" value="${sessionScope.userId}">
                    <div class="form-group">
                        <label>Quantity:</label>
                        <input type="number" name="quantity" min="1" max="${product.getStockQuantity()}" required>
                    </div>
                    <button type="submit">Purchase</button>
                </form>
            <% } %>

            <% if (session.getAttribute("userRole").equals("SUPPLIER") &&
                  session.getAttribute("userId").equals(product.getSupplierId())) { %>
                <form action="app" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="UPDATE_PRODUCT">
                    <input type="hidden" name="productId" value="${product.getId()}">
                    <div class="form-group">
                        <label>Product Name:</label>
                        <input type="text" name="name" value="${product.getName()}" required>
                    </div>
                    <div class="form-group">
                        <label>Description:</label>
                        <textarea name="description" required>${product.getDescription()}</textarea>
                    </div>
                    <div class="form-group">
                        <label>Price:</label>
                        <input type="number" step="0.01" name="price" required>
            </div>
            <div class="form-group">
                <label>Stock Quantity:</label>
                <input type="number" name="stockQuantity" required>
            </div>
            <div class="form-group">
                <label>Discount (%):</label>
                <input type="number" name="discount" min="0" max="100" value="0">
            </div>
            <div class="form-group">
                <label>Product Image:</label>
                <input type="file" name="productImage" accept="image/*" required>
                <input type="hidden" name="action" value="ADD_IMAGE">
            </div>
            <button type="submit">Create Product</button>
        </form>
    </div>
</body>
</html" value="${product.price}" required>
                    </div>
                    <div class="form-group">
                        <label>Stock Quantity:</label>
                        <input type="number" name="stockQuantity" value="${product.getStockQuantity()}" required>
                    </div>
                    <div class="form-group">
                        <label>Discount (%):</label>
                        <input type="number" name="discount" value="${product.getDiscount()}" min="0" max="100">
                    </div>
                    <div class="form-group">
                        <label>New Image:</label>
                        <input type="file" name="productImage" accept="image/*">
                        <input type="hidden" name="action" value="ADD_IMAGE">
                    </div>
                    <button type="submit">Update Product</button>
                </form>
            <% } %>
        </div>
    </div>
</body>
</html>