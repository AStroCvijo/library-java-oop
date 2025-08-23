package CLI;

import Entities.Bibliotekar;

import java.time.LocalDate;
import java.util.Scanner;

import Entities.Clan;
import Enums.*;
import FileManager.CsvUserRepo;

public class LibrarianUI {
    // Function for handling Librarian login
    public static void handleLibrarianLogin(Scanner scanner, Bibliotekar[] librarians, String CSV_USER_FILE_PATH) {
        System.out.println("\n========== BIBLIOTEKAR LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Bibliotekar librarian = authenticateLibrarian(username, password, librarians);
        if (librarian != null) {
            System.out.println("\nLogin successful! Welcome, " + librarian.getFirstName() + "!");
            showLibrarianMenu(librarian, scanner, CSV_USER_FILE_PATH);
        } else {
            System.out.println("\nInvalid username or password.\n");
        }
    }

    // Function for authenticating librarians
    private static Bibliotekar authenticateLibrarian(String username, String password, Bibliotekar[] librarians) {
        for (Bibliotekar librarian : librarians) {
            if (librarian != null && librarian.getUsername().equals(username) && librarian.getPassword().equals(password)) {
                return librarian;
            }
        }
        return null;
    }

    // Function for printing the librarian menu
    private static void showLibrarianMenu(Bibliotekar librarian, Scanner scanner, String CSV_USER_FILE_PATH) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n========== BIBLIOTEKAR MENU ==========");
            System.out.println("Welcome: " + librarian.getFirstName() + " " + librarian.getLastName());
            System.out.println("1. Upravljaj izdavanjem knjiga");
            System.out.println("2. Upravljaj vraćanjem knjiga");
            System.out.println("3. Upravljaj rezervacijama (potvrdi/odbij)");
            System.out.println("4. Pregled dnevnih izdavanja i rezervacija");
            System.out.println("5. Pregled dostupnih i izdatih knjiga");
            System.out.println("6. Učlanjivanje novih članova");
            System.out.println("7. Upravljaj produžetkom članarine");
            System.out.println("8. Logout");
            System.out.print("Choose option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.\n");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("Upravljanje izdavanjem knjiga...");
                    // Manage book loans
                    break;
                case 2:
                    System.out.println("Upravljanje vraćanjem knjiga...");
                    // Manage book returns
                    break;
                case 3:
                    System.out.println("Upravljanje rezervacijama...");
                    // Confirm/reject reservations
                    break;
                case 4:
                    System.out.println("Pregled dnevnih izdavanja i rezervacija...");
                    // View daily issues and reservations
                    break;
                case 5:
                    System.out.println("Pregled dostupnih i izdatih knjiga...");
                    // View available and issued books
                    break;
                case 6:
                    // New member registration
                    registerNewMember(scanner , CSV_USER_FILE_PATH);
                    break;
                case 7:
                    System.out.println("Upravljanje produžetkom članarine...");
                    // Handle membership renewal
                    break;
                case 8:
                    System.out.println("Logging out...");
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Function for handling User registration
    private static void registerNewMember(Scanner scanner, String CSV_USER_FILE_PATH) {
        System.out.println("\n========== REGISTRACIJA NOVOG ČLANA ==========");

        try {
            // Collect member information
            System.out.print("Ime: ");
            String firstName = scanner.nextLine();

            System.out.print("Prezime: ");
            String lastName = scanner.nextLine();

            System.out.print("Pol (Muški/Ženski): ");
            String gender = scanner.nextLine();

            System.out.print("Datum rođenja (YYYY-MM-DD): ");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Telefon: ");
            String phone = scanner.nextLine();

            System.out.print("Adresa: ");
            String address = scanner.nextLine();

            System.out.print("Email (koristiće se kao korisničko ime): ");
            String email = scanner.nextLine();

            System.out.print("Sifra: ");
            String password = scanner.nextLine();

            System.out.println("Dostupne kategorije članova:");
            for (KategorijaClana category : KategorijaClana.values()) {
                System.out.println("- " + category.name());
            }

            System.out.print("Kategorija člana: ");
            KategorijaClana category = KategorijaClana.valueOf(scanner.nextLine().toUpperCase());

            // Create new member
            Clan newMember = new Clan(firstName, lastName, gender, birthDate, phone, address,
                    email, password, category);

            // Save to CSV
            boolean success = CsvUserRepo.addUserToCSV(CSV_USER_FILE_PATH, newMember);

            if (success) {
                System.out.println("Član je uspešno registrovan!");
                System.out.println("Korisničko ime: " + email);
            } else {
                System.out.println("Došlo je do greške pri registraciji člana.");
            }

        } catch (Exception e) {
            System.out.println("Greška pri unosu podataka: " + e.getMessage());
        }
    }
}
