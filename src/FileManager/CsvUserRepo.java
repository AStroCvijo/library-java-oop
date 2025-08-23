package FileManager;

import Entities.Clan;
import Enums.KategorijaClana;
import Enums.Pol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvUserRepo {
    // Date-Time formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String CSV_HEADER = "firstName,lastName,gender,birthDate,phone,address,username,password,category";

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

    // Function for saving users to CSV file
    public static boolean saveUsersToCSV(String CSV_FILE_PATH, Clan[] users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            // Write header
            bw.write(CSV_HEADER);
            bw.newLine();

            // Write each user
            for (Clan user : users) {
                bw.write(convertUserToCSVLine(user));
                bw.newLine();
            }

            System.out.println("Successfully saved " + users.length + " users to CSV file.");
            return true;

        } catch (IOException e) {
            System.out.println("Error saving users to CSV: " + e.getMessage());
            return false;
        }
    }

    // Function to add a new user to CSV
    public static boolean addUserToCSV(String CSV_FILE_PATH, Clan newUser) {
        // First load existing users
        Clan[] existingUsers = loadUsersFromCSV(CSV_FILE_PATH);
        List<Clan> userList = new ArrayList<>();

        if (existingUsers != null) {
            Collections.addAll(userList, existingUsers);
        }

        // Add new user
        userList.add(newUser);

        // Save back to CSV
        return saveUsersToCSV(CSV_FILE_PATH, userList.toArray(new Clan[0]));
    }

    private static Clan createUserFromCSV(String[] values) {
        String firstName = values[0].trim();
        String lastName = values[1].trim();
        Pol gender = Pol.valueOf(values[2].trim());
        LocalDate birthDate = LocalDate.parse(values[3].trim(), DATE_FORMATTER);
        String phone = values[4].trim();
        String address = values[5].trim();
        String username = values[6].trim();
        String password = values[7].trim();
        KategorijaClana category = KategorijaClana.valueOf(values[8].trim());

        return new Clan(firstName, lastName, gender, birthDate, phone, address, username, password, category);
    }

    private static String convertUserToCSVLine(Clan user) {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getBirthDate().format(DATE_FORMATTER),
                user.getPhone(),
                user.getAddress(),
                user.getUsername(),
                user.getPassword(),
                user.getCategory().name()
        );
    }
}