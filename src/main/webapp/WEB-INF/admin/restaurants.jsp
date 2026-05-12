<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Restaurants | Dyana</title>
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
        <a href="${pageContext.request.contextPath}/admin/reservations">Manage Reservations</a>
        <a href="${pageContext.request.contextPath}/admin/services">Manage Services</a>
    </nav>
    <div class="container">
        <h2>Manage Restaurants</h2>
        <!-- SELECT RESTAURANT -->
        <div class="card">
            <h3>Select Restaurant</h3>
            <form method="get" action="${pageContext.request.contextPath}/admin/restaurants">
                <select name="restaurantId" onchange="this.form.submit()">
                    <option value="">-- Select Restaurant --</option>
                    <c:forEach var="restaurant" items="${restaurants}">
                        <option value="${restaurant.id}"
                        <c:if test="${restaurant.id == selectedRestaurantId}">
                            selected="selected"
                        </c:if>>
                        ${restaurant.name}
                        </option>
                    </c:forEach>
                </select>
            </form>
        </div>

        <c:if test="${not empty selectedRestaurantId}">
        <%-- SUCCESS MESSAGE --%>
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="message success-message">${sessionScope.successMessage}</div>
            <c:remove var="successMessage" scope="session" />
        </c:if>
        <%-- INFO MESSAGE --%>
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>
        <%-- ERROR MESSAGE --%>
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
            <c:remove var="error" scope="session" />
        </c:if>
            <div class="card">
                <h3>Edit Restaurant Details</h3>
                <form method="post" action="${pageContext.request.contextPath}/admin/restaurants">
                    <input type="hidden" name="action" value="updateRestaurant">
                    <input type="hidden" name="restaurantId" value="${selectedRestaurant.id}">

                    <div class="form-row">
                        <div>
                            <label>Name</label>
                            <input type="text" name="name" value="${selectedRestaurant.name}" required />
                        </div>
                        <div>
                            <label>City</label>
                            <input type="text" name="city" value="${selectedRestaurant.city}" required />
                        </div>
                        <div>
                            <label>State</label>
                            <input type="text" name="state" value="${selectedRestaurant.state}" required />
                        </div>
                    </div>

                    <label class="restaurant-form-label">Description</label>
                    <textarea name="description" rows="3">${selectedRestaurant.description}</textarea>

                    <label class="restaurant-form-label">How It Works</label>
                    <textarea name="howItWorks" rows="3">${selectedRestaurant.howItWorks}</textarea>

                    <label class="restaurant-form-label">
                        <input type="checkbox" name="requireAllergenInfo"
                               <c:if test="${selectedRestaurant.requireAllergenInfo}">checked</c:if> />
                        Require Allergen Info
                    </label>

                    <button type="submit">Save Changes</button>

                </form>
            </div>
            <c:if test="${sessionScope.role == 'SUPER_ADMIN'}">
            <div class="card">
                <h3>Manage Administrators</h3>
                <p class="current-admins">Current Admins</p>
                <ul>
                    <c:forEach var="admin" items="${selectedRestaurant.administrators}">
                        <li>Username: <strong>${admin.username}</strong> | Email: <span>${admin.email}</span></li>
                    </c:forEach>
                </ul>
                <br>
                <hr>
                <form method="post" action="${pageContext.request.contextPath}/admin/restaurants">
                    <input type="hidden" name="action" value="addAdmin">
                    <input type="hidden" name="restaurantId" value="${selectedRestaurant.id}">

                    <label class="add-admin">Add Admin</label>
                    <p class="form-hint">
                        Please provide an admin email to assign to
                        <strong>${selectedRestaurant.name}</strong>
                    </p>
                    <input type="email" name="adminEmail" required/>

                    <button type="submit">Add Admin</button>
                </form>
                <br>
                <hr>
                <form method="post" action="${pageContext.request.contextPath}/admin/restaurants">
                    <input type="hidden" name="action" value="removeAdmin"/>
                    <input type="hidden" name="restaurantId" value="${selectedRestaurant.id}"/>

                    <label class="remove-admin">Remove Admin</label>
                    <select name="adminId">
                        <c:forEach var="admin" items="${selectedRestaurant.administrators}">
                            <option value="${admin.id}">${admin.username} | ${admin.email}</option>
                        </c:forEach>
                    </select>
                    <button type="submit">Remove</button>
                </form>
            </div>
            </c:if>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
