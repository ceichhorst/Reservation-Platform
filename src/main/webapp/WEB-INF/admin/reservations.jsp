<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Reservations</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="stylesheet" href="<c:url value='/css/admin.css' />">
    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
    <jsp:include page="/WEB-INF/components/header.jsp" />
    <nav>
        <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/restaurants">Manage Restaurants</a>
        <a href="${pageContext.request.contextPath}/admin/services">Manage Services</a>
    </nav>
    <div class="container">
        <h2>Reservations</h2>
        <!-- INFO MESSAGE -->
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>
        <!-- ERROR MESSAGE -->
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <form method="get" action="${pageContext.request.contextPath}/admin/reservations" class="filter-form">
            <input
                type="text"
                name="id"
                placeholder="Confirmation ID"
                value="${filterId}"
            />
            <input
                type="text"
                name="customerName"
                placeholder="Customer Name"
                value="${filterCustomerName}"
            />
            <input
                type="text"
                name="email"
                placeholder="Customer Email"
                value="${filterEmail}"
            />
            <input
                type="date"
                name="serviceDate"
                value="${filterDate}"
            />
            <button type="submit">Filter</button>
            <a href="${pageContext.request.contextPath}/admin/reservations">Clear</a>
        </form>
        <div class="scroll-container">
        <c:choose>
            <c:when test="${empty reservations}">
                <p>No reservations found.</p>
            </c:when>
            <c:otherwise>
                <table class="reservation-table">
                    <thead>
                        <tr>
                            <th>Confirmation ID</th>
                            <th>Customer Name</th>
                            <th>Email</th>
                            <th>Service Date</th>
                            <th>Service Time</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="reservation" items="${reservations}">
                            <tr>
                                <td>${reservation.id}</td>
                                <td>${reservation.customerName}</td>
                                <td>${reservation.email}</td>
                                <td>${reservation.serviceInstance.serviceDate}</td>
                                <td>${reservation.serviceInstance.serviceTimeFormatted}</td>
                                <td>
                                    <span class="status ${reservation.status}">
                                        ${reservation.status}
                                    </span>
                                </td>
                                <td>
                                    <c:if test="${reservation.status != 'CONFIRMED'}">
                                        <form method="post" style="display:inline;">
                                            <input type="hidden" name="id" value="${reservation.id}" />
                                            <input type="hidden" name="action" value="confirm" />
                                            <button type="submit">Confirm</button>
                                        </form>
                                    </c:if>
                                    <c:if test="${reservation.status != 'CANCELLED'}">
                                        <form method="post" style="display:inline;">
                                            <input type="hidden" name="id" value="${reservation.id}" />
                                            <input type="hidden" name="action" value="cancel" />
                                            <button type="submit">Cancel</button>
                                        </form>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
        </div>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
