<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <title>Admin Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<c:url value='/css/styles.css' />">
</head>
<body>
    <div class="login-container">
        <h2>Admin Login</h2>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <form method="post" action="login">
            <input type="text" name="username" placeholder="Username" required />
            <input type="password" name="password" placeholder="Password" required />
            <button type="submit">Login</button>
        </form>
    </div>

</body>
</html>
