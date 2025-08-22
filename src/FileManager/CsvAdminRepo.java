package FileManager;

import Entities.Administrator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
}
