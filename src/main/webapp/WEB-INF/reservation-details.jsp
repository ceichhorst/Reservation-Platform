<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reservation Details | Dyana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">
    <link rel="stylesheet" href="<c:url value='/css/reservation-details.css' />">
    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
    <jsp:include page="/WEB-INF/components/header.jsp" />
    <main class="reservation-details">
        <h1>Reservation Details</h1>
        <!-- Reservation Summary -->
        <section class="reservation-summary">
            <h2>Your Reservation</h2>
            <ul>
                <li><strong>Date:</strong> ${reservationDate}</li>
                <!-- Applies if service allows it -->
                <li><strong>Time:</strong> ${reservationTime}</li>
                <li><strong>Party Size:</strong> ${partySize}</li>
            </ul>
        </section>
        <!-- Contact Information Form -->
        <section class="contact-info">
            <h2>Your Information</h2>
            <form action="${pageContext.request.contextPath}/confirm-reservation"
                  method="POST"
                  class="details-form">
                <div class="form-group">
                    <label for="customerName">Name</label>
                    <span class="required-indicator">*</span>
                    <input type="text" id="customerName" name="customerName" required aria-required="true">
                </div>
                <div class="form-group">
                    <label for="email">Email</label>
                    <span class="required-indicator">*</span>
                    <input type="email" id="email" name="email" required aria-required="true">
                </div>
                <div class="form-group">
                    <label for="guestAllergies">Allergies/Dietary Restrictions</label>
                    <c:if test="${requireAllergenInfo}">
                        <span class="required-indicator" aria-hidden="true">*</span>
                    </c:if>
                    <input type="text"
                           id="guestAllergies"
                           name="guestAllergies"
                           placeholder="If none, please enter 'N/A'"
                           <c:if test="${requireAllergenInfo}">
                               required aria-required="true"
                           </c:if>>
                </div>
                <div class="form-group">
                    <label for="guestNotes">Comments/Special Requests</label>
                    <textarea id="guestNotes"
                              name="guestNotes"
                              rows="4"
                              placeholder="Comments, celebrations, accessibility needs..."></textarea>
                </div>

                <!-- Hidden fields -->
                <input type="hidden" name="restaurantId" value="${restaurantId}">
                <input type="hidden" name="serviceInstanceId" value="${serviceInstanceId}">
                <input type="hidden" name="reservationDate" value="${reservationDate}">
                <input type="hidden" name="reservationTime" value="${reservationTime}">
                <input type="hidden" name="partySize" value="${partySize}">

                <button type="submit" class="confirm-button">
                    Confirm Reservation
                </button>
            </form>
        </section>
    </main>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
