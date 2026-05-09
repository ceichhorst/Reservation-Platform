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
        <a href="${pageContext.request.contextPath}/admin/restaurants">Manage Restaurants</a>
        <a href="${pageContext.request.contextPath}/admin/reservations">Manage Reservations</a>
        <a href="${pageContext.request.contextPath}/admin/services">Manage Services</a>
    </nav>
    <div class="dashboard-welcome">
        <h2>Welcome, ${not empty username ? username : userEmail}!</h2>
        <p class="welcome-sub">Logged in as ${userEmail}</p>
    </div>
    <div class="container">
        <!-- Add admin profile section or simple 'Welcome $ { adminUser }' message -->
        <h2>Overview</h2>
        <!-- Don't need time listed on this table -->
        <table class="dashboard-table">
            <thead>
                <tr>
                    <th>Service Date</th>
                    <th>Total Reservations Confirmed</th>
                    <th>Total Seats Booked</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="stat" items="${reservationStats}">
                    <tr>
                        <td>${stat.serviceDate}</td>
                        <td>${stat.reservationCount}</td>
                        <td>${stat.totalSeatsBooked}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="dashboard-cards">
            <!-- <hr>
            <h2>Quick Actions</h2>
            <div class="cta-buttons">
                <a href="${pageContext.request.contextPath}/admin/reservations">View Reservations</a>
            </div>
            <hr>
            <h2>System Messages</h2>

            <c:if test="${not empty message}">
                <div class="message">
                    ${message}
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="error">
                        ${error}
                </div>
            </c:if>
            -->
        </div>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
