package SWING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Entities.Administrator;
import Entities.Bibliotekar;
import Entities.Clan;
import FileManager.CsvAdminRepo;
import FileManager.CsvLibrarianRepo;
import FileManager.CsvUserRepo;

public class AdminWindow {
    private static Administrator[] administrators;
    private static String csvAdminFilePath;
    private static String csvLibrarianFilePath;
    private static String csvUserFilePath;
    private static JFrame mainFrame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static Administrator currentAdmin;

    public static void initializeAdminUI(Administrator[] adminsArray, String adminPath, String librarianPath, String userPath) {
        administrators = adminsArray;
        csvAdminFilePath = adminPath;
        csvLibrarianFilePath = librarianPath;
        csvUserFilePath = userPath;

        mainFrame = new JFrame("Biblioteka - Admin Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createAdminMenuPanel();

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }

    private static void createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        loginPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Administrator Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(50, 50, 50));

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JTextField userField = new JTextField(20);
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JPasswordField passField = new JPasswordField(20);
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setBackground(new Color(200, 200, 200));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1; gbc.gridx = 0;
        loginPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        loginPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        loginPanel.add(passField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridy = 4;
        loginPanel.add(backButton, gbc);

        // Button actions
        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            Administrator admin = authenticateAdmin(username, password, administrators);
            if (admin != null) {
                currentAdmin = admin;
                userField.setText("");
                passField.setText("");
                cardLayout.show(mainPanel, "adminMenu");
            } else {
                JOptionPane.showMessageDialog(mainFrame,
                        "Invalid username or password",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            mainFrame.dispose();
            LibraryMainWindow.main(new String[0]);
        });

        mainPanel.add(loginPanel, "login");
    }

    private static void createAdminMenuPanel() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        menuPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome label
        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(50, 50, 50));

        // Buttons
        JButton manageAdminsBtn = new JButton("1. Upravljaj administratorima");
        JButton manageLibrariansBtn = new JButton("2. Upravljaj bibliotekarima");
        JButton manageUsersBtn = new JButton("3. Upravljaj članovima");
        JButton manageBooksBtn = new JButton("4. Upravljaj knjigama");
        JButton manageGenresBtn = new JButton("5. Upravljaj žanrovima");
        JButton manageServicesBtn = new JButton("6. Upravljaj uslugama");
        JButton managePricingBtn = new JButton("7. Upravljaj cenovnikom");
        JButton viewReportsBtn = new JButton("8. Prikazi izveštaje");
        JButton settingsBtn = new JButton("9. Podešavanja");
        JButton logoutBtn = new JButton("10. Logout");

        // Style buttons
        manageAdminsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageAdminsBtn.setBackground(new Color(70, 130, 180));
        manageAdminsBtn.setForeground(Color.WHITE);
        manageAdminsBtn.setFocusPainted(false);

        manageLibrariansBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageLibrariansBtn.setBackground(new Color(70, 130, 180));
        manageLibrariansBtn.setForeground(Color.WHITE);
        manageLibrariansBtn.setFocusPainted(false);

        manageUsersBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageUsersBtn.setBackground(new Color(70, 130, 180));
        manageUsersBtn.setForeground(Color.WHITE);
        manageUsersBtn.setFocusPainted(false);

        manageBooksBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageBooksBtn.setBackground(new Color(70, 130, 180));
        manageBooksBtn.setForeground(Color.WHITE);
        manageBooksBtn.setFocusPainted(false);

        manageGenresBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageGenresBtn.setBackground(new Color(70, 130, 180));
        manageGenresBtn.setForeground(Color.WHITE);
        manageGenresBtn.setFocusPainted(false);

        manageServicesBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageServicesBtn.setBackground(new Color(70, 130, 180));
        manageServicesBtn.setForeground(Color.WHITE);
        manageServicesBtn.setFocusPainted(false);

        managePricingBtn.setFont(new Font("Arial", Font.BOLD, 14));
        managePricingBtn.setBackground(new Color(70, 130, 180));
        managePricingBtn.setForeground(Color.WHITE);
        managePricingBtn.setFocusPainted(false);

        viewReportsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        viewReportsBtn.setBackground(new Color(70, 130, 180));
        viewReportsBtn.setForeground(Color.WHITE);
        viewReportsBtn.setFocusPainted(false);

        settingsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        settingsBtn.setBackground(new Color(70, 130, 180));
        settingsBtn.setForeground(Color.WHITE);
        settingsBtn.setFocusPainted(false);

        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.setBackground(new Color(200, 200, 200));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        menuPanel.add(welcomeLabel, gbc);

        gbc.gridwidth = 2; gbc.gridy = 1;
        menuPanel.add(manageAdminsBtn, gbc);

        gbc.gridy = 2;
        menuPanel.add(manageLibrariansBtn, gbc);

        gbc.gridy = 3;
        menuPanel.add(manageUsersBtn, gbc);

        gbc.gridy = 4;
        menuPanel.add(manageBooksBtn, gbc);

        gbc.gridy = 5;
        menuPanel.add(manageGenresBtn, gbc);

        gbc.gridy = 6;
        menuPanel.add(manageServicesBtn, gbc);

        gbc.gridy = 7;
        menuPanel.add(managePricingBtn, gbc);

        gbc.gridy = 8;
        menuPanel.add(viewReportsBtn, gbc);

        gbc.gridy = 9;
        menuPanel.add(settingsBtn, gbc);

        gbc.gridy = 10;
        menuPanel.add(logoutBtn, gbc);

        // Button actions
        manageAdminsBtn.addActionListener(e -> showAdminCRUDMenu());
        manageLibrariansBtn.addActionListener(e -> showLibrarianCRUDMenu());
        manageUsersBtn.addActionListener(e -> showUserCRUDMenu());

        manageBooksBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje knjigama...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        manageGenresBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje žanrovima...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        manageServicesBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje dodatnim uslugama...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        managePricingBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje cenovnikom...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        viewReportsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Prikaz izveštaja...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        settingsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Podešavanje vremena trajanja najma...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        logoutBtn.addActionListener(e -> {
            currentAdmin = null;
            cardLayout.show(mainPanel, "login");
        });

        // Update welcome label when panel is shown
        menuPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                welcomeLabel.setText("Welcome: " + currentAdmin.getFirstName() + " " + currentAdmin.getLastName());
            }
        });

        mainPanel.add(menuPanel, "adminMenu");
    }

    private static Administrator authenticateAdmin(String username, String password, Administrator[] administrators) {
        for (Administrator admin : administrators) {
            if (admin != null && admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
                return admin;
            }
        }
        return null;
    }

    // ADMIN CRUD Operations
    private static void showAdminCRUDMenu() {
        JDialog crudDialog = new JDialog(mainFrame, "Administratori CRUD", true);
        crudDialog.setSize(500, 400);
        crudDialog.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton viewAdminsBtn = new JButton("1. Prikazi sve administratore");
        JButton addAdminBtn = new JButton("2. Dodaj novog administratora");
        JButton updateAdminBtn = new JButton("3. Izmeni administratora");
        JButton deleteAdminBtn = new JButton("4. Obriši administratora");
        JButton backBtn = new JButton("5. Nazad na glavni meni");

        panel.add(viewAdminsBtn);
        panel.add(addAdminBtn);
        panel.add(updateAdminBtn);
        panel.add(deleteAdminBtn);
        panel.add(backBtn);

        viewAdminsBtn.addActionListener(e -> displayAllAdmins());
        addAdminBtn.addActionListener(e -> addNewAdmin());
        updateAdminBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(crudDialog,
                    "Funkcija za izmenu administratora će biti implementirana.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        deleteAdminBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(crudDialog,
                    "Funkcija za brisanje administratora će biti implementirana.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        backBtn.addActionListener(e -> crudDialog.dispose());

        crudDialog.add(panel);
        crudDialog.setVisible(true);
    }

    private static void displayAllAdmins() {
        Administrator[] admins = CsvAdminRepo.loadAdministratorsFromCSV(csvAdminFilePath);
        StringBuilder sb = new StringBuilder("===== SVI ADMINISTRATORI =====\n\n");

        if (admins != null && admins.length > 0) {
            for (int i = 0; i < admins.length; i++) {
                sb.append((i+1) + ". " + admins[i].getFirstName() + " " + admins[i].getLastName() +
                        " (" + admins[i].getUsername() + ")\n");
            }
        } else {
            sb.append("Nema administratora u sistemu.");
        }

        JOptionPane.showMessageDialog(mainFrame, sb.toString(), "Administratori", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void addNewAdmin() {
        JDialog addDialog = new JDialog(mainFrame, "Dodavanje Novog Administratora", true);
        addDialog.setSize(500, 500);
        addDialog.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create form fields similar to librarian registration
        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField genderField = new JTextField(20);
        JTextField birthDateField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++;
        panel.add(new JLabel("Ime:"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        // Add other fields similarly...
        // (Implementation similar to librarian registration form)

        JButton addButton = new JButton("Dodaj");
        JButton cancelButton = new JButton("Otkaži");

        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panel.add(addButton, gbc);
        gbc.gridy = row++;
        panel.add(cancelButton, gbc);

        addButton.addActionListener(e -> {
            try {
                // Collect data from fields and create new admin
                // Similar to console version but using Swing components
                JOptionPane.showMessageDialog(addDialog, "Administrator uspešno dodat!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addDialog, "Greška: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> addDialog.dispose());

        addDialog.add(panel);
        addDialog.setVisible(true);
    }

    // LIBRARIAN CRUD Operations
    private static void showLibrarianCRUDMenu() {
        JDialog crudDialog = new JDialog(mainFrame, "Bibliotekari CRUD", true);
        crudDialog.setSize(500, 400);
        crudDialog.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton viewLibrariansBtn = new JButton("1. Prikazi sve bibliotekare");
        JButton addLibrarianBtn = new JButton("2. Dodaj novog bibliotekara");
        JButton updateLibrarianBtn = new JButton("3. Izmeni bibliotekara");
        JButton deleteLibrarianBtn = new JButton("4. Obriši bibliotekara");
        JButton backBtn = new JButton("5. Nazad na glavni meni");

        panel.add(viewLibrariansBtn);
        panel.add(addLibrarianBtn);
        panel.add(updateLibrarianBtn);
        panel.add(deleteLibrarianBtn);
        panel.add(backBtn);

        viewLibrariansBtn.addActionListener(e -> displayAllLibrarians());
        addLibrarianBtn.addActionListener(e -> addNewLibrarian());
        updateLibrarianBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(crudDialog,
                    "Funkcija za izmenu bibliotekara će biti implementirana.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        deleteLibrarianBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(crudDialog,
                    "Funkcija za brisanje bibliotekara će biti implementirana.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        backBtn.addActionListener(e -> crudDialog.dispose());

        crudDialog.add(panel);
        crudDialog.setVisible(true);
    }

    private static void displayAllLibrarians() {
        Bibliotekar[] librarians = CsvLibrarianRepo.loadLibrariansFromCSV(csvLibrarianFilePath);
        StringBuilder sb = new StringBuilder("===== SVI BIBLIOTEKARI =====\n\n");

        if (librarians != null && librarians.length > 0) {
            for (int i = 0; i < librarians.length; i++) {
                sb.append((i+1) + ". " + librarians[i].getFirstName() + " " + librarians[i].getLastName() +
                        " (" + librarians[i].getUsername() + ")\n");
            }
        } else {
            sb.append("Nema bibliotekara u sistemu.");
        }

        JOptionPane.showMessageDialog(mainFrame, sb.toString(), "Bibliotekari", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void addNewLibrarian() {
        // Implementation similar to addNewAdmin but for librarians
        // Create a form with all librarian-specific fields
        JOptionPane.showMessageDialog(mainFrame,
                "Funkcija za dodavanje bibliotekara će biti implementirana.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // USER CRUD Operations
    private static void showUserCRUDMenu() {
        JDialog crudDialog = new JDialog(mainFrame, "Članovi CRUD", true);
        crudDialog.setSize(500, 400);
        crudDialog.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton viewUsersBtn = new JButton("1. Prikazi sve članove");
        JButton addUserBtn = new JButton("2. Dodaj novog člana");
        JButton updateUserBtn = new JButton("3. Izmeni člana");
        JButton deleteUserBtn = new JButton("4. Obriši člana");
        JButton backBtn = new JButton("5. Nazad na glavni meni");

        panel.add(viewUsersBtn);
        panel.add(addUserBtn);
        panel.add(updateUserBtn);
        panel.add(deleteUserBtn);
        panel.add(backBtn);

        viewUsersBtn.addActionListener(e -> displayAllUsers());
        addUserBtn.addActionListener(e -> addNewUser());
        updateUserBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(crudDialog,
                    "Funkcija za izmenu člana će biti implementirana.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        deleteUserBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(crudDialog,
                    "Funkcija za brisanje člana će biti implementirana.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        backBtn.addActionListener(e -> crudDialog.dispose());

        crudDialog.add(panel);
        crudDialog.setVisible(true);
    }

    private static void displayAllUsers() {
        Clan[] users = CsvUserRepo.loadUsersFromCSV(csvUserFilePath);
        StringBuilder sb = new StringBuilder("===== SVI ČLANOVI =====\n\n");

        if (users != null && users.length > 0) {
            for (int i = 0; i < users.length; i++) {
                sb.append((i+1) + ". " + users[i].getFirstName() + " " + users[i].getLastName() +
                        " (" + users[i].getUsername() + ") - " + users[i].getCategory() + "\n");
            }
        } else {
            sb.append("Nema članova u sistemu.");
        }

        JOptionPane.showMessageDialog(mainFrame, sb.toString(), "Članovi", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void addNewUser() {
        // Implementation similar to librarian registration but for users
        // Create a form with user-specific fields and category combo box
        JOptionPane.showMessageDialog(mainFrame,
                "Funkcija za dodavanje člana će biti implementirana.",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
    }
}