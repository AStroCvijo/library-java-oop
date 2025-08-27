package model.entities;

import model.enums.PriceListItemType;
import java.time.LocalDate;

public class PriceListItem {
    private int id;
    private PriceListItemType type;
    private String description;
    private double price;
    private LocalDate validFrom;
    private LocalDate validTo;

    // Constructor
    public PriceListItem(int id, PriceListItemType type, String description,
                         double price, LocalDate validFrom, LocalDate validTo) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.price = price;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    // Getter and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public PriceListItemType getType() { return type; }
    public void setType(PriceListItemType type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public LocalDate getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDate validFrom) { this.validFrom = validFrom; }

    public LocalDate getValidTo() { return validTo; }
    public void setValidTo(LocalDate validTo) { this.validTo = validTo; }
}