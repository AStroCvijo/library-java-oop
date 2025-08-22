import java.util.Scanner;
import Entities.*;
import FileManager.CsvAdminRepo;

public class Main {
    // Array of Administrators
    private static Administrator[] administrators = {};
    private static Bibliotekar[] librarians = {};
    private static Clan[] users = {};

    // Path to Administrator, Librarian, User CSV
    private static final String CSV_ADMIN_FILE_PATH = "Data/administrator.csv";
    private static final String CSV_LIBRARIAN_FILE_PATH = "Data/zaposleni.csv";
    private static final String CSV_USER_FILE_PATH = "Data/clan.csv";

    public static void main(String[] args) {
        // Load administrators, librarians and users from CSV file
        administrators = CsvAdminRepo.loadAdministratorsFromCSV(CSV_ADMIN_FILE_PATH);

        // If there are no administrators
        if (administrators == null || administrators.length == 0) {
            System.out.println("No administrators loaded from CSV.");
        }

        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        Administrator currentAdmin;

        while (!loggedIn) {
            System.out.println("=== ADMINISTRATOR LOGIN ===");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            currentAdmin = authenticate(username, password);

            if (currentAdmin != null) {
                loggedIn = true;
                System.out.println("\nLogin successful! Welcome, " + currentAdmin.getFirstName() + "!");
                showAdminMenu(currentAdmin, scanner);
            } else {
                System.out.println("\nInvalid username or password. Please try again.\n");
            }
        }

        scanner.close();
    }

    // Function for authenticating admins
    private static Administrator authenticate(String username, String password) {
        for (Administrator admin : administrators) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    // Function for printing the admin menu
    private static void showAdminMenu(Administrator admin, Scanner scanner) {
        System.out.println("Welcome Administrator: " + admin.getFirstName() + " " + admin.getLastName());
    }
}