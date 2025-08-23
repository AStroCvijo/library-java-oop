package Entities;
import Enums.KategorijaClana;
import Enums.Pol;
import Enums.TipKorisnika;

import java.time.LocalDate;

public class Clan extends Korisnik {
    private KategorijaClana Category;

    // Constructor
    public Clan(String firstName, String lastName, Pol gender, LocalDate birthDate, String phone, String address, String username, String password, KategorijaClana Category) {
        super(firstName, lastName, gender, birthDate, phone, address, username, password, TipKorisnika.CLAN);

        this.Category = Category;
    }

    // Getters and Setters
    public KategorijaClana getCategory() { return Category; }
    public void setCategory(KategorijaClana Category) { Category = Category; }
}
