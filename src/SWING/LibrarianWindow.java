package SWING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import Entities.Bibliotekar;
import SWING.LibrarianDialog.MemberRegistrationDialog;

public class LibrarianWindow {
    private static Bibliotekar[] librarians;
    private static String csvUserFilePath;
    private static JFrame mainFrame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static Bibliotekar currentLibrarian;

    public static void initializeLibrarianUI(Bibliotekar[] librariansArray, String csvPath) {
        librarians = librariansArray;
        csvUserFilePath = csvPath;

        mainFrame = new JFrame("Biblioteka - Librarian Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createLibrarianMenuPanel();

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
        JLabel titleLabel = new JLabel("Librarian Login");
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

            Bibliotekar librarian = authenticateLibrarian(username, password, librarians);
            if (librarian != null) {
                currentLibrarian = librarian;
                userField.setText("");
                passField.setText("");
                cardLayout.show(mainPanel, "librarianMenu");
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

    private static void createLibrarianMenuPanel() {
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
        JButton manageLoansBtn = new JButton("1. Upravljaj izdavanjem knjiga");
        JButton manageReturnsBtn = new JButton("2. Upravljaj vraćanjem knjiga");
        JButton manageReservationsBtn = new JButton("3. Upravljaj rezervacijama");
        JButton viewDailyBtn = new JButton("4. Pregled dnevnih izdavanja");
        JButton viewBooksBtn = new JButton("5. Pregled dostupnih knjiga");
        JButton registerMemberBtn = new JButton("6. Učlanjivanje novih članova");
        JButton manageRenewalBtn = new JButton("7. Produžetak članarine");
        JButton logoutBtn = new JButton("8. Logout");

        // Style buttons
        manageLoansBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageLoansBtn.setBackground(new Color(70, 130, 180));
        manageLoansBtn.setForeground(Color.WHITE);
        manageLoansBtn.setFocusPainted(false);

        manageReturnsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageReturnsBtn.setBackground(new Color(70, 130, 180));
        manageReturnsBtn.setForeground(Color.WHITE);
        manageReturnsBtn.setFocusPainted(false);

        manageReservationsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageReservationsBtn.setBackground(new Color(70, 130, 180));
        manageReservationsBtn.setForeground(Color.WHITE);
        manageReservationsBtn.setFocusPainted(false);

        viewDailyBtn.setFont(new Font("Arial", Font.BOLD, 14));
        viewDailyBtn.setBackground(new Color(70, 130, 180));
        viewDailyBtn.setForeground(Color.WHITE);
        viewDailyBtn.setFocusPainted(false);

        viewBooksBtn.setFont(new Font("Arial", Font.BOLD, 14));
        viewBooksBtn.setBackground(new Color(70, 130, 180));
        viewBooksBtn.setForeground(Color.WHITE);
        viewBooksBtn.setFocusPainted(false);

        registerMemberBtn.setFont(new Font("Arial", Font.BOLD, 14));
        registerMemberBtn.setBackground(new Color(70, 130, 180));
        registerMemberBtn.setForeground(Color.WHITE);
        registerMemberBtn.setFocusPainted(false);

        manageRenewalBtn.setFont(new Font("Arial", Font.BOLD, 14));
        manageRenewalBtn.setBackground(new Color(70, 130, 180));
        manageRenewalBtn.setForeground(Color.WHITE);
        manageRenewalBtn.setFocusPainted(false);

        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.setBackground(new Color(200, 200, 200));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        menuPanel.add(welcomeLabel, gbc);

        gbc.gridwidth = 2; gbc.gridy = 1;
        menuPanel.add(manageLoansBtn, gbc);

        gbc.gridy = 2;
        menuPanel.add(manageReturnsBtn, gbc);

        gbc.gridy = 3;
        menuPanel.add(manageReservationsBtn, gbc);

        gbc.gridy = 4;
        menuPanel.add(viewDailyBtn, gbc);

        gbc.gridy = 5;
        menuPanel.add(viewBooksBtn, gbc);

        gbc.gridy = 6;
        menuPanel.add(registerMemberBtn, gbc);

        gbc.gridy = 7;
        menuPanel.add(manageRenewalBtn, gbc);

        gbc.gridy = 8;
        menuPanel.add(logoutBtn, gbc);

        // Button actions
        manageLoansBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje izdavanjem knjiga...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        manageReturnsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje vraćanjem knjiga...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        manageReservationsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje rezervacijama...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        viewDailyBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Pregled dnevnih izdavanja i rezervacija...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        viewBooksBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Pregled dostupnih i izdatih knjiga...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        registerMemberBtn.addActionListener(e -> {
            registerNewMember();
        });

        manageRenewalBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Upravljanje produžetkom članarine...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        logoutBtn.addActionListener(e -> {
            currentLibrarian = null;
            cardLayout.show(mainPanel, "login");
        });

        // Update welcome label when panel is shown
        menuPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                welcomeLabel.setText("Welcome: " + currentLibrarian.getFirstName() + " " + currentLibrarian.getLastName());
            }
        });

        mainPanel.add(menuPanel, "librarianMenu");
    }

    private static Bibliotekar authenticateLibrarian(String username, String password, Bibliotekar[] librarians) {
        for (Bibliotekar librarian : librarians) {
            if (librarian != null && librarian.getUsername().equals(username) && librarian.getPassword().equals(password)) {
                return librarian;
            }
        }
        return null;
    }

    private static void registerNewMember() {
        MemberRegistrationDialog registrationDialog =
                new MemberRegistrationDialog(mainFrame, csvUserFilePath);
        registrationDialog.show();
    }
}