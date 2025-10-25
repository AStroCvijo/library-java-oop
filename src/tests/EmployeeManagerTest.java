package tests;

import model.entities.Employee;
import model.enums.EmployeeEducationLevel;
import model.enums.EmployeeRole;
import model.enums.Gender;
import model.managers.EmployeeManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeManagerTest {

    private EmployeeManager employeeManager;
    private String testFilename = "test_employees.csv";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(testFilename);
        testFile.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("id,firstName,lastName,gender,birthDate,phone,address,username,password,educationLevel,yearsOfExperience,baseSalary,role");
        }
        employeeManager = new EmployeeManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetEmployee() {
        Employee employee = new Employee(1, "John", "Doe", Gender.MALE, LocalDate.of(1990, 1, 1), "123456", "Address", "john", "pass", EmployeeEducationLevel.BACHELOR, 5, 50000, EmployeeRole.LIBRARIAN);
        employeeManager.add(employee);
        Employee retrievedEmployee = employeeManager.getById(1);
        assertNotNull(retrievedEmployee);
        assertEquals("John", retrievedEmployee.getFirstName());
    }

    @Test
    void updateEmployee() {
        Employee employee = new Employee(1, "John", "Doe", Gender.MALE, LocalDate.of(1990, 1, 1), "123456", "Address", "john", "pass", EmployeeEducationLevel.BACHELOR, 5, 50000, EmployeeRole.LIBRARIAN);
        employeeManager.add(employee);
        employee.setFirstName("Jane");
        employeeManager.update(employee);
        Employee retrievedEmployee = employeeManager.getById(1);
        assertEquals("Jane", retrievedEmployee.getFirstName());
    }

    @Test
    void deleteEmployee() {
        Employee employee = new Employee(1, "John", "Doe", Gender.MALE, LocalDate.of(1990, 1, 1), "123456", "Address", "john", "pass", EmployeeEducationLevel.BACHELOR, 5, 50000, EmployeeRole.LIBRARIAN);
        employeeManager.add(employee);
        employeeManager.delete(1);
        assertNull(employeeManager.getById(1));
    }

    @Test
    void getAllEmployees() {
        employeeManager.add(new Employee(1, "John", "Doe", Gender.MALE, LocalDate.of(1990, 1, 1), "123456", "Address", "john", "pass", EmployeeEducationLevel.BACHELOR, 5, 50000, EmployeeRole.LIBRARIAN));
        employeeManager.add(new Employee(2, "Jane", "Doe", Gender.FEMALE, LocalDate.of(1992, 2, 2), "654321", "Address 2", "jane", "pass", EmployeeEducationLevel.MASTER, 2, 60000, EmployeeRole.ADMINISTRATOR));
        List<Employee> employees = employeeManager.getAll();
        assertEquals(2, employees.size());
    }
}
