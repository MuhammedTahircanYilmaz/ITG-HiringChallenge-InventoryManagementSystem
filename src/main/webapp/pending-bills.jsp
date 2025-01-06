<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pending Bills</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <script>
        window.onload = function() {
            const token = localStorage.getItem('token');
            const role = localStorage.getItem('userRole');
            if (!token || role !== 'SUPPLIER') {
                window.location.href = 'login.jsp';
            }
        }

        function updateBillStatus(billId, status) {
            const form = document.createElement('form');
            form.method = 'post';
            form.action = 'app';

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'UPDATE_BILL_STATUS';

            const billIdInput = document.createElement('input');
            billIdInput.type = 'hidden';
            billIdInput.name = 'billId';
            billIdInput.value = billId;

            const statusInput = document.createElement('input');
            statusInput.type = 'hidden';
            statusInput.name = 'status';
            statusInput.value = status;

            form.appendChild(actionInput);
            form.appendChild(billIdInput);
            form.appendChild(statusInput);

            document.body.appendChild(form);
            form.submit();
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Pending Bills</h2>
        <div class="bills-list">
            <c:forEach var="bill" items="${bills}">
                <div class="bill-card">
                    <div class="bill-header">
                        <h3>Order #${bill.id}</h3>
                        <span class="status pending">Pending</span>
                    </div>
                    <div class="bill-details">
                        <p>Retailer: ${bill.getRetailerName()}</p>
                        <p>Product: ${bill.getProductName()}</p>
                        <p>Quantity: ${bill.getQuantity()}</p>
                        <p>Total Amount: $${bill.getTotalAmount()}</p>
                        <p>Order Date: ${bill.getDate()}</p>
                    </div>
                    <div class="bill-actions">
                        <button onclick="updateBillStatus(${bill.getId()}, 'COMPLETED')"
                                class="accept-button">Accept</button>
                        <button onclick="updateBillStatus(${bill.getId()}, 'REJECTED')"
                                class="reject-button">Reject</button>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html>