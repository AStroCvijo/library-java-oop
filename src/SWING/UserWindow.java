package SWING;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Entities.Clan;

public class UserWindow {
    private static Clan[] users;
    private static String csvUserFilePath;
    private static JFrame mainFrame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static Clan currentUser;

    public static void initializeUserUI(Clan[] usersArray, String csvPath) {
        users = usersArray;
        csvUserFilePath = csvPath;

        mainFrame = new JFrame("Biblioteka - User Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 600);
        mainFrame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        createLoginPanel();
        createUserMenuPanel();

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
        JLabel titleLabel = new JLabel("Member Login");
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

            Clan user = authenticateUser(username, password, users);
            if (user != null) {
                currentUser = user;
                userField.setText("");
                passField.setText("");
                cardLayout.show(mainPanel, "userMenu");
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

    private static void createUserMenuPanel() {
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
        JButton viewBooksBtn = new JButton("1. Pregled dostupnih knjiga");
        JButton reserveBookBtn = new JButton("2. Zahtev za rezervaciju knjige");
        JButton viewReservationsBtn = new JButton("3. Pregled mojih rezervacija");
        JButton cancelReservationBtn = new JButton("4. Otkazivanje rezervacije");
        JButton renewMembershipBtn = new JButton("5. Produžetak članarine");
        JButton logoutBtn = new JButton("6. Logout");

        // Style buttons
        viewBooksBtn.setFont(new Font("Arial", Font.BOLD, 14));
        viewBooksBtn.setBackground(new Color(70, 130, 180));
        viewBooksBtn.setForeground(Color.WHITE);
        viewBooksBtn.setFocusPainted(false);

        reserveBookBtn.setFont(new Font("Arial", Font.BOLD, 14));
        reserveBookBtn.setBackground(new Color(70, 130, 180));
        reserveBookBtn.setForeground(Color.WHITE);
        reserveBookBtn.setFocusPainted(false);

        viewReservationsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        viewReservationsBtn.setBackground(new Color(70, 130, 180));
        viewReservationsBtn.setForeground(Color.WHITE);
        viewReservationsBtn.setFocusPainted(false);

        cancelReservationBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelReservationBtn.setBackground(new Color(70, 130, 180));
        cancelReservationBtn.setForeground(Color.WHITE);
        cancelReservationBtn.setFocusPainted(false);

        renewMembershipBtn.setFont(new Font("Arial", Font.BOLD, 14));
        renewMembershipBtn.setBackground(new Color(70, 130, 180));
        renewMembershipBtn.setForeground(Color.WHITE);
        renewMembershipBtn.setFocusPainted(false);

        logoutBtn.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutBtn.setBackground(new Color(200, 200, 200));

        // Layout
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        menuPanel.add(welcomeLabel, gbc);

        gbc.gridwidth = 2; gbc.gridy = 1;
        menuPanel.add(viewBooksBtn, gbc);

        gbc.gridy = 2;
        menuPanel.add(reserveBookBtn, gbc);

        gbc.gridy = 3;
        menuPanel.add(viewReservationsBtn, gbc);

        gbc.gridy = 4;
        menuPanel.add(cancelReservationBtn, gbc);

        gbc.gridy = 5;
        menuPanel.add(renewMembershipBtn, gbc);

        gbc.gridy = 6;
        menuPanel.add(logoutBtn, gbc);

        // Button actions
        viewBooksBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Pregled dostupnih knjiga...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        reserveBookBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Zahtev za rezervaciju knjige...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        viewReservationsBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Pregled mojih rezervacija...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        cancelReservationBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Otkazivanje rezervacije...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        renewMembershipBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Zahtev za produžetak članarine...",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        logoutBtn.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(mainPanel, "login");
        });

        // Update welcome label when panel is shown
        menuPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                welcomeLabel.setText("Welcome: " + currentUser.getFirstName() + " " + currentUser.getLastName());
            }
        });

        mainPanel.add(menuPanel, "userMenu");
    }

    private static Clan authenticateUser(String username, String password, Clan[] users) {
        for (Clan user : users) {
            if (user != null && user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}