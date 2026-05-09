package com.ceichhorst.reservation.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

/**
 * Service responsible for sending email notifications using SMTP
 *
 * @author ceichhorst
 */
public class EmailService {

    // TODO Note security with it - AWS would be better but this can serve purpose of project
    private final String username = "dyanasystems@gmail.com";
    private final String password = "wnhswfdiutuukfgh";

    private final Session session;

    public EmailService() {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        session.setDebug(true);
    }

    public void sendReservationConfirmation(
        String toEmail,
        String customerName,
        String restaurantName,
        String restaurantEmail,
        String date,
        String time,
        int partySize,
        String allergenInfo,
        String additionalComments
    ) {
        try {
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            message.setSubject("Reservation Confirmation");

            String body =
                    "Hello " + customerName + ",\n\n" +
                    "Your reservation at " + restaurantName + " is confirmed:\n" +
                    "Date: " + date + "\n" +
                    "Time: " + time + "\n" +
                    "Party Size: " + partySize + "\n" +
                    "\n" +
                    "Allergen Info: " + allergenInfo + "\n" +
                    "Comments: " + additionalComments + "\n\n" +
                    "Questions? Contact us at " + restaurantEmail + "\n\n" +
                    "Thank you for booking with us!";

            message.setText(body);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

    }
}
