package gui;

import javax.swing.*;
import java.awt.*;
import model.theme.UITheme;

public class LoginGUI extends JFrame {
    private LibrarySystemGUI mainGUI;

    public LoginGUI(LibrarySystemGUI mainGUI) {
        this.mainGUI = mainGUI;
        setTitle("Library Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        UITheme.applyTheme(this);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);

        // Header
        mainPanel.add(UITheme.createHeaderPanel("User Login"), BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(UITheme.BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 5, 10, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(UITheme.BOLD_FONT);
        userLabel.setForeground(UITheme.FONT_COLOR);
        userLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JTextField userText = new JTextField(20);
        userText.setFont(UITheme.MAIN_FONT);
        userText.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(userText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.BOLD_FONT);
        passLabel.setForeground(UITheme.FONT_COLOR);
        passLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        JPasswordField passText = new JPasswordField(20);
        passText.setFont(UITheme.MAIN_FONT);
        passText.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(passText, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(20, 5, 10, 5);
        JButton loginButton = new JButton("Login");
        UITheme.styleButton(loginButton);
        loginButton.setPreferredSize(new Dimension(120, 40));
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Action Listener
        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passText.getPassword());
            mainGUI.login(username, password);
        });

        add(mainPanel);
    }
}
