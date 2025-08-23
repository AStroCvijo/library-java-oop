package SWING;

import javax.swing.*;
import java.awt.*;
import Entities.*;
import FileManager.CsvAdminRepo;
import FileManager.CsvLibrarianRepo;
import FileManager.CsvUserRepo;

public class LibraryMainWindow {
    private static final String CSV_ADMIN_FILE_PATH = "Data/administrator.csv";
    private static final String CSV_LIBRARIAN_FILE_PATH = "Data/bibliotekar.csv";
    private static final String CSV_USER_FILE_PATH = "Data/clan.csv";

    private static Administrator[] administrators;
    private static Bibliotekar[] librarians;
    private static Clan[] users;
    private static JFrame mainFrame;

    public static void main(String[] args) {
        // Load data from CSV files
        administrators = CsvAdminRepo.loadAdministratorsFromCSV(CSV_ADMIN_FILE_PATH);
        librarians = CsvLibrarianRepo.loadLibrariansFromCSV(CSV_LIBRARIAN_FILE_PATH);
        users = CsvUserRepo.loadUsersFromCSV(CSV_USER_FILE_PATH);

        // Check if any data was loaded
        checkLoadedData();

        // Initialize Swing UI
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void checkLoadedData() {
        StringBuilder message = new StringBuilder();

        if (administrators == null || administrators.length == 0) {
            message.append("No administrators loaded from CSV.\n");
        }
        if (librarians == null || librarians.length == 0) {
            message.append("No librarians loaded from CSV.\n");
        }
        if (users == null || users.length == 0) {
            message.append("No users loaded from CSV.\n");
        }

        if (message.length() > 0) {
            JOptionPane.showMessageDialog(null,
                    message.toString(),
                    "Data Loading Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void createAndShowGUI() {
        mainFrame = new JFrame("Biblioteka - Main Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Library System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(50, 50, 50));

        // Buttons
        JButton adminButton = new JButton("1. Administrator Login");
        JButton librarianButton = new JButton("2. Bibliotekar Login");
        JButton userButton = new JButton("3. Član Login");
        JButton exitButton = new JButton("4. Exit");

        // Style buttons
        adminButton.setFont(new Font("Arial", Font.BOLD, 14));
        adminButton.setBackground(new Color(70, 130, 180));
        adminButton.setForeground(Color.WHITE);
        adminButton.setFocusPainted(false);

        librarianButton.setFont(new Font("Arial", Font.BOLD, 14));
        librarianButton.setBackground(new Color(70, 130, 180));
        librarianButton.setForeground(Color.WHITE);
        librarianButton.setFocusPainted(false);

        userButton.setFont(new Font("Arial", Font.BOLD, 14));
        userButton.setBackground(new Color(70, 130, 180));
        userButton.setForeground(Color.WHITE);
        userButton.setFocusPainted(false);

        exitButton.setFont(new Font("Arial", Font.PLAIN, 12));
        exitButton.setBackground(new Color(200, 200, 200));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 2; gbc.gridy = 1;
        mainPanel.add(adminButton, gbc);

        gbc.gridy = 2;
        mainPanel.add(librarianButton, gbc);

        gbc.gridy = 3;
        mainPanel.add(userButton, gbc);

        gbc.gridy = 4;
        mainPanel.add(exitButton, gbc);

        // Button actions
        adminButton.addActionListener(e -> {
            mainFrame.setVisible(false);
            AdminWindow.initializeAdminUI(administrators, CSV_ADMIN_FILE_PATH, CSV_LIBRARIAN_FILE_PATH, CSV_USER_FILE_PATH);
        });

        librarianButton.addActionListener(e -> {
            mainFrame.setVisible(false);
            LibrarianWindow.initializeLibrarianUI(librarians, CSV_USER_FILE_PATH);
        });

        userButton.addActionListener(e -> {
            mainFrame.setVisible(false);
            UserWindow.initializeUserUI(users, CSV_USER_FILE_PATH);
        });

        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(mainFrame,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }
}