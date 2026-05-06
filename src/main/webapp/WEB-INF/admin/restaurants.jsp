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
            <div class="card">
                <h3>Edit Restaurant Details</h3>
                <form method="post" action="${pageContext.request.contextPath}/admin/restaurants">
                    <input type="hidden" name="action" value="updateRestaurant">
                    <input type="hidden" name="restaurantId" value="${selectedRestaurant.id}">

                    <label>Name</label>
                    <input type="text" name="name" value="${selectedRestaurant.name}" required />

                    <label>City</label>
                    <input type="text" name="city" value="${selectedRestaurant.city}" required />

                    <label>State</label>
                    <input type="text" name="tate" value="${selectedRestaurant.state}" required />

                    <label>Description</label>
                    <textarea name="description" rows="3">${selectedRestaurant.description}"</textarea>

                    <label>How It Works</label>
                    <textarea name="howItWorks" rows="3">${selectedRestaurant.howItWorks}"</textarea>

                    <label>
                        <input type="checkbox" name="requireAllergenInfo"
                               <c:if test="${selectedRestuarant.requireAllergenInfo}">checked</c:if> />
                        Require Allergen Info
                    </label>

                    <button type="submit">Save Changes</button>

                </form>
            </div>

            <div class="card">
                <h3>Manage Administrators</h3>
                <p><strong>Current Admins</strong></p>
                <ul>
                    <c:forEach var="admin" items="${selectedRestaurant.administrators}">
                        <li>${admin.email}</li>
                    </c:forEach>
                </ul>

                <form method="post" action="${pageContext.request.contextPath}/admin/restaurants">
                    <input type="hidden" name="action" value="addAdmin">
                    <input type="hidden" name="restaurantId" value="${selectedRestaurant.id}">

                    <label>Add Admin (Email)</label>
                    <input type="email" name="adminEmail" required/>

                    <button type="submit">Add Admin</button>
                </form>

                <form method="post" action="${pageContext.request.contextPath}/admin/restaurants">
                    <input type="hidden" name="action" value="removeAdmin"/>
                    <input type="hidden" name="restaurantId" value="${selectedRestaurant.id}"/>

                    <label>Remove Admin</label>
                    <select name="adminId">
                        <c:forEach var="admin" items="${selectedRestaurant.administrators}">
                            <option value="${admin.id}">${admin.email}</option>
                        </c:forEach>
                    </select>
                    <button type="submit">Remove</button>
                </form>
            </div>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
