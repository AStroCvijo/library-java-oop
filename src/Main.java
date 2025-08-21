import java.time.LocalDate;
import java.util.Scanner;
import Enums.*;
import Entities.*;

public class Main {
    // Sample administrator data (in real application, this would come from database)
    private static Administrator[] administrators = {
            new Administrator("admin", "admin", "MALE", LocalDate.of(1985, 5, 15),
                    "123-456-789", "Admin Address", "admin", "admin123"),
            new Administrator("john", "doe", "MALE", LocalDate.of(1990, 8, 20),
                    "555-1234", "123 Main St", "johndoe", "password123")
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean loggedIn = false;
        Administrator currentAdmin = null;

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

    private static Administrator authenticate(String username, String password) {
        for (Administrator admin : administrators) {
            if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    private static void showAdminMenu(Administrator admin, Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("\n=== ADMIN DASHBOARD ===");
            System.out.println("Logged in as: " + admin.getFirstName() + " " + admin.getLastName());
            System.out.println("\n1. View Profile");
            System.out.println("2. Change Password");
            System.out.println("3. System Settings");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    viewProfile(admin);
                    break;
                case 2:
                    changePassword(admin, scanner);
                    break;
                case 3:
                    System.out.println("System settings functionality would go here.");
                    break;
                case 4:
                    System.out.println("Logging out... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void viewProfile(Administrator admin) {
        System.out.println("\n=== PROFILE INFORMATION ===");
        System.out.println("Name: " + admin.getFirstName() + " " + admin.getLastName());
        System.out.println("Gender: " + admin.getGender());
        System.out.println("Birth Date: " + admin.getBirthDate());
        System.out.println("Phone: " + admin.getPhone());
        System.out.println("Address: " + admin.getAddress());
        System.out.println("Username: " + admin.getUsername());
    }

    private static void changePassword(Administrator admin, Scanner scanner) {
        System.out.println("\n=== CHANGE PASSWORD ===");
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        if (currentPassword.equals(admin.getPassword())) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine();
            System.out.print("Confirm new password: ");
            String confirmPassword = scanner.nextLine();

            if (newPassword.equals(confirmPassword)) {
                admin.setPassword(newPassword);
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Passwords do not match!");
            }
        } else {
            System.out.println("Current password is incorrect!");
        }
    }
}