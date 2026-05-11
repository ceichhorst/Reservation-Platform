<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Unauthorized Access - Dyana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <link rel="stylesheet" href="<c:url value='/css/error.css' />">
    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
    <jsp:include page="/WEB-INF/components/header.jsp" />
    <div class="error-page">
        <div class="error-code">403</div>
        <h1 class="error-title">Access Denied</h1>
        <p class="error-message">
            You don't have permission to access this page.
            If you believe this is a mistake, please contact your head administrator.
        </p>
        <a href="${pageContext.request.contextPath}/r/${restaurant.id}" class="error-btn">Back to Home</a>
        <c:if test="${not empty sessionScope.userEmail}">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="error-btn-secondary">
                Go To Dashboard
            </a>
        </c:if>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
