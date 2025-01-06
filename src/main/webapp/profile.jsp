
<!-- profile.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Profile Settings</h2>
        <div class="profile-section">
            <h3>Update Company Name</h3>
            <form action="app" method="post">
                <input type="hidden" name="action" value="${sessionScope.userRole == 'RETAILER' ? 'UPDATE_RETAILER' : 'UPDATE_SUPPLIER'}">
                <input type="hidden" name="id" value="${sessionScope.userId}">
                <div class="form-group">
                    <label>New Company Name:</label>
                    <input type="text" name="companyName" required>
                </div>
                <button type="submit">Update Name</button>
            </form>
        </div>

        <div class="profile-section">
            <h3>Change Password</h3>
            <form action="app" method="post">
                <input type="hidden" name="action" value="${sessionScope.userRole == 'RETAILER' ? 'UPDATE_RETAILER' : 'UPDATE_SUPPLIER'}">
                <input type="hidden" name="id" value="${sessionScope.userId}">
                <div class="form-group">
                    <label>Current Password:</label>
                    <input type="password" name="currentPassword" required>
                </div>
                <div class="form-group">
                    <label>New Password:</label>
                    <input type="password" name="newPassword" required>
                </div>
                <button type="submit">Change Password</button>
            </form>
        </div>
    </div>
</body>
</html>