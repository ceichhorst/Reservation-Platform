package com.ceichhorst.reservation.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Service responsible for sending email notifications using SMTP
 *
 * @author ceichhorst
 */
public class EmailService {

    private final String username;
    private final String password;

    private final Session session;

    public EmailService() {
        Properties config = new Properties();

        try (InputStream input =
                getClass().getClassLoader()
                                .getResourceAsStream("email.properties")) {

            if (input == null) {
                throw new RuntimeException("email.properties not found");
            }

            config.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load email properties", e);
        }

        username = config.getProperty("mail.username");
        password = config.getProperty("mail.password");

        Properties props = new Properties();

        props.put("mail.smtp.auth",config.getProperty("mail.smtp.auth"));
        props.put("mail.smtp.starttls.enable", config.getProperty("mail.smtp.starttls.enable"));
        props.put("mail.smtp.host", config.getProperty("mail.smtp.host"));
        props.put("mail.smtp.port", config.getProperty("mail.smtp.port"));

        session = Session.getInstance(props, new Authenticator() {
            @Override
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
