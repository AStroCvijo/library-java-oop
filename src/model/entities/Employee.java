package model.entities;

import model.enums.EmployeeEducationLevel;
import java.time.LocalDate;

public class Employee extends User {
    private EmployeeEducationLevel educationLevel;
    private int yearsOfExperience;
    private double baseSalary;

    // Constructor
    public Employee(int id, String firstName, String lastName,
                    model.enums.Gender gender, LocalDate birthDate,
                    String phone, String address, String username,
                    String password, EmployeeEducationLevel educationLevel,
                    int yearsOfExperience, double baseSalary) {
        super(id, firstName, lastName, gender, birthDate, phone, address, username, password);
        this.educationLevel = educationLevel;
        this.yearsOfExperience = yearsOfExperience;
        this.baseSalary = baseSalary;
    }

    // Helper functions

    public double calculateSalary() {
        double coefficient = getCoefficientForEducation();
        return baseSalary * (coefficient + 0.004 * yearsOfExperience);
    }

    private double getCoefficientForEducation() {
        switch (educationLevel) {
            case HIGH_SCHOOL: return 1.5;
            case BACHELOR: return 2.0;
            case MASTER: return 2.5;
            case DOCTORATE: return 3.0;
            default: return 1.0;
        }
    }

    // Getter and setters
    public EmployeeEducationLevel getEducationLevel() { return educationLevel; }
    public void setEducationLevel(EmployeeEducationLevel educationLevel) {}

    public int getYearsOfExperience() { return yearsOfExperience; }
    public void setYearsOfExperience(int yearsOfExperience) {this.yearsOfExperience = yearsOfExperience; }

    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
}