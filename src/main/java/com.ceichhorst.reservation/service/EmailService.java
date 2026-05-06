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
    private final String password = "placeholder"; // TODO EMAIL INFO TO BE ADDED

    private final Session session;

    public EmailService() {
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "stmp.gmail.com");
        props.put("mail.smpt.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendReservationConfirmation(
        String toEmail,
        String customerName,
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
                    "Your reservation is confirmed:\n" +
                    "Date: " + date + "\n" +
                    "Time: " + time + "\n" +
                    "Party Size: " + partySize + "\n" +
                    "\n" +
                    "Allergen Info: " + allergenInfo + "\n" +
                    "Comments: " + additionalComments + "\n\n" +
                    "Thank you for booking with us!";

            message.setText(body);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }

    }
}
