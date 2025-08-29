package model.managers;

import model.entities.Employee;
import model.enums.EmployeeEducationLevel;
import model.enums.Gender;
import model.enums.EmployeeRole;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeManager implements IManager<Employee> {
    private List<Employee> employees;
    private String filename;

    public EmployeeManager(String filename) {
        this.employees = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }

    @Override
    public void add(Employee employee) {
        employees.add(employee);
        saveToFile();
    }

    @Override
    public Employee getById(int id) {
        return employees.stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Employee> getAll() {
        return new ArrayList<>(employees);
    }

    @Override
    public void update(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == employee.getId()) {
                employees.set(i, employee);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        employees.removeIf(e -> e.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        employees.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Preskoči header
                }

                String[] data = line.split(",");
                if (data.length >= 13) {
                    int id = Integer.parseInt(data[0]);
                    String firstName = data[1];
                    String lastName = data[2];
                    Gender gender = Gender.valueOf(data[3]);
                    LocalDate birthDate = LocalDate.parse(data[4]);
                    String phone = data[5];
                    String address = data[6];
                    String username = data[7];
                    String password = data[8];
                    EmployeeEducationLevel educationLevel = EmployeeEducationLevel.valueOf(data[9]);
                    int yearsOfExperience = Integer.parseInt(data[10]);
                    double baseSalary = Double.parseDouble(data[11]);
                    EmployeeRole role = EmployeeRole.valueOf(data[12]);

                    Employee employee = new Employee(id, firstName, lastName, gender,
                            birthDate, phone, address, username,
                            password, educationLevel,
                            yearsOfExperience, baseSalary, role);
                    employees.add(employee);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading employees: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("id,firstName,lastName,gender,birthDate,phone,address,username,password,educationLevel,yearsOfExperience,baseSalary");

            for (Employee employee : employees) {
                writer.println(employee.getId() + "," +
                        employee.getFirstName() + "," +
                        employee.getLastName() + "," +
                        employee.getGender() + "," +
                        employee.getBirthDate() + "," +
                        employee.getPhone() + "," +
                        employee.getAddress() + "," +
                        employee.getUsername() + "," +
                        employee.getPassword() + "," +
                        employee.getEducationLevel() + "," +
                        employee.getYearsOfExperience() + "," +
                        employee.getBaseSalary() + "," +
                        employee.getRole());

            }
        } catch (IOException e) {
            System.out.println("Error saving employees: " + e.getMessage());
        }
    }
}