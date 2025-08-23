package Entities;

import java.time.LocalDate;
import Enums.*;

public class Bibliotekar extends Korisnik {
    private NivoStrucneSpreme LevelOfEducation;
    private Integer Experience;
    private Double Salary;

    // Constructor
    public Bibliotekar(String firstName, String lastName, Pol gender, LocalDate birthDate, String phone,
                       String address, String username, String password, NivoStrucneSpreme levelOfEducation,
                       Integer experience) {

        super(firstName, lastName, gender, birthDate, phone, address, username, password, TipKorisnika.BIBLITEKAR);
        this.LevelOfEducation = levelOfEducation;
        this.Experience = experience;
        this.Salary = 1000 * (100 + 0.004 * experience);
    }

    // Getters and Setters
    public NivoStrucneSpreme getLevelOfEducation() { return LevelOfEducation; }
    public void setLevelOfEducation(NivoStrucneSpreme levelOfEducation) { LevelOfEducation = levelOfEducation; }

    public Integer getExperience() { return Experience; }
    public void setExperience(Integer experience) { Experience = experience; }

    public Double getSalary() { return Salary; }
    public void setSalary(Double salary) { Salary = salary; }
}