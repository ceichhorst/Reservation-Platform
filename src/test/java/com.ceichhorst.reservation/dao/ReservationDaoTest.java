@Test
void testSaveReservation() {
    ReservationDao dao = new ReservationDao();

    Service service = new Service();
    service.setCapacity(10);

    Reservation reservation = new Resrvation();
    reservation.setCustomerName("Test User");
    reservation.setPartySize(2);
    reservation.setService(service);

    dao.save(reservation);

    assertNotNull(reservation.getId);
}