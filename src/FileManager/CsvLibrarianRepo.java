package FileManager;

import Entities.Bibliotekar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Enums.*;

public class CsvLibrarianRepo {
    // Date-Time formatter
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Function for loading Librarians from a csv file
    public static Bibliotekar[] loadLibrariansFromCSV(String CSV_FILE_PATH) {
        List<Bibliotekar> librarianList = new ArrayList<>();

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
                        Bibliotekar bibliotekar = createLibrarianFromCSV(values);
                        librarianList.add(bibliotekar);
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                    }
                }
            }

            // If CSV loaded successfully
            if (!librarianList.isEmpty()) {
                System.out.println("Loaded " + librarianList.size() + " librarians from CSV file.");
                return librarianList.toArray(new Bibliotekar[0]);
            } else {
                System.out.println("No librarians found.");
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    private static Bibliotekar createLibrarianFromCSV(String[] values) {
        String firstName = values[0].trim();
        String lastName = values[1].trim();
        String gender = values[2].trim();
        LocalDate birthDate = LocalDate.parse(values[3].trim(), DATE_FORMATTER);
        String phone = values[4].trim();
        String address = values[5].trim();
        String username = values[6].trim();
        String password = values[7].trim();
        NivoStrucneSpreme levelOfEducation = NivoStrucneSpreme.valueOf(values[8].trim());
        Integer Experience = Integer.valueOf(values[9].trim());

        return new Bibliotekar(firstName, lastName, gender, birthDate, phone, address, username, password, levelOfEducation, Experience);
    }
}
