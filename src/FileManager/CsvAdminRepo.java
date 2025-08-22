package FileManager;

import Entities.Administrator;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvAdminRepo {
    // Date-Time formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Function for loading Administrators from a csv file
    public static Administrator[] loadAdministratorsFromCSV(String CSV_FILE_PATH) {
        List<Administrator> adminList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                // Skip header line
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");

                if (values.length >= 8) {
                    try {
                        Administrator admin = createAdministratorFromCSV(values);
                        adminList.add(admin);
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                    }
                }
            }

            // If CSV loaded successfully
            if (!adminList.isEmpty()) {
                System.out.println("Loaded " + adminList.size() + " administrators from CSV file.");
                return adminList.toArray(new Administrator[0]);
            } else {
                System.out.println("No administrators found.");
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    // Add to CsvAdminRepo class
    public static boolean addAdminToCSV(String CSV_FILE_PATH, Administrator newAdmin) {
        // First load existing admins
        Administrator[] existingAdmins = loadAdministratorsFromCSV(CSV_FILE_PATH);
        List<Administrator> adminList = new ArrayList<>();

        if (existingAdmins != null) {
            for (Administrator admin : existingAdmins) {
                adminList.add(admin);
            }
        }

        // Add new admin
        adminList.add(newAdmin);

        // Save back to CSV
        return saveAdminsToCSV(CSV_FILE_PATH, adminList.toArray(new Administrator[0]));
    }

    public static boolean saveAdminsToCSV(String CSV_FILE_PATH, Administrator[] admins) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header
            bw.write("firstName,lastName,gender,birthDate,phone,address,username,password");
            bw.newLine();

            // Write each admin
            for (Administrator admin : admins) {
                bw.write(convertAdminToCSVLine(admin));
                bw.newLine();
            }

            System.out.println("Successfully saved " + admins.length + " admins to CSV file.");
            return true;

        } catch (IOException e) {
            System.out.println("Error saving admins to CSV: " + e.getMessage());
            return false;
        }
    }

    private static Administrator createAdministratorFromCSV(String[] values) {
        String firstName = values[0].trim();
        String lastName = values[1].trim();
        String gender = values[2].trim();
        LocalDate birthDate = LocalDate.parse(values[3].trim(), DATE_FORMATTER);
        String phone = values[4].trim();
        String address = values[5].trim();
        String username = values[6].trim();
        String password = values[7].trim();

        return new Administrator(firstName, lastName, gender, birthDate, phone, address, username, password);
    }

    private static String convertAdminToCSVLine(Administrator admin) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                admin.getFirstName(),
                admin.getLastName(),
                admin.getGender(),
                admin.getBirthDate().format(DATE_FORMATTER),
                admin.getPhone(),
                admin.getAddress(),
                admin.getUsername(),
                admin.getPassword()
        );
    }
}
