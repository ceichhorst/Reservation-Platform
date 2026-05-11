<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<header>
    <div class="header-bar">
        <!-- MAIN LOGO / HOME BUTTON -->
        <div class="header-left">
            <a href="${pageContext.request.contextPath}/r/${restaurant.id}" class="logo-link">
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
                <c:set var="isLoggedIn" value="${not empty sessionScope.userEmail}" />
                <c:set var="role" value="${sessionScope.role}" />
                <!-- If LOGGED IN -->
                <c:choose>
                    <c:when test="${not isLoggedIn}">
                        <a href="${pageContext.request.contextPath}/login"
                           class="admin-link">
                            Admin Login
                        </a>
                    </c:when>
                    <c:otherwise>
                        <c:if test="${role eq 'ADMIN' or role eq 'SUPER_ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/dashboard"
                               class="admin-link">
                                Dashboard
                            </a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/logout"
                           class="admin-link">
                            Logout
                        </a>
                    </c:otherwise>
                </c:choose>

            </div>
        </div>
    </div>

</header>
