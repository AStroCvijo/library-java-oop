package tests;

import model.entities.ReservationService;
import model.managers.ReservationServiceManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceManagerTest {

    private ReservationServiceManager reservationServiceManager;
    private String testFilename = "test_reservation_services.csv";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(testFilename);
        testFile.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("id,reservationId,additionalServiceId,quantity,price");
        }
        reservationServiceManager = new ReservationServiceManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetReservationService() {
        ReservationService rs = new ReservationService(1, 1, 1, 1, 50.0);
        reservationServiceManager.add(rs);
        ReservationService retrievedRs = reservationServiceManager.getById(1);
        assertNotNull(retrievedRs);
        assertEquals(1, retrievedRs.getReservationId());
    }

    @Test
    void updateReservationService() {
        ReservationService rs = new ReservationService(1, 1, 1, 1, 50.0);
        reservationServiceManager.add(rs);
        rs.setPrice(60.0);
        reservationServiceManager.update(rs);
        ReservationService retrievedRs = reservationServiceManager.getById(1);
        assertEquals(60.0, retrievedRs.getPrice());
    }

    @Test
    void deleteReservationService() {
        ReservationService rs = new ReservationService(1, 1, 1, 1, 50.0);
        reservationServiceManager.add(rs);
        reservationServiceManager.delete(1);
        assertNull(reservationServiceManager.getById(1));
    }

    @Test
    void getAllReservationServices() {
        reservationServiceManager.add(new ReservationService(1, 1, 1, 1, 50.0));
        reservationServiceManager.add(new ReservationService(2, 1, 2, 2, 100.0));
        List<ReservationService> rss = reservationServiceManager.getAll();
        assertEquals(2, rss.size());
    }
}
