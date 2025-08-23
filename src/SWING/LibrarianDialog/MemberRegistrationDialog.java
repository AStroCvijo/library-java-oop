package SWING.LibrarianDialog;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import Entities.Clan;
import Enums.KategorijaClana;
import Enums.Pol;
import FileManager.CsvUserRepo;

public class MemberRegistrationDialog {
    private JDialog registerDialog;
    private String csvUserFilePath;
    private JFrame parentFrame;

    public MemberRegistrationDialog(JFrame parent, String csvFilePath) {
        this.parentFrame = parent;
        this.csvUserFilePath = csvFilePath;
        initializeDialog();
    }

    private void initializeDialog() {
        registerDialog = new JDialog(parentFrame, "Registracija Novog Člana", true);
        registerDialog.setSize(500, 600);
        registerDialog.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Registracija Novog Člana");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(50, 50, 50));

        // Input fields
        JTextField firstNameField = new JTextField(20);
        firstNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        firstNameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JTextField lastNameField = new JTextField(20);
        lastNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        lastNameField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JComboBox<Pol> genderCombo = new JComboBox<>(Pol.values());
        genderCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        JTextField birthDateField = new JTextField(20);
        birthDateField.setFont(new Font("Arial", Font.PLAIN, 14));
        birthDateField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JTextField phoneField = new JTextField(20);
        phoneField.setFont(new Font("Arial", Font.PLAIN, 14));
        phoneField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JTextField addressField = new JTextField(20);
        addressField.setFont(new Font("Arial", Font.PLAIN, 14));
        addressField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JComboBox<KategorijaClana> categoryCombo = new JComboBox<>(KategorijaClana.values());
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));

        // Buttons
        JButton registerButton = new JButton("Registruj");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);

        JButton cancelButton = new JButton("Otkaži");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 12));
        cancelButton.setBackground(new Color(200, 200, 200));

        // Layout
        int row = 0;
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ime:"), gbc);
        gbc.gridx = 1;
        panel.add(firstNameField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Prezime:"), gbc);
        gbc.gridx = 1;
        panel.add(lastNameField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("POL:"), gbc);
        gbc.gridx = 1;
        panel.add(genderCombo, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Datum rođenja (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        panel.add(birthDateField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Telefon:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Adresa:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Email (korisničko ime):"), gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Šifra:"), gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Kategorija člana:"), gbc);
        gbc.gridx = 1;
        panel.add(categoryCombo, gbc);

        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        gbc.gridy = ++row;
        panel.add(cancelButton, gbc);

        registerButton.addActionListener(e -> {
            try {
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                Pol gender = (Pol) genderCombo.getSelectedItem();
                LocalDate birthDate = LocalDate.parse(birthDateField.getText().trim());
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                KategorijaClana category = (KategorijaClana) categoryCombo.getSelectedItem();

                if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() ||
                        address.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(registerDialog,
                            "Sva polja moraju biti popunjena!",
                            "Greška",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Clan newMember = new Clan(firstName, lastName, gender, birthDate, phone, address,
                        email, password, category);

                boolean success = CsvUserRepo.addUserToCSV(csvUserFilePath, newMember);

                if (success) {
                    JOptionPane.showMessageDialog(registerDialog,
                            "Član je uspešno registrovan!\nKorisničko ime: " + email,
                            "Uspeh",
                            JOptionPane.INFORMATION_MESSAGE);
                    registerDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(registerDialog,
                            "Došlo je do greške pri registraciji člana.",
                            "Greška",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Neispravan format datuma. Koristite YYYY-MM-DD.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(registerDialog,
                        "Greška pri unosu podataka: " + ex.getMessage(),
                        "Greška",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> registerDialog.dispose());

        registerDialog.add(panel);
    }

    public void show() {
        registerDialog.setVisible(true);
    }
}