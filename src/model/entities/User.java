package model.entities;

import model.enums.Gender;
import java.time.LocalDate;

public abstract class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected Gender gender;
    protected LocalDate birthDate;
    protected String phone;
    protected String address;
    protected String username;
    protected String password;

    // Constructor
    public User(int id, String firstName, String lastName, Gender gender,
                LocalDate birthDate, String phone, String address,
                String username, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    // Getter and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

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
}