<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Reservation Confirmation | Dyana</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@100;200;300;400;500;600;700;800;900&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/main.css' />">

    <link rel="icon" type="image/png" href="<c:url value='/images/favicon-32.png' />">
</head>
<body>
    <jsp:include page="/WEB-INF/components/header.jsp" />
        <main class="confirmation-page">
            <!-- Success Message -->
            <section class="confirmation-success">
                <h1>Reservation Confirmed</h1>
                <p>
                    Thank you, <strong>${customerName}</strong>! Your reservation at
                    <strong>${restaurantName}</strong> has been successfully booked.
                </p>
            </section>
            <!-- Reservation Details -->
            <section class="confirmation-details">
                <h2>Reservation Details</h2>
                <ul>
                    <li><strong>Date:</strong> ${reservationDate}</li>
                    <!-- Applies if service allows it -->
                    <li><strong>Time:</strong> ${reservationTime}</li>
                    <li><strong>Party Size:</strong> ${partySize}</li>
                </ul>
            </section>
            <!-- Reservation Details -->
            <section class="confirmation-guest">
                <h2>Your Information</h2>
                <ul>
                    <li><strong>Name:</strong> ${customerName}</li>
                    <li><strong>Email:</strong> ${email}</li>
                    <li><strong>Allergens:</strong> ${guestAllergens}</li>
                    <li><strong>Comment:</strong> ${guestComments}</li>
                </ul>
            </section>
            <!-- Confirmation ID -->
            <section class="confirmation-meta">
                <h2>Confirmation</h2>
                <p><strong>Confirmation ID: </strong>${confirmationId}</p>
            </section>
            <!-- Helper Footer -->
            <section class="confirmation-footer">
                <p>
                    A confirmation email has been sent to <strong>${email}</strong>.
                </p>
                <p>
                    If you need to make changes to your reservation, please email <strong>${restaurantEmail}</strong>.
                </p>
            </section>
            <!-- Actions -->
            <section class="confirmation-actions">
                <a href="${pageContext.request.contextPath}/r/${restaurant.id}"
                   class="button-primary">
                   Make Another Reservation
                </a>
            </section>

        </main>
    <jsp:include page="/WEB-INF/components/footer.jsp" />
</body>
</html>
