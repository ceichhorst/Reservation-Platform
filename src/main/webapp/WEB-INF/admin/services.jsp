<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Services | Dyana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <link rel="stylesheet" href="<c:url value='/css/home.css' />">
    <link rel="stylesheet" href="<c:url value='/css/admin.css' />">
    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
    <jsp:include page="/WEB-INF/components/header.jsp" />
    <div class="container">
        <h2>Manage Services</h2>
        <!-- Select Restaurant -->
        <div class="card">
            <h3>Select Restaurant</h3>
            <form method="get" action="${pageContext.request.contextPath}/admin/services">
                <select name="restaurantId" onchange="this.form.submit()">
                    <option value="">-- Select Restaurant --</option>
                    <c:forEach var="restaurant" items="${restaurants}">
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
        <c:if test="${not empty selectedRestaurantId and restaurant.id == selectedRestaurantId}">
        <!-- Service Config -->
        <div class="card">
            <h3>Service Configuration</h3>
            <p><strong>Schedule Type:</strong> ${schedulingType}</p>
            <form method="post" action="${pageContext.request.contextPath}/admin/services">
                <input type="hidden" name="action" value="updateService"/>
                <input type="hidden" name="restaurantId" value="${selectedRestaurantId}"/>

                <select name="scheduleType">
                    <option value="DATE_ONLY" ${scheduleType == 'DATE_ONLY' ? 'selected' : ''}>DATE ONLY</option>
                    <option value="DATE_TIME" ${scheduleType == 'DATE_TIME' ? 'selected' : ''}>DATE & TIME</option>
                    <option value="FIXED_TIME_SLOTS" ${scheduleType == 'FIXED_TIME_SLOTS' ? 'selected' : ''}>FIXED TIME SLOTS</option>
                </select>
                <button type="submit">Update</button>
            </form>
        </div>
        <!-- Add Service -->
        <div class="card">
            <h3>Add Service Dates</h3>
            <form method="post" action="${pageContext.request.contextPath}/admin/services">
                <input type="hidden" name="action" value="addService"/>
                <input type="hidden" name="restaurantId" value="${selectedRestaurantId}"/>

                <c:choose>
                    <c:when test="${scheduleType == 'DATE_TIME'}">
                        <label>Date:</label>
                        <input type="date" name="date" required />

                        <label>Start Time:</label>
                        <input type="time" name="time" required />

                        <label>End Time:</label>
                        <input type="time" name="endTime" required />

                        <label>Capacity:</label>
                        <input type="number" name="capacity" required />

                    </c:when>
                    <c:otherwise>
                        <label>Date:</label>
                        <input type="date" name="date" required />

                        <label>Time:</label>
                        <input type="time" name="time" required />

                        <label>Capacity:</label>
                        <input type="number" name="capacity" required />
                    </c:otherwise>
                </c:choose>

                <button type="submit">Add Service</button>
            </form>
        </div>
        <!-- Service Table (Show seats booked in here potentially)-->
        <div class="card">
            <c:if test="${not empty sessionScope.error}">
                <div class="error">
                    ${sessionScope.error}
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>
            <h3>Existing Services</h3>
            <c:choose>
                <c:when test="${empty services}">
                    <p>No services found for this restaurant.</p>
                </c:when>
                <c:otherwise>
                    <form method="post" action="${pageContext.request.contextPath}/admin/services">
                    <table class="reservation-table">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Time</th>
                            <th>Capacity</th>
                            <th>Status</th>
                            <th>Select</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="service" items="${services}">
                            <tr>
                                <td>${service.serviceDate}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${scheduleType == 'DATE_TIME'}">
                                            ${service.serviceTime}
                                            -
                                            ${service.endTime}
                                        </c:when>
                                        <c:otherwise>
                                            ${service.serviceTime}
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    ${service.capacity}
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${service.visible == true}">
                                            Active
                                        </c:when>
                                        <c:otherwise>
                                            Hidden
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <input type="checkbox" name="serviceIds" value="${service.id}" />
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <input type="hidden" name="restaurantId" value="${selectedRestaurantId}"/>
                    <button type="submit" name="action" value="bulkToggleVisibility">
                        Toggle Visibility
                    </button>
                    <button type="submit" name="action" value="bulkDelete">
                        Delete
                    </button>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>

