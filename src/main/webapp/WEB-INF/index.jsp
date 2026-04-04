<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <h1 class="restaurant-name"><strong>Diane's Delicious Diner</strong></h1>
        <p class="restaurant-location">Madison, Wisconsin</p>
        <p class="restaurant-description">
            Diane's Delicious Diner is a simulated restaurant environment where Madison College students develop and
            apply their culinary skills.
        </p>
    </section>

    <section class="reservation-bar">
        <form class="reservation-form" action="${pageContext.request.contextPath}/reservation" method="post">
            <input type="date" name="date" required>
            <select name="partySize" required>
                <option value="">Party Size</option>
                <c:forEach begin="1" end="10" var="i">
                    <option value="${i}">${i}</option>
                </c:forEach>
            </select>
            <select name="time" class="time-select">
                <option value="">Any Time</option>
                <!-- dynamically populate -->
            </select>
            <button type="submit">Make a Reservation</button>
        </form>
    </section>
    <section class="upcoming-dates">
        <h2>Upcoming Service Dates</h2>
        <ul>
            <c:forEach var="service" items="${services}">
                <li>${service.serviceDate}</li>
            </c:forEach>
        </ul>
    </section>
    <section class="mini-calendar">
        <h2>Availability Calendar</h2>
        <div class="calendar-grid">
            <!-- static placeholder for now -->
            <div class="day disabled">1</div>
            <div class="day disabled">2</div>
            <div class="day available">3</div>
            <div class="day full">4</div>
            <div class="day available">5</div>
        </div>
    </section>
    <section class="how-it-works">
        <h2>How It Works</h2>
        <p>
            Reservations are accepted on select days throughout the season.
            Choose a date above to see real-time availability.
            Time options may vary depending on the service schedule set by the restaurant.
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
            <c:forEach var="restaurant" items="${restaurants}">
                <tr>
                    <td>${restaurant.id}</td>
                    <td>${restaurant.name}</td>
                    <td>${restaurant.schedulingType}</td>
                </tr>
            </c:forEach>
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