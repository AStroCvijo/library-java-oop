package model.entities;

public class ReservationService {
    private int id;
    private int reservationId;
    private int additionalServiceId;
    private int quantity;
    private double price;

    // Constructor
    public ReservationService(int id, int reservationId,
                              int additionalServiceId, int quantity,
                              double price) {
        this.id = id;
        this.reservationId = reservationId;
        this.additionalServiceId = additionalServiceId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getter and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getAdditionalServiceId() { return additionalServiceId; }
    public void setAdditionalServiceId(int additionalServiceId) { this.additionalServiceId = additionalServiceId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}