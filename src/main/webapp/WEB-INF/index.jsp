<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dyana - Home Page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
    <jsp:include page="/WEB-INF/components/header.jsp" />
    <section class="restaurant-info">
        <h1 class="restaurant-name"><strong>${restaurant.name}</strong></h1>
        <p class="restaurant-location">${restaurant.city}, ${restaurant.state}</p>
        <p class="restaurant-description">${restaurant.description}</p>
    </section>

    <!-- RESERVATION DATE & TIME SELECTOR -->
    <section class="reservation-bar">
        <input type="hidden" id="restaurantId" value="${restaurant.id}" />
        <input type="hidden" id="schedulingType" value="${schedulingType}" />
        <!-- DATE -->
        <form action="${pageContext.request.contextPath}/reservation" method="post">

            <input type="hidden" name="restaurantId" value="${restaurant.id}" />
            <input type="hidden" name="date" value="${selectedDate}" />

            <select id="dateSelect" name="date" required>
                <option value="">Select a Date</option>
                <c:choose>
                    <c:when test="${restaurant.schedulingType == 'DATE_ONLY'}">
                        <c:forEach var="day" items="${calendar}">
                            <c:if test="${day.available}">
                                <option value="${day.date}">
                                        ${day.date} (${day.totalSlots - day.bookedSlots} seats left)
                                </option>
                            </c:if>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="day" items="${calendar}">
                            <c:if test="${day.available}">
                                <option value="${day.date}">
                                        ${day.date}
                                </option>
                            </c:if>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </select>

            <select id="timeSelect" name="serviceInstanceId" required>
                <option value="">Select a Time</option>
            </select>

            <select name="partySize" required>
                <option value="">Party Size</option>
                <c:forEach begin="1" end="10" var="i">
                    <option value="${i}">
                        ${i}
                    </option>
                </c:forEach>
            </select>

            <button type="submit">Make a Reservation</button>

        </form>
    </section>

    <section class="mini-calendar">
        <h2>Availability Calendar</h2>
        <c:choose>
            <c:when test="${restaurant.schedulingType == 'DATE_ONLY'}">
                <c:forEach var="day" items="${calendar}">
                    <div class="day ${day.available ? 'available' : 'full'}">
                            ${day.date}
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <c:forEach var="day" items="${calendar}">
                    <div class="calendar-day ${day.available ? 'available' : 'full'}">
                        <div class="calendar-day-header">${day.date}</div>
                        <c:choose>
                            <c:when test="${!day.available}">
                                <div class="sold-out-message">Date Sold Out</div>
                            </c:when>
                            <c:otherwise>
                                <table class="slot-table">
                                    <tbody>
                                    <c:set var="currentHour" value="-1" />
                                    <c:forEach var="slot" items="${day.slots}">
                                    <c:if test="${slot.hour != currentHour}">
                                        <c:if test="${currentHour != -1}">
                                            </tr>
                                        </c:if>
                                        <tr>
                                        <c:set var="currentHour" value="${slot.hour}" />
                                        </c:if>
                                        <td class="slot-cell ${slot.full ? 'full' : 'available'}"
                                            title="${slot.remainingSeats} remaining">
                                                ${slot.serviceTimeFormatted}
                                        </td>
                                        </c:forEach>
                                        <c:if test="${currentHour != -1}">
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </section>
    <section class="how-it-works">
        <h2>How It Works</h2>
        <p>
            ${restaurant.howItWorks}
        </p>
    </section>
    <div class="container">
        <p>Questions? Contact us at <strong>${restaurant.email}</strong></p>
        <br>
        <c:if test="${not empty message}">
            <div style="color:red;"><strong>${message}</strong></div>
        </c:if>
        <c:if test="${not empty stackTrace}">
            <pre>${stackTrace}</pre>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
    <script>
        window.contextPath = '${pageContext.request.contextPath}';
    </script>

    <script src="<c:url value='/js/availability.js' />"></script>
</body>
</html>