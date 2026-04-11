<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header>
    <div class="header-bar">
        <!-- MAIN LOGO / HOME BUTTON -->
        <div class="header-left">
            <a href="${pageContext.request.contextPath}/home" class="logo-link">
                <div class="logo-wrap">
                    <img src="<c:url value='/images/dyana_symbol_logo_alt.svg' />"
                         class="symbol_logo"
                         alt="Dyana Logo">
                    <img src="<c:url value='/images/dyana_text_logo.svg' />"
                         class="text_logo"
                         alt="Dyana Logo">
                </div>
            </a>
        </div>

        <!-- LOGIN BUTTON -->
        <div class="header-right">
            <div class="admin-button">
                <c:set var="isAdmin" value="${not empty sessionScope.userEmail}" />
                <!-- If LOGGED IN -->
                <c:choose>
                    <c:when test="${isAdmin}">
                        <a href="${pageContext.request.contextPath}/logout"
                           class="admin-link">
                            Logout
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login"
                           class="admin-link">
                            Admin Login
                        </a>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </div>

</header>
