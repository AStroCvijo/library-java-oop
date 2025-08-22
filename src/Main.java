import java.util.Scanner;
import Entities.*;
import FileManager.CsvAdminRepo;
import FileManager.CsvLibrarianRepo;
import FileManager.CsvUserRepo;
import UI.*;

public class Main {

    // Path to Administrator, Librarian, User CSV
    private static final String CSV_ADMIN_FILE_PATH = "Data/administrator.csv";
    private static final String CSV_LIBRARIAN_FILE_PATH = "Data/bibliotekar.csv";
    private static final String CSV_USER_FILE_PATH = "Data/clan.csv";

    public static void main(String[] args) {
        // Load administrators, librarians and users from CSV file
        Administrator[] administrators = CsvAdminRepo.loadAdministratorsFromCSV(CSV_ADMIN_FILE_PATH);
        Bibliotekar[] librarians = CsvLibrarianRepo.loadLibrariansFromCSV(CSV_LIBRARIAN_FILE_PATH);
        Clan[] users = CsvUserRepo.loadUsersFromCSV(CSV_USER_FILE_PATH);

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
                    AdminUI.handleAdminLogin(scanner, administrators, CSV_ADMIN_FILE_PATH, CSV_LIBRARIAN_FILE_PATH, CSV_USER_FILE_PATH);
                    break;
                case 2:
                    LibrarianUI.handleLibrarianLogin(scanner, librarians, CSV_USER_FILE_PATH);
                    break;
                case 3:
                    UserUI.handleUserLogin(scanner, users, CSV_USER_FILE_PATH);
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
}