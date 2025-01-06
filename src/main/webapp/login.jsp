<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        function handleLogin(event) {
            event.preventDefault();

            const form = event.target;
            const formData = {
                email: form.email.value,
                password: form.password.value,
                roleName: form.userType.value
            };

            fetch('login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: new URLSearchParams(formData)
            })
            .then(response => response.json())
            .then(data => {
                if (data.error) {
                    document.getElementById('error-message').textContent = data.error;
                } else {
                    // Store the token and user info
                    localStorage.setItem('token', data.token);
                    localStorage.setItem('userId', data.userId);
                    localStorage.setItem('userRole', data.roleName);

                    // Redirect based on role
                    switch(data.roleName) {
                        case 'RETAILER':
                            window.location.href = 'retailer-main.jsp';
                            break;
                        case 'SUPPLIER':
                            window.location.href = 'supplier-main.jsp';
                            break;
                        case 'ADMIN':
                            window.location.href = 'admin-main.jsp';
                            break;
                    }
                }
            })
            .catch(error => {
                document.getElementById('error-message').textContent = 'An error occurred during login';
            });
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Login</h2>
        <div id="error-message" class="error-message"></div>
        <div class="login-options">
            <form onsubmit="handleLogin(event)">
                <div class="form-group">
                    <label>Login as:</label>
                    <select name="roleName" required>
                        <option value="RETAILER">Retailer</option>
                        <option value="SUPPLIER">Supplier</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" required>
                </div>
                <div class="form-group">
                    <label>Password:</label>
                    <input type="password" name="password" required>
                </div>
                <button type="submit">Login</button>
            </form>
        </div>
    </div>
</body>
</html>
