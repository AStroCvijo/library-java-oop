import java.util.Scanner;
import Entities.*;
import FileManager.CsvAdminRepo;
import FileManager.CsvLibrarianRepo;
import FileManager.CsvUserRepo;

public class Main {
    // Array of Administrators, Librarians, and Users
    private static Administrator[] administrators = {};
    private static Bibliotekar[] librarians = {};
    private static Clan[] users = {};

    // Path to Administrator, Librarian, User CSV
    private static final String CSV_ADMIN_FILE_PATH = "Data/administrator.csv";
    private static final String CSV_LIBRARIAN_FILE_PATH = "Data/bibliotekar.csv";
    private static final String CSV_USER_FILE_PATH = "Data/clan.csv";

    public static void main(String[] args) {
        // Load administrators, librarians and users from CSV file
        administrators = CsvAdminRepo.loadAdministratorsFromCSV(CSV_ADMIN_FILE_PATH);
        librarians = CsvLibrarianRepo.loadLibrariansFromCSV(CSV_LIBRARIAN_FILE_PATH);
        users = CsvUserRepo.loadUsersFromCSV(CSV_USER_FILE_PATH);

        // Check if any data was loaded
        if (administrators == null || administrators.length == 0) {
            System.out.println("No administrators loaded from CSV.");
        }
        if (librarians == null || librarians.length == 0) {
            System.out.println("No librarians loaded from CSV.");
        }
        if (users == null || users.length == 0) {
            System.out.println("No users loaded from CSV.");
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("========== BIBLIOTEKA LOGIN ==========");
            System.out.println("1. Administrator Login");
            System.out.println("2. Bibliotekar Login");
            System.out.println("3. Član Login");
            System.out.println("4. Exit");
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
                    handleAdminLogin(scanner);
                    break;
                case 2:
                    handleLibrarianLogin(scanner);
                    break;
                case 3:
                    handleUserLogin(scanner);
                    break;
                case 4:
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
        scanner.close();
    }

    // Function for authenticating admins
    private static Administrator authenticateAdmin(String username, String password) {
        for (Administrator admin : administrators) {
            if (admin != null && admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    // Function for authenticating librarians
    private static Bibliotekar authenticateLibrarian(String username, String password) {
        for (Bibliotekar librarian : librarians) {
            if (librarian != null && librarian.getUsername().equals(username) && librarian.getPassword().equals(password)) {
                return librarian;
            }
        }
        return null;
    }

    // Function for authenticating users
    private static Clan authenticateUser(String username, String password) {
        for (Clan user : users) {
            if (user != null && user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private static void handleAdminLogin(Scanner scanner) {
        System.out.println("\n========== ADMINISTRATOR LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Administrator admin = authenticateAdmin(username, password);
        if (admin != null) {
            System.out.println("\nLogin successful! Welcome, " + admin.getFirstName() + "!");
            showAdminMenu(admin, scanner);
        } else {
            System.out.println("\nInvalid username or password.\n");
        }
    }

    private static void handleLibrarianLogin(Scanner scanner) {
        System.out.println("\n========== BIBLIOTEKAR LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Bibliotekar librarian = authenticateLibrarian(username, password);
        if (librarian != null) {
            System.out.println("\nLogin successful! Welcome, " + librarian.getFirstName() + "!");
            showLibrarianMenu(librarian, scanner);
        } else {
            System.out.println("\nInvalid username or password.\n");
        }
    }

    private static void handleUserLogin(Scanner scanner) {
        System.out.println("\n========== ČLAN LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Clan user = authenticateUser(username, password);
        if (user != null) {
            System.out.println("\nLogin successful! Welcome, " + user.getFirstName() + "!");
            showUserMenu(user, scanner);
        } else {
            System.out.println("\nInvalid username or password.\n");
        }
    }

    // Function for printing the admin menu
    private static void showAdminMenu(Administrator admin, Scanner scanner) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n========== ADMINISTRATOR MENU ==========");
            System.out.println("Welcome: " + admin.getFirstName() + " " + admin.getLastName());
            System.out.println("1. Upravljaj zaposlenima (CRUD)");
            System.out.println("2. Upravljaj članovima (CRUD)");
            System.out.println("3. Upravljaj knjigama (CRUD)");
            System.out.println("4. Upravljaj žanrovima (CRUD)");
            System.out.println("5. Upravljaj dodatnim uslugama (CRUD)");
            System.out.println("6. Upravljaj cenovnikom");
            System.out.println("7. Prikazi izveštaje (prihodi i rashodi)");
            System.out.println("8. Podešavanja (vreme trajanja najma)");
            System.out.println("9. Registruj nove administratore i bibliotekare");
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
                    System.out.println("Upravljanje zaposlenima...");
                    // CRUD operations for employees
                    break;
                case 2:
                    System.out.println("Upravljanje članovima...");
                    // CRUD operations for members
                    break;
                case 3:
                    System.out.println("Upravljanje knjigama...");
                    // CRUD operations for books
                    break;
                case 4:
                    System.out.println("Upravljanje žanrovima...");
                    // CRUD operations for genres
                    break;
                case 5:
                    System.out.println("Upravljanje dodatnim uslugama...");
                    // CRUD operations for additional services
                    break;
                case 6:
                    System.out.println("Upravljanje cenovnikom...");
                    // Manage pricing
                    break;
                case 7:
                    System.out.println("Prikaz izveštaja...");
                    // Show income/expense reports
                    break;
                case 8:
                    System.out.println("Podešavanje vremena trajanja najma...");
                    // Configure loan duration
                    break;
                case 9:
                    System.out.println("Registracija novih administratorea i bibliotekara...");
                    // Register new staff
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

    // Function for printing the librarian menu
    private static void showLibrarianMenu(Bibliotekar librarian, Scanner scanner) {
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
                    System.out.println("Učlanjivanje novih članova...");
                    // Register new members
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

    // Function for printing the user menu
    private static void showUserMenu(Clan user, Scanner scanner) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n========== ČLAN MENU ==========");
            System.out.println("Welcome: " + user.getFirstName() + " " + user.getLastName());
            System.out.println("1. Pregled dostupnih knjiga");
            System.out.println("2. Zahtev za rezervaciju knjige");
            System.out.println("3. Pregled mojih rezervacija sa statusima");
            System.out.println("4. Otkazivanje rezervacije");
            System.out.println("5. Zahtev za produžetak članarine");
            System.out.println("6. Logout");
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
                    System.out.println("Pregled dostupnih knjiga...");
                    // Browse available books
                    break;
                case 2:
                    System.out.println("Zahtev za rezervaciju knjige...");
                    // Make reservation request
                    break;
                case 3:
                    System.out.println("Pregled mojih rezervacija...");
                    // View reservations with status
                    break;
                case 4:
                    System.out.println("Otkazivanje rezervacije...");
                    // Cancel reservation
                    break;
                case 5:
                    System.out.println("Zahtev za produžetak članarine...");
                    // Request membership renewal
                    break;
                case 6:
                    System.out.println("Logging out...");
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}