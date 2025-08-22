package FileManager;

import Entities.Clan;
import Enums.KategorijaClana;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CsvUserRepo {
    // Date-Time formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Function for loading users from a csv file
    public static Clan[] loadUsersFromCSV(String CSV_FILE_PATH) {
        List<Clan> userList = new ArrayList<>();

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
                        Clan user = createUserFromCSV(values);
                        userList.add(user);
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                    }
                }
            }

            // If CSV loaded successfully
            if (!userList.isEmpty()) {
                System.out.println("Loaded " + userList.size() + " users from CSV file.");
                return userList.toArray(new Clan[0]);
            } else {
                System.out.println("No users found.");
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    private static Clan createUserFromCSV(String[] values) {
        String firstName = values[0].trim();
        String lastName = values[1].trim();
        String gender = values[2].trim();
        LocalDate birthDate = LocalDate.parse(values[3].trim(), DATE_FORMATTER);
        String phone = values[4].trim();
        String address = values[5].trim();
        String username = values[6].trim();
        String password = values[7].trim();
        KategorijaClana category = KategorijaClana.valueOf(values[8].trim());

        return new Clan(firstName, lastName, gender, birthDate, phone, address, username, password,  category);
    }
}
