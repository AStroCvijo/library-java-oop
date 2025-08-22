package UI;

import Entities.Administrator;
import Entities.Bibliotekar;
import Entities.Clan;
import FileManager.CsvAdminRepo;
import FileManager.CsvLibrarianRepo;
import FileManager.CsvUserRepo;
import Enums.KategorijaClana;
import Enums.NivoStrucneSpreme;

import java.time.LocalDate;
import java.util.Scanner;

public class AdminUI {
    private static String csvAdminFilePath;
    private static String csvLibrarianFilePath;
    private static String csvUserFilePath;

    // Function for handling Administrator login
    public static void handleAdminLogin(Scanner scanner, Administrator[] administrators,
                                        String adminFilePath, String librarianFilePath, String userFilePath) {
        csvAdminFilePath = adminFilePath;
        csvLibrarianFilePath = librarianFilePath;
        csvUserFilePath = userFilePath;

        System.out.println("\n========== ADMINISTRATOR LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Administrator admin = authenticateAdmin(username, password, administrators);
        if (admin != null) {
            System.out.println("\nLogin successful! Welcome, " + admin.getFirstName() + "!");
            showAdminMenu(admin, scanner);
        } else {
            System.out.println("\nInvalid username or password.\n");
        }
    }

    // Function for authenticating admins
    private static Administrator authenticateAdmin(String username, String password, Administrator[] administrators) {
        for (Administrator admin : administrators) {
            if (admin != null && admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    // Function for printing the admin menu
    private static void showAdminMenu(Administrator admin, Scanner scanner) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n========== ADMINISTRATOR MENU ==========");
            System.out.println("Welcome: " + admin.getFirstName() + " " + admin.getLastName());
            System.out.println("1. Upravljaj administratorima (CRUD)");
            System.out.println("2. Upravljaj bibliotekarima (CRUD)");
            System.out.println("3. Upravljaj članovima (CRUD)");
            System.out.println("4. Upravljaj knjigama (CRUD)");
            System.out.println("5. Upravljaj žanrovima (CRUD)");
            System.out.println("6. Upravljaj dodatnim uslugama (CRUD)");
            System.out.println("7. Upravljaj cenovnikom");
            System.out.println("8. Prikazi izveštaje (prihodi i rashodi)");
            System.out.println("9. Podešavanja (vreme trajanja najma)");
            System.out.println("10. Logout");
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
                    showAdminCRUDMenu(scanner);
                    break;
                case 2:
                    showLibrarianCRUDMenu(scanner);
                    break;
                case 3:
                    showUserCRUDMenu(scanner);
                    break;
                case 4:
                    System.out.println("Upravljanje knjigama...");
                    // CRUD operations for books
                    break;
                case 5:
                    System.out.println("Upravljanje žanrovima...");
                    // CRUD operations for genres
                    break;
                case 6:
                    System.out.println("Upravljanje dodatnim uslugama...");
                    // CRUD operations for additional services
                    break;
                case 7:
                    System.out.println("Upravljanje cenovnikom...");
                    // Manage pricing
                    break;
                case 8:
                    System.out.println("Prikaz izveštaja...");
                    // Show income/expense reports
                    break;
                case 9:
                    System.out.println("Podešavanje vremena trajanja najma...");
                    // Configure loan duration
                    break;
                case 10:
                    System.out.println("Logging out...");
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // ADMIN CRUD Operations
    private static void showAdminCRUDMenu(Scanner scanner) {
        boolean inCRUDMenu = true;

        while (inCRUDMenu) {
            System.out.println("\n========== ADMINISTRATORI CRUD ==========");
            System.out.println("1. Prikazi sve administratore");
            System.out.println("2. Dodaj novog administratora");
            System.out.println("3. Izmeni administratora");
            System.out.println("4. Obriši administratora");
            System.out.println("5. Nazad na glavni meni");
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
                    displayAllAdmins();
                    break;
                case 2:
                    addNewAdmin(scanner);
                    break;
                case 3:
                    updateAdmin(scanner);
                    break;
                case 4:
                    deleteAdmin(scanner);
                    break;
                case 5:
                    inCRUDMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayAllAdmins() {
        Administrator[] admins = CsvAdminRepo.loadAdministratorsFromCSV(csvAdminFilePath);
        if (admins != null && admins.length > 0) {
            System.out.println("\n===== SVI ADMINISTRATORI =====");
            for (int i = 0; i < admins.length; i++) {
                System.out.println((i+1) + ". " + admins[i].getFirstName() + " " + admins[i].getLastName() +
                        " (" + admins[i].getUsername() + ")");
            }
        } else {
            System.out.println("Nema administratora u sistemu.");
        }
    }

    private static void addNewAdmin(Scanner scanner) {
        System.out.println("\n===== DODAVANJE NOVOG ADMINISTRATORA =====");

        try {
            System.out.print("Ime: ");
            String firstName = scanner.nextLine();

            System.out.print("Prezime: ");
            String lastName = scanner.nextLine();

            System.out.print("Pol: ");
            String gender = scanner.nextLine();

            System.out.print("Datum rođenja (YYYY-MM-DD): ");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Telefon: ");
            String phone = scanner.nextLine();

            System.out.print("Adresa: ");
            String address = scanner.nextLine();

            System.out.print("Korisničko ime: ");
            String username = scanner.nextLine();

            System.out.print("Lozinka: ");
            String password = scanner.nextLine();

            Administrator newAdmin = new Administrator(firstName, lastName, gender, birthDate,
                    phone, address, username, password);

            // Add to CSV
            boolean success = CsvAdminRepo.addAdminToCSV(csvAdminFilePath, newAdmin);

            if (success) {
                System.out.println("Administrator uspešno dodat!");
            } else {
                System.out.println("Greška pri dodavanju administratora.");
            }

        } catch (Exception e) {
            System.out.println("Greška pri unosu podataka: " + e.getMessage());
        }
    }

    private static void updateAdmin(Scanner scanner) {
        // Implementation for updating admin
        System.out.println("Funkcija za izmenu administratora će biti implementirana.");
    }

    private static void deleteAdmin(Scanner scanner) {
        // Implementation for deleting admin
        System.out.println("Funkcija za brisanje administratora će biti implementirana.");
    }

    // LIBRARIAN CRUD Operations
    private static void showLibrarianCRUDMenu(Scanner scanner) {
        boolean inCRUDMenu = true;

        while (inCRUDMenu) {
            System.out.println("\n========== BIBLIOTEKARI CRUD ==========");
            System.out.println("1. Prikazi sve bibliotekare");
            System.out.println("2. Dodaj novog bibliotekara");
            System.out.println("3. Izmeni bibliotekara");
            System.out.println("4. Obriši bibliotekara");
            System.out.println("5. Nazad na glavni meni");
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    displayAllLibrarians();
                    break;
                case 2:
                    addNewLibrarian(scanner);
                    break;
                case 3:
                    updateLibrarian(scanner);
                    break;
                case 4:
                    deleteLibrarian(scanner);
                    break;
                case 5:
                    inCRUDMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayAllLibrarians() {
        Bibliotekar[] librarians = CsvLibrarianRepo.loadLibrariansFromCSV(csvLibrarianFilePath);
        if (librarians != null && librarians.length > 0) {
            System.out.println("\n===== SVI BIBLIOTEKARI =====");
            for (int i = 0; i < librarians.length; i++) {
                System.out.println((i+1) + ". " + librarians[i].getFirstName() + " " + librarians[i].getLastName() +
                        " (" + librarians[i].getUsername() + ")");
            }
        } else {
            System.out.println("Nema bibliotekara u sistemu.");
        }
    }

    private static void addNewLibrarian(Scanner scanner) {
        System.out.println("\n===== DODAVANJE NOVOG BIBLIOTEKARA =====");

        try {
            System.out.print("Ime: ");
            String firstName = scanner.nextLine();

            System.out.print("Prezime: ");
            String lastName = scanner.nextLine();

            System.out.print("Pol: ");
            String gender = scanner.nextLine();

            System.out.print("Datum rođenja (YYYY-MM-DD): ");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Telefon: ");
            String phone = scanner.nextLine();

            System.out.print("Adresa: ");
            String address = scanner.nextLine();

            System.out.print("Korisničko ime: ");
            String username = scanner.nextLine();

            System.out.print("Lozinka: ");
            String password = scanner.nextLine();

            System.out.println("Dostupni nivoi stručne spreme:");
            for (NivoStrucneSpreme level : NivoStrucneSpreme.values()) {
                System.out.println("- " + level.name());
            }

            System.out.print("Nivo stručne spreme: ");
            NivoStrucneSpreme educationLevel = NivoStrucneSpreme.valueOf(scanner.nextLine().toUpperCase());

            System.out.print("Staž (godine): ");
            int experience = Integer.parseInt(scanner.nextLine());

            Bibliotekar newLibrarian = new Bibliotekar(firstName, lastName, gender, birthDate,
                    phone, address, username, password,
                    educationLevel, experience);

            // Add to CSV
            boolean success = CsvLibrarianRepo.addLibrarianToCSV(csvLibrarianFilePath, newLibrarian);

            if (success) {
                System.out.println("Bibliotekar uspešno dodat!");
            } else {
                System.out.println("Greška pri dodavanju bibliotekara.");
            }

        } catch (Exception e) {
            System.out.println("Greška pri unosu podataka: " + e.getMessage());
        }
    }

    private static void updateLibrarian(Scanner scanner) {
        System.out.println("Funkcija za izmenu bibliotekara će biti implementirana.");
    }

    private static void deleteLibrarian(Scanner scanner) {
        System.out.println("Funkcija za brisanje bibliotekara će biti implementirana.");
    }

    // USER CRUD Operations
    private static void showUserCRUDMenu(Scanner scanner) {
        boolean inCRUDMenu = true;

        while (inCRUDMenu) {
            System.out.println("\n========== ČLANOVI CRUD ==========");
            System.out.println("1. Prikazi sve članove");
            System.out.println("2. Dodaj novog člana");
            System.out.println("3. Izmeni člana");
            System.out.println("4. Obriši člana");
            System.out.println("5. Nazad na glavni meni");
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    displayAllUsers();
                    break;
                case 2:
                    addNewUser(scanner);
                    break;
                case 3:
                    updateUser(scanner);
                    break;
                case 4:
                    deleteUser(scanner);
                    break;
                case 5:
                    inCRUDMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void displayAllUsers() {
        Clan[] users = CsvUserRepo.loadUsersFromCSV(csvUserFilePath);
        if (users != null && users.length > 0) {
            System.out.println("\n===== SVI ČLANOVI =====");
            for (int i = 0; i < users.length; i++) {
                System.out.println((i+1) + ". " + users[i].getFirstName() + " " + users[i].getLastName() +
                        " (" + users[i].getUsername() + ") - " + users[i].getCategory());
            }
        } else {
            System.out.println("Nema članova u sistemu.");
        }
    }

    private static void addNewUser(Scanner scanner) {
        System.out.println("\n===== DODAVANJE NOVOG ČLANA =====");

        try {
            System.out.print("Ime: ");
            String firstName = scanner.nextLine();

            System.out.print("Prezime: ");
            String lastName = scanner.nextLine();

            System.out.print("Pol: ");
            String gender = scanner.nextLine();

            System.out.print("Datum rođenja (YYYY-MM-DD): ");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Telefon: ");
            String phone = scanner.nextLine();

            System.out.print("Adresa: ");
            String address = scanner.nextLine();

            System.out.print("Email (korisničko ime): ");
            String email = scanner.nextLine();

            String tempPassword = "temp123"; // Default temporary password

            System.out.println("Dostupne kategorije članova:");
            for (KategorijaClana category : KategorijaClana.values()) {
                System.out.println("- " + category.name());
            }

            System.out.print("Kategorija člana: ");
            KategorijaClana category = KategorijaClana.valueOf(scanner.nextLine().toUpperCase());

            Clan newUser = new Clan(firstName, lastName, gender, birthDate,
                    phone, address, email, tempPassword, category);

            // Add to CSV
            boolean success = CsvUserRepo.addUserToCSV(csvUserFilePath, newUser);

            if (success) {
                System.out.println("Član uspešno dodat!");
                System.out.println("Privremena lozinka: " + tempPassword);
            } else {
                System.out.println("Greška pri dodavanju člana.");
            }

        } catch (Exception e) {
            System.out.println("Greška pri unosu podataka: " + e.getMessage());
        }
    }

    private static void updateUser(Scanner scanner) {
        System.out.println("Funkcija za izmenu člana će biti implementirana.");
    }

    private static void deleteUser(Scanner scanner) {
        System.out.println("Funkcija za brisanje člana će biti implementirana.");
    }
}