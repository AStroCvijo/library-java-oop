package Entities;

import java.time.LocalDate;

public class Zaposleni extends Korisnik {
    private String LevelOfEducation;
    private Integer Experience;
    private Double Salary;

    // Constructor
    public Zaposleni(String firstName, String lastName, String gender, LocalDate birthDate, String phone,
                     String address, String username, String password, String levelOfEducation,
                     Integer experience, Double salary) {

        super(firstName, lastName, gender, birthDate, phone, address, username, password);
        this.LevelOfEducation = levelOfEducation;
        this.Experience = experience;
        this.Salary = 1000 * (100 + 0.004 * experience);
    }

    // Getters and Setters
    public String getLevelOfEducation() { return LevelOfEducation; }
    public void setLevelOfEducation(String levelOfEducation) { LevelOfEducation = levelOfEducation; }

    public Integer getExperience() { return Experience; }
    public void setExperience(Integer experience) { Experience = experience; }

    public Double getSalary() { return Salary; }
    public void setSalary(Double salary) { Salary = salary; }
}