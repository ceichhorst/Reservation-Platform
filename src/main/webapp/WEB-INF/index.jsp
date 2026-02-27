<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reservation Platform</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/css/styles.css' />">
</head>
<body>
    <header>
        <h1>Enterprise Reservation Platform</h1>
        <p>Scalable. Reliable. Built for Real-Time Availability. [FILLER]</p>
        <p style="color:red;">DATA VERSION</p>
    </header>
    <nav>
        <a href="#">Home</a>
        <a href="#">Make Reservation</a>
        <a href="#">Admin Login</a>
    </nav>
    <div class="container">
        <h2>Welcome</h2>
        <p>
            Our platform provides real-time reservation management designed to prevent overbooking and ensure a seamless
            experience for both customers and administrators. Built using enterprise-grade Java technologies, the
            system guarantees data integrity, secure authentication, and scalable performance under high demand.
        </p>

        <h2>Key Features</h2>
        <ul>
            <li>Real-time availability enforcement</li>
            <li>Secure authentication and role-based access</li>
            <li>Centralized reservation management</li>
            <li>Concurrency-safe booking process</li>
            <li>Cloud-hosted infrastructure</li>
        </ul>

        <div class="cta-buttons">
            <a href="#">Make a Reservation</a>
            <a href="#">Administrator Access</a>
        </div>

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
<footer>
    &copy; 2026 Reservation Platform | Enterprise Java Project
</footer>
</body>
</html>