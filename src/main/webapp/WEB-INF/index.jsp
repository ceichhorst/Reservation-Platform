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
        <!-- DATE -->
        <form action="${pageContext.request.contextPath}/reservation" method="get">

            <input type="hidden" name="restaurantId" value="${restaurant.id}" />

            <select name="date" onchange="this.form.submit()" required>
                <option value="">Select a Date</option>

                <c:choose>
                    <c:when test="${restaurant.schedulingType == 'DATE_ONLY'}">
                        <c:forEach var="day" items="${calendar}">
                            <option value="${day.date}"
                                    <c:if test="${day.date == selectedDate}">selected</c:if>>
                                    ${day.date} (${day.totalSlots - day.bookedSlots} seats left)
                            </option>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="day" items="${calendar}">
                            <option value="${day.date}"
                                    <c:if test="${day.date == selectedDate}">selected</c:if>>
                                    ${day.date}
                            </option>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </select>

        </form>
        <!-- TIME -->
        <form action="${pageContext.request.contextPath}/reservation" method="post">

            <input type="hidden" name="restaurantId" value="${restaurant.id}" />
            <input type="hidden" name="date" value="${selectedDate}" />

            <c:choose>
                <c:when test="${restaurant.schedulingType == 'DATE_ONLY'}">
                    <c:if test="${not empty availableTimes}">
                        <div class="fixed-slot">
                                ${availableTimes[0].serviceTime} (assigned automatically)
                        </div>
                        <input type="hidden"
                               name="serviceInstanceId"
                               value="${availableTimes[0].id}" />
                    </c:if>
                </c:when>

                <c:otherwise>
                    <select name="serviceInstanceId" required>
                        <option value="">Select a Time</option>
                        <c:forEach var="slot" items="${availableTimes}">
                            <option value="${slot.id}">
                                ${slot.serviceTime.format(DateTimeFormatter.ofPattern("h:mm a"))}
                            </option>
                        </c:forEach>
                    </select>
                </c:otherwise>
            </c:choose>

            <!-- PARTY SIZE -->
            <select name="partySize" required>
                <option value="">Party Size</option>
                <c:forEach begin="1" end="10" var="i">
                    <option value="${i}">${i}</option>
                </c:forEach>
            </select>

            <button type="submit">Make a Reservation</button>

        </form>
    </section>
    <!--
    <section class="upcoming-dates">
        <h2>Upcoming Service Dates</h2>
        <ul>
            <c:forEach var="service" items="${services}">
                <li>${service.serviceDate}</li>
            </c:forEach>
        </ul>
    </section>
    -->
    <section class="mini-calendar">
        <h2>Availability Calendar</h2>
        <c:forEach var="day" items="${calendar}">
            <div class="day ${day.available ? 'available' : 'full'}">
                ${day.date}
            </div>
        </c:forEach>
    </section>
    <section class="how-it-works">
        <h2>How It Works</h2>
        <p>
            ${restaurant.howItWorks};
        </p>
    </section>
    <div class="container">
        <hr>
        <h2>Available Restaurants</h2>

        <table border="1">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Scheduling Type</th>
            </tr>
            <tr>
                <td>${restaurant.id}</td>
                <td>${restaurant.name}</td>
                <td>${restaurant.schedulingType}</td>
            </tr>
        </table>
        <br>
        <h2>Upcoming Service Instances</h2>
        <table border="1">
            <tr>
                <th>Date</th>
                <th>Time</th>
                <th>Capacity</th>
            </tr>
            <c:forEach var="service" items="${services}">
                <tr>
                    <td>${service.serviceDate}</td>
                    <td>${service.serviceTime}</td>
                    <td>${service.capacity}</td>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${not empty message}">
            <div style="color:red;"><strong>${message}</strong></div>
        </c:if>
        <c:if test="${not empty stackTrace}">
            <pre>${stackTrace}</pre>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>