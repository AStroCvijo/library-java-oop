package CLI;

import Entities.Clan;

import java.util.Scanner;

public class UserUI {
    // Function for handling User login
    public static void handleUserLogin(Scanner scanner, Clan[] users, String csvUserFilePath) {
        System.out.println("\n========== ČLAN LOGIN ==========");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Clan user = authenticateUser(username, password, users);
        if (user != null) {
            System.out.println("\nLogin successful! Welcome, " + user.getFirstName() + "!");
            showUserMenu(user, scanner);
        } else {
            System.out.println("\nInvalid username or password.\n");
        }
    }

    // Function for authenticating users
    private static Clan authenticateUser(String username, String password, Clan[] users) {
        for (Clan user : users) {
            if (user != null && user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
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
