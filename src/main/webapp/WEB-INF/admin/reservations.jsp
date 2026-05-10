<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Manage Reservations</title>
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
        <a href="${pageContext.request.contextPath}/admin/restaurants">Manage Restaurants</a>
        <a href="${pageContext.request.contextPath}/admin/services">Manage Services</a>
    </nav>
    <div class="reservation-table-container">
        <h2>Reservations</h2>
        <%-- SUCCESS MESSAGE --%>
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="message success-message">${sessionScope.successMessage}</div>
            <c:remove var="successMessage" scope="session" />
        </c:if>
        <!-- INFO MESSAGE -->
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>
        <!-- ERROR MESSAGE -->
        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>
        <form method="get" action="${pageContext.request.contextPath}/admin/reservations" class="filter-form">
            <input
                type="text"
                name="id"
                placeholder="Confirmation ID"
                value="${filterId}"
            />
            <input
                type="text"
                name="customerName"
                placeholder="Customer Name"
                value="${filterCustomerName}"
            />
            <input
                type="text"
                name="email"
                placeholder="Customer Email"
                value="${filterEmail}"
            />
            <input
                type="date"
                name="serviceDate"
                value="${filterDate}"
            />
            <button type="submit">Filter</button>
            <a href="${pageContext.request.contextPath}/admin/reservations">Clear</a>
        </form>
        <div class="scroll-container">
        <c:choose>
            <c:when test="${empty reservations}">
                <p>No reservations found.</p>
            </c:when>
            <c:otherwise>
                <table class="reservation-table">
                    <thead>
                        <tr>
                            <th>Confirmation ID</th>
                            <th>Customer Name</th>
                            <th>Email</th>
                            <th>Service Date</th>
                            <th>Service Time</th>
                            <th>Party Size</th>
                            <th>Allergen Info</th>
                            <th>Comments</th>
                            <th>Last Handled By</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="reservation" items="${reservations}">
                            <tr>
                                <td>${reservation.id}</td>
                                <td>${reservation.customerName}</td>
                                <td>${reservation.email}</td>
                                <td>${reservation.serviceInstance.serviceDate}</td>
                                <td>${reservation.serviceInstance.serviceTimeFormatted}</td>
                                <td>${reservation.partySize}</td>
                                <td>${not empty reservation.allergenInfo ? reservation.allergenInfo : '-'}</td>
                                <td>${not empty reservation.additionalComments ? reservation.additionalComments : '-'}</td>
                                <td>${not empty reservation.handledByAdminId ? reservation.handledByAdminId : '-'}</td>
                                <td>
                                    <span class="status ${reservation.status}">
                                        ${reservation.status}
                                    </span>
                                </td>
                                <td>
                                    <c:if test="${reservation.status != 'CONFIRMED'}">
                                        <form method="post" style="display:inline;">
                                            <input type="hidden" name="id" value="${reservation.id}" />
                                            <input type="hidden" name="action" value="confirm" />
                                            <button type="submit">Confirm</button>
                                        </form>
                                    </c:if>
                                    <c:if test="${reservation.status != 'CANCELLED'}">
                                        <form method="post" style="display:inline;">
                                            <input type="hidden" name="id" value="${reservation.id}" />
                                            <input type="hidden" name="action" value="cancel" />
                                            <button type="submit">Cancel</button>
                                        </form>
                                    </c:if>
                                    <%-- Edit button --%>
                                    <button type="button"
                                            class="edit-toggle-btn"
                                            onclick="toggleEditForm(${reservation.id}, this)">
                                        Edit
                                    </button>
                                </td>
                            </tr>
                            <%-- Inline Edit Form --%>
                            <tr id="edit-row-${reservation.id}" class="edit-form-row" style="display:none;">
                                <td colspan="11">
                                    <form method="post" class="edit-form">
                                        <input type="hidden" name="id" value="${reservation.id}" />
                                        <input type="hidden" name="action" value="edit" />
                                        <div class="edit-form-grid">
                                            <label>Customer Name
                                                <input type="text" name="customerName"
                                                       value="${reservation.customerName}" required />
                                            </label>
                                            <label>Email
                                                <input type="email" name="email"
                                                       value="${reservation.email}" required />
                                            </label>
                                            <label>Party Size
                                                <input type="number" name="partySize" min="1" max="10"
                                                       value="${reservation.partySize}" required />
                                            </label>
                                            <label>Allergen Info
                                                <textarea name="allergenInfo">${reservation.allergenInfo}</textarea>
                                            </label>
                                            <label>Additional Comments
                                                <textarea name="additionalComments">${reservation.additionalComments}</textarea>
                                            </label>
                                        </div>
                                        <button type="submit">Save Changes</button>
                                        <button type="button"
                                                onclick="toggleEditForm(${reservation.id}, null)">
                                            Cancel
                                        </button>
                                    </form>
                                </td>
                            </tr>

                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
        </div>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
    <script src="<c:url value='/js/editToggle.js' />"></script>
</body>
</html>
