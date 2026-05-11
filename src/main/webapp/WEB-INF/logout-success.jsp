<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ page session="true" %>
<c:set var="redirect" value="${sessionScope.postLogoutPath}" />
<c:if test="${empty redirect}">
    <c:set var="redirect" value="/" />
</c:if>
<c:remove var="postLogoutRedirect" scope="session" />
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Dyana - Logout Success</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="<c:url value='/css/main.css' />">
  <link rel="stylesheet" href="<c:url value='/css/home.css' />">
  <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
  <meta http-equiv="refresh" content="2;url=<c:url value='${redirect}' />" />
</head>
<body>
    <div class="container">
        <h2>You have been successfully logged out.</h2>
        <p>You will be redirected shortly.</p>
        <p>If you are not redirected automatically,
        <a href="<c:url value='${redirect}' />">click here</a>.
        </p>
    </div>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
