<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Server Error - Dyana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <link rel="stylesheet" href="<c:url value='/css/error.css' />">
    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
<jsp:include page="/WEB-INF/components/header.jsp" />
<div class="error-page">
    <div class="error-code">500</div>
    <h1 class="error-title">Something Went Wrong</h1>
    <p class="error-message">
        An unexpected error occurred. Please try again or contact support if the error persists.
    </p>
    <a href="${pageContext.request.contextPath}/r/${restaurant.id}" class="error-btn">Back to Home</a>
</div>
<jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
