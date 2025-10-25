package tests;

import model.entities.Reservation;
import model.enums.ReservationStatus;
import model.managers.ReservationManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationManagerTest {

    private ReservationManager reservationManager;
    private String testFilename = "test_reservations.csv";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(testFilename);
        testFile.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("id,memberId,bookId,reservationDate,pickupDate,returnDate,status,totalPrice");
        }
        reservationManager = new ReservationManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetReservation() {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(8), ReservationStatus.PENDING, 0);
        reservationManager.add(reservation);
        Reservation retrievedReservation = reservationManager.getById(1);
        assertNotNull(retrievedReservation);
        assertEquals(1, retrievedReservation.getMemberId());
    }

    @Test
    void updateReservation() {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(8), ReservationStatus.PENDING, 0);
        reservationManager.add(reservation);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationManager.update(reservation);
        Reservation retrievedReservation = reservationManager.getById(1);
        assertEquals(ReservationStatus.CONFIRMED, retrievedReservation.getStatus());
    }

    @Test
    void deleteReservation() {
        Reservation reservation = new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(8), ReservationStatus.PENDING, 0);
        reservationManager.add(reservation);
        reservationManager.delete(1);
        assertNull(reservationManager.getById(1));
    }

    @Test
    void getAllReservations() {
        reservationManager.add(new Reservation(1, 1, 1, LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(8), ReservationStatus.PENDING, 0));
        reservationManager.add(new Reservation(2, 2, 2, LocalDate.now(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(9), ReservationStatus.PENDING, 0));
        List<Reservation> reservations = reservationManager.getAll();
        assertEquals(2, reservations.size());
    }
}
