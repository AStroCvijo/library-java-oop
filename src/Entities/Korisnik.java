package Entities;

import java.time.LocalDate;
import Enums.*;

class Korisnik {
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private String phone;
    private String address;
    private String username;
    private String password;
    private TipKorisnika userType;

    // Constructor
    public Korisnik(String firstName, String lastName, String gender, LocalDate birthDate,
                String phone, String address, String username, String password, TipKorisnika userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // Getters and setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public TipKorisnika getUserType() { return userType; }
    public void setUserType(TipKorisnika userType) { this.userType = userType; }
}
