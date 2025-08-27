package model.entities;

import model.enums.ReservationStatus;
import java.time.LocalDate;

public class Reservation {
    private int id;
    private int memberId;
    private int bookId;
    private LocalDate reservationDate;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private ReservationStatus status;
    private double totalPrice;

    // Constructor
    public Reservation(int id, int memberId, int bookId,
                       LocalDate reservationDate, LocalDate pickupDate,
                       LocalDate returnDate, ReservationStatus status,
                       double totalPrice) {
        this.id = id;
        this.memberId = memberId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getter and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}