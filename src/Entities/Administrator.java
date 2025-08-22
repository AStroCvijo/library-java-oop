package Entities;

import java.time.LocalDate;
import Enums.*;

public class Administrator extends Korisnik {

    public Administrator(String firstName, String lastName, String gender, LocalDate birthDate,
                         String phone, String address, String username, String password) {

        super(firstName, lastName, gender, birthDate, phone, address, username, password, TipKorisnika.ADMINISTRATOR);
    }
}
