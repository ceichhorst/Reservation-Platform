package com.ceichhorst.reservation.service;

import jakarta.mail.Transport;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmailServiceTest {

    @Test
    void testSendReservationConfirmation_sendsEmailSuccessfully() {
        EmailService service = new EmailService();

        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {

            service.sendReservationConfirmation(
                    "test@user.com",
                    "Name",
                    "Test Restaurant",
                    "rest@email.com",
                    "2026.06-01",
                    "18:00",
                    2,
                    "None",
                    "Window seat please"
            );

            transportMock.verify(() ->
                    Transport.send(any(jakarta.mail.Message.class)),
                    times(1)
            );
        }
    }
}
