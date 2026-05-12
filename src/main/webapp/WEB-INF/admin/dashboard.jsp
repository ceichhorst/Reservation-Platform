<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
    <link rel="stylesheet" href="<c:url value='/css/admin.css' />">
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
        <h2>Admin Dashboard</h2>
        <c:if test="${not empty message}">
            <c:choose>
                <c:when test="${message == 'No restaurants assigned.'}">
                    <div class="message">
                        Your account has not been assigned a restaurant yet.
                        Please contact your head administrator to get access.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="message">${message}</div>
                </c:otherwise>
            </c:choose>
        </c:if>
        <div class="card">
            <h3>Select Restaurant</h3>
            <form method="get" action="${pageContext.request.contextPath}/admin/dashboard">
                <select name="restaurantId" onchange="this.form.submit()">
                <option value="">-- Select Restaurant --</option>
                    <c:forEach var="restaurant" items="${restaurantList}">
                        <option value="${restaurant.id}"
                        <c:if test="${restaurant.id == selectedRestaurantId}">
                            selected="selected"
                        </c:if>
                        >
                        ${restaurant.name}
                        </option>
                    </c:forEach>
                </select>
            </form>
        </div>
        <c:if test="${not empty selectedRestaurantId}">
        <!-- Summary Card -->
        <div class="dashboard-cards">
            <div class="dashboard-card">
                <div class="card-label">Active Upcoming Reservations</div>
                <div class="card-value">${reservationCount}</div>
            </div>
        </div>
        <h2>Upcoming Overview</h2>
        <c:choose>
            <c:when test="${empty reservationStats}">
                <p>No upcoming reservations found.</p>
            </c:when>
            <c:otherwise>
                <c:set var="hasTimeSlots" value="false" />
                <c:forEach var="r" items="${restaurantList}">
                    <c:if test="${r.schedulingType == 'DATE_TIME' || r.schedulingType == 'FIXED_TIME_SLOTS'}">
                        <c:set var="hasTimeSlots" value="true" />
                    </c:if>
                </c:forEach>
                <table class="dashboard-table">
                    <thead>
                    <tr>
                        <th>Service Date</th>
                        <th>Total Reservations Confirmed</th>
                        <th>Total Seats Booked</th>
                        <c:if test="${hasTimeSlots}">
                            <th></th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="stat" items="${reservationStats}" varStatus="loop">
                        <tr class="stat-row">
                            <td>${stat.serviceDate}</td>
                            <td>${stat.reservationCount}</td>
                            <td>${stat.totalSeatsBooked}</td>
                            <c:if test="${hasTimeSlots}">
                                <td>
                                    <button class="accordion-toggle"
                                            onclick="toggleSlots('slots-${loop.index}', this)"
                                            aria-expanded="false">
                                        <span class="arrow-icon">▶</span>
                                    </button>
                                </td>
                            </c:if>
                        </tr>
                        <%-- Slot breakdown row --%>
                        <c:if test="${hasTimeSlots}">
                            <tr id="slots-${loop.index}" class="slot-breakdown" style="display:none;">
                                <td colspan="4">
                                    <table class="slot-detail-table">
                                        <thead>
                                           <tr>
                                               <th>Time</th>
                                               <th>Reservations</th>
                                               <th>Seats Booked</th>
                                           </tr>
                                        </thead>
                                        <tbody>
                                            <c:set var="hasSlots" value="false" />
                                            <c:forEach var="slot" items="${timeSlotStats}">
                                                <c:if test="${slot.serviceDate == stat.serviceDate}">
                                                    <c:set var="hasSlots" value="true" />
                                                    <tr>
                                                        <td>${slot.serviceTimeFormatted}</td>
                                                        <td>${slot.reservationCount}</td>
                                                        <td>${slot.totalSeatsBooked}</td>
                                                    </tr>
                                                </c:if>
                                            </c:forEach>
                                        <c:if test="${!hasSlots}">
                                            <tr>
                                                <td colspan="3">No slot data available</td>
                                            </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
    <script src="<c:url value='/js/timeSlots.js' />"></script>
</body>
</html>
