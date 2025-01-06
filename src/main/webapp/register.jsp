<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Register</h2>
        <div class="registration-options">
            <div class="option">
                <h3>Register as Retailer</h3>
                <form action="app" method="post">
                    <input type="hidden" name="action" value="CREATE_RETAILER">
                    <div class="form-group">
                        <label>Company Name:</label>
                        <input type="text" name="name" required>
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input type="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label>Password:</label>
                        <input type="password" name="password" required>
                    </div>
                    <button type="submit">Register</button>
                </form>
            </div>

            <div class="option">
                <h3>Register as Supplier</h3>
                <form action="app" method="post">
                    <input type="hidden" name="action" value="CREATE_SUPPLIER">
                    <div class="form-group">
                        <label>Company Name:</label>
                        <input type="text" name="name" required>
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input type="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label>Password:</label>
                        <input type="password" name="password" required>
                    </div>
                    <button type="submit">Register</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>