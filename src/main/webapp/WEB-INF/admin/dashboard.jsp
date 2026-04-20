<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | Dyana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
    <jsp:include page="/WEB-INF/components/header.jsp" />
    <nav>
        <a href="${pageContext.request.contextPath}/admin/reservations">Manage Reservations</a>
        <a href="${pageContext.request.contextPath}/admin/services">Manage Services</a>
    </nav>
    <div class="container">
        <h2>Overview</h2>
        <div class="dashboard-cards">
            <div class="card">
                <h3>Upcoming Services</h3>
                <ul>
                    <c:forEach var="service" items="${services}">
                        <li>${service.serviceDate}</li>
                    </c:forEach>
                </ul>
            </div>
            <div class="card">
                <h3>Total Reservations (Per Service)</h3>
                <ul>
                    <c:forEach var="reservation" items="${reservations}">
                        <li>${reservation.Reservation}</li>
                    </c:forEach>
                </ul>
            </div>
            <hr>
            <h2>Quick Actions</h2>
            <div class="cta-buttons">
                <a href="${pageContext.request.contextPath}/admin/reservations">View Reservations</a>
            </div>
            <hr>
            <h2>System Messages</h2>
            <!-- Info Message -->
            <c:if test="${not empty message}">
                <div class="message">
                    ${message}
                </div>
            </c:if>
            <!-- Error Message -->
            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>

        </div>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
