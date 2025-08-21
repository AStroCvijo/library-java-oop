package Entities;
import Enums.Pol;

import java.time.LocalDate;

class Clan extends Korisnik {
    private String Category;

    // Constructor
    public Clan(String firstName, String lastName, String gender, LocalDate birthDate, String phone, String address, String username, String password, String Category) {
        super(firstName, lastName, gender, birthDate, phone, address, username, password);

        this.Category = Category;
    }

    // Getters and Setters
    public String getCategory() { return Category; }
    public void setCategory(String Category) { Category = Category; }
}
