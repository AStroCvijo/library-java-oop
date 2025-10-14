import model.entities.*;
import model.enums.*;
import model.managers.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class LibrarySystemGUI extends JFrame {
    private EmployeeManager employeeManager;
    private MemberManager memberManager;
    private BookManager bookManager;
    private GenreManager genreManager;
    private ReservationManager reservationManager;
    private PriceListManager priceListManager;
    private MembershipManager membershipManager;

    private User currentUser;

    public LibrarySystemGUI() {
        setTitle("Biblioteka");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        initializeManagers();
        showLoginScreen();
    }

    private void initializeManagers() {
        employeeManager = new EmployeeManager("data/employees.csv");
        memberManager = new MemberManager("data/members.csv");
        bookManager = new BookManager("data/books.csv");
        genreManager = new GenreManager("data/genres.csv");
        reservationManager = new ReservationManager("data/reservations.csv");
        priceListManager = new PriceListManager("data/pricelist.csv");
        membershipManager = new MembershipManager("data/memberships.csv");
    }

    private void showLoginScreen() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Bibliotečni Sistem");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel usernameLabel = new JLabel("Korisničko ime:");
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Lozinka:");
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Prijava");
        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = authenticateUser(username, password);
            if (user != null) {
                currentUser = user;
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Neispravni podaci za prijavu",
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        getContentPane().removeAll();
        getContentPane().add(loginPanel);
        revalidate();
        repaint();
    }

    private User authenticateUser(String username, String password) {
        for (Employee emp : employeeManager.getAll()) {
            if (emp.getUsername().equals(username) && emp.getPassword().equals(password)) {
                return new User(emp.getId(), emp.getFirstName(), emp.getLastName(),
                        UserType.EMPLOYEE, emp.getRole());
            }
        }

        for (Member mem : memberManager.getAll()) {
            if (mem.getUsername().equals(username) && mem.getPassword().equals(password)) {
                return new User(mem.getId(), mem.getFirstName(), mem.getLastName(),
                        UserType.MEMBER, null);
            }
        }

        return null;
    }

    private void showMainMenu() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 60, 120));
        headerPanel.setForeground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        JLabel welcomeLabel = new JLabel("Dobrodošli, " + currentUser.firstName + " " +
                currentUser.lastName + " (" + currentUser.type + ")");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel);

        JButton logoutButton = new JButton("Odjava");
        logoutButton.addActionListener(e -> {
            currentUser = null;
            showLoginScreen();
        });
        headerPanel.add(Box.createHorizontalGlue());
        headerPanel.add(logoutButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center panel sa opcijama
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        if (currentUser.type == UserType.EMPLOYEE && currentUser.role == EmployeeRole.ADMINISTRATOR) {
            showAdminMenu(centerPanel);
        } else if (currentUser.type == UserType.EMPLOYEE && currentUser.role == EmployeeRole.LIBRARIAN) {
            showLibrarianMenu(centerPanel);
        } else if (currentUser.type == UserType.MEMBER) {
            showMemberMenu(centerPanel);
        }

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    private void showAdminMenu(JPanel centerPanel) {
        centerPanel.add(createMenuButton("Upravljanje Zaposlenima", e -> showEmployeeManagement()));
        centerPanel.add(createMenuButton("Upravljanje Članovima", e -> showMemberManagement()));
        centerPanel.add(createMenuButton("Upravljanje Knjigama", e -> showBookManagement()));
        centerPanel.add(createMenuButton("Upravljanje Žanrovima", e -> showGenreManagement()));
        centerPanel.add(createMenuButton("Upravljanje Rezervacijama", e -> showReservationManagement()));
        centerPanel.add(createMenuButton("Upravljanje Cenovnikom", e -> showPriceListManagement()));
        centerPanel.add(createMenuButton("Upravljanje Članarinama", e -> showMembershipManagement()));
        centerPanel.add(createMenuButton("Izveštaji i Analitika", e -> showReports()));
        centerPanel.add(createMenuButton("Podešavanja Sistema", e -> showSettings()));
    }

    private void showLibrarianMenu(JPanel centerPanel) {
        centerPanel.add(createMenuButton("Izdavanje Knjiga", e -> showBookIssuing()));
        centerPanel.add(createMenuButton("Upravljanje Rezervacijama", e -> showReservationManagement()));
        centerPanel.add(createMenuButton("Dodavanje Novih Članova", e -> showMemberRegistration()));
        centerPanel.add(createMenuButton("Prikaz Dostupnih Knjiga", e -> showAvailableBooks()));
        centerPanel.add(createMenuButton("Pregled Članarina", e -> showMembershipManagement()));
        centerPanel.add(createMenuButton("Dnevni Izveštaj", e -> showDailyReport()));
    }

    private void showMemberMenu(JPanel centerPanel) {
        centerPanel.add(createMenuButton("Pregled Dostupnih Knjiga", e -> showMemberAvailableBooks()));
        centerPanel.add(createMenuButton("Kreiraj Rezervaciju", e -> showMemberReservation()));
        centerPanel.add(createMenuButton("Moje Rezervacije", e -> showMyReservations()));
        centerPanel.add(createMenuButton("Moj Profil", e -> showMemberProfile()));
        centerPanel.add(createMenuButton("Istorija Iznajmljivanja", e -> showRentalHistory()));
        centerPanel.add(createMenuButton("Obnova Članarine", e -> showMembershipRenewal()));
    }

    private JButton createMenuButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    // ==================== ADMIN METHODS ====================
    private void showEmployeeManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Zaposlenog");
        JButton editButton = new JButton("Uredi Zaposlenog");
        JButton deleteButton = new JButton("Obriši Zaposlenog");
        JButton backButton = new JButton("Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Ime", "Prezime", "Pozicija", "Plata", "Stež"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Employee emp : employeeManager.getAll()) {
            model.addRow(new Object[]{
                    emp.getId(), emp.getFirstName(), emp.getLastName(),
                    emp.getRole(), emp.calculateSalary(), emp.getYearsOfExperience()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add button action
        addButton.addActionListener(e -> showEmployeeDialog(null));

        // Edit button action
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Employee emp = employeeManager.getById(id);
                showEmployeeDialog(emp);
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite zaposlenog za izmenu.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Da li ste sigurni da želite da obrišete zaposlenog?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    employeeManager.delete(id);
                    showEmployeeManagement(); // Refresh
                }
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite zaposlenog za brisanje.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Upravljanje Zaposlenima");
    }

    private void showEmployeeDialog(Employee employee) {
        JDialog dialog = new JDialog(this, employee == null ? "Dodaj Zaposlenog" : "Uredi Zaposlenog", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField firstNameField = new JTextField(employee != null ? employee.getFirstName() : "", 20);
        JTextField lastNameField = new JTextField(employee != null ? employee.getLastName() : "", 20);
        JComboBox<Gender> genderCombo = new JComboBox<>(Gender.values());
        JTextField birthDateField = new JTextField(employee != null ? employee.getBirthDate().toString() : "YYYY-MM-DD", 20);
        JTextField phoneField = new JTextField(employee != null ? employee.getPhone() : "", 20);
        JTextField addressField = new JTextField(employee != null ? employee.getAddress() : "", 20);
        JTextField usernameField = new JTextField(employee != null ? employee.getUsername() : "", 20);
        JPasswordField passwordField = new JPasswordField(employee != null ? employee.getPassword() : "", 20);
        JComboBox<EmployeeEducationLevel> educationCombo = new JComboBox<>(EmployeeEducationLevel.values());
        JSpinner yearsOfExperienceSpinner = new JSpinner(new SpinnerNumberModel(employee != null ? employee.getYearsOfExperience() : 0, 0, 50, 1));
        JTextField salaryField = new JTextField(employee != null ? String.valueOf(employee.getBaseSalary()) : "", 20);
        JComboBox<EmployeeRole> roleCombo = new JComboBox<>(EmployeeRole.values());

        if (employee != null) {
            genderCombo.setSelectedItem(employee.getGender());
            educationCombo.setSelectedItem(employee.getEducationLevel());
            roleCombo.setSelectedItem(employee.getRole());
        }

        panel.add(new JLabel("Ime:"));
        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Prezime:"));
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Pol:"));
        panel.add(genderCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Datum rođenja (YYYY-MM-DD):"));
        panel.add(birthDateField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Telefon:"));
        panel.add(phoneField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Adresa:"));
        panel.add(addressField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Korisničko ime:"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Lozinka:"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Obrazovanje:"));
        panel.add(educationCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Godine iskustva:"));
        panel.add(yearsOfExperienceSpinner);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Osnovna plata:"));
        panel.add(salaryField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Pozicija:"));
        panel.add(roleCombo);

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(e -> {
            try {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                Gender gender = (Gender) genderCombo.getSelectedItem();
                LocalDate birthDate = LocalDate.parse(birthDateField.getText());
                String phone = phoneField.getText();
                String address = addressField.getText();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                EmployeeEducationLevel education = (EmployeeEducationLevel) educationCombo.getSelectedItem();
                int yearsOfExperience = (int) yearsOfExperienceSpinner.getValue();
                double baseSalary = Double.parseDouble(salaryField.getText());
                EmployeeRole role = (EmployeeRole) roleCombo.getSelectedItem();

                Employee newEmployee;
                if (employee == null) {
                    newEmployee = new Employee(
                            employeeManager.getNextId(), firstName, lastName, gender, birthDate, phone, address,
                            username, password, education, yearsOfExperience, baseSalary, role
                    );
                    employeeManager.add(newEmployee);
                    JOptionPane.showMessageDialog(dialog, "Zaposleni je uspešno dodat!");
                } else {
                    newEmployee = new Employee(
                            employee.getId(), firstName, lastName, gender, birthDate, phone, address,
                            username, password, education, yearsOfExperience, baseSalary, role
                    );
                    employeeManager.update(newEmployee);
                    JOptionPane.showMessageDialog(dialog, "Zaposleni je uspešno ažuriran!");
                }

                dialog.dispose();
                showEmployeeManagement(); // Refresh prikaza
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Greška pri unosu podataka: " + ex.getMessage(),
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showBookManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Knjigu");
        JButton editButton = new JButton("Uredi Knjigu");
        JButton deleteButton = new JButton("Obriši Knjigu");
        JButton backButton = new JButton("Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Naslov", "Autor", "Žanr", "Status", "Godište", "Dostupno"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Book book : bookManager.getAll()) {
            Genre genre = genreManager.getById(book.getGenreId());
            model.addRow(new Object[]{
                    book.getId(), book.getTitle(), book.getAuthor(),
                    genre != null ? genre.getName() : "N/A", book.getStatus(),
                    book.getPublicationYear(), book.getAvailableCopies()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showBookDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Book book = bookManager.getById(id);
                showBookDialog(book);
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite knjigu za izmenu.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Da li ste sigurni da želite da obrišete knjigu?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    bookManager.delete(id);
                    showBookManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite knjigu za brisanje.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Upravljanje Knjigama");
    }

    private void showBookDialog(Book book) {
        JDialog dialog = new JDialog(this, book == null ? "Dodaj Knjigu" : "Uredi Knjigu", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField titleField = new JTextField(book != null ? book.getTitle() : "", 20);
        JTextField authorField = new JTextField(book != null ? book.getAuthor() : "", 20);
        JTextField isbnField = new JTextField(book != null ? book.getIsbn() : "", 20);
        JTextField yearField = new JTextField(book != null ? String.valueOf(book.getPublicationYear()) : "", 20);
        JSpinner copiesSpinner = new JSpinner(new SpinnerNumberModel(book != null ? book.getAvailableCopies() : 1, 1, 100, 1));

        // Genre combobox
        JComboBox<Genre> genreCombo = new JComboBox<>();
        for (Genre genre : genreManager.getAll()) {
            genreCombo.addItem(genre);
        }

        JComboBox<BookStatus> statusCombo = new JComboBox<>(BookStatus.values());

        if (book != null) {
            genreCombo.setSelectedItem(genreManager.getById(book.getGenreId()));
            statusCombo.setSelectedItem(book.getStatus());
        }

        panel.add(new JLabel("Naslov:"));
        panel.add(titleField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Godina izdanja:"));
        panel.add(yearField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Žanr:"));
        panel.add(genreCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Broj primeraka:"));
        panel.add(copiesSpinner);

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText();
                String author = authorField.getText();
                String isbn = isbnField.getText();
                int publicationYear = Integer.parseInt(yearField.getText());
                Genre selectedGenre = (Genre) genreCombo.getSelectedItem();
                BookStatus status = (BookStatus) statusCombo.getSelectedItem();
                int copies = (int) copiesSpinner.getValue();


                // potencijalno izmjeniti
                Book newBook;
                if (book == null) {
                    newBook = new Book(
                            bookManager.getNextId(), title, author, isbn, publicationYear,
                            selectedGenre != null ? selectedGenre.getId() : 0, copies, copies, status
                    );
                    bookManager.add(newBook);
                    JOptionPane.showMessageDialog(dialog, "Knjiga je uspešno dodata!");
                } else {

                    if (copies == book.getAvailableCopies()) {
                        newBook = new Book(
                                book.getId(), title, author, isbn, publicationYear,
                                selectedGenre != null ? selectedGenre.getId() : 0, copies, book.getAvailableCopies(), status
                        );
                        bookManager.update(newBook);
                        JOptionPane.showMessageDialog(dialog, "Knjiga je uspešno ažurirana!");
                    } else if (copies > book.getAvailableCopies()) {
                        newBook = new Book(
                                book.getId(), title, author, isbn, publicationYear,
                                selectedGenre != null ? selectedGenre.getId() : 0, copies, book.getAvailableCopies()+1, status
                        );
                        bookManager.update(newBook);
                        JOptionPane.showMessageDialog(dialog, "Knjiga je uspešno ažurirana!");
                    } else if (copies < book.getAvailableCopies()) {
                        newBook = new Book(
                                book.getId(), title, author, isbn, publicationYear,
                                selectedGenre != null ? selectedGenre.getId() : 0, copies, book.getAvailableCopies()-1, status
                        );
                        bookManager.update(newBook);
                        JOptionPane.showMessageDialog(dialog, "Knjiga je uspešno ažurirana!");
                    }
                }

                dialog.dispose();
                showBookManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Greška pri unosu podataka: " + ex.getMessage(),
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showPriceListManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Stavku");
        JButton editButton = new JButton("Uredi Stavku");
        JButton deleteButton = new JButton("Obriši Stavku");
        JButton backButton = new JButton("Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Tip", "Opis", "Cena", "Od", "Do"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (PriceListItem item : priceListManager.getAll()) {
            model.addRow(new Object[]{
                    item.getId(), item.getType(), item.getDescription(),
                    item.getPrice(), item.getValidFrom(), item.getValidTo()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showPriceListItemDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                PriceListItem item = priceListManager.getById(id);
                showPriceListItemDialog(item);
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite stavku za izmenu.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Da li ste sigurni da želite da obrišete stavku?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    priceListManager.delete(id);
                    showPriceListManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite stavku za brisanje.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Upravljanje Cenovnikom");
    }

    private void showPriceListItemDialog(PriceListItem item) {
        JDialog dialog = new JDialog(this, item == null ? "Dodaj Stavku" : "Uredi Stavku", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JComboBox<PriceListItemType> typeCombo = new JComboBox<>(PriceListItemType.values());
        JTextField descriptionField = new JTextField(item != null ? item.getDescription() : "", 20);
        JTextField priceField = new JTextField(item != null ? String.valueOf(item.getPrice()) : "", 20);
        JTextField validFromField = new JTextField(item != null ? item.getValidFrom().toString() : "YYYY-MM-DD", 20);
        JTextField validToField = new JTextField(item != null ? item.getValidTo().toString() : "YYYY-MM-DD", 20);

        if (item != null) {
            typeCombo.setSelectedItem(item.getType());
        }

        panel.add(new JLabel("Tip stavke:"));
        panel.add(typeCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Opis:"));
        panel.add(descriptionField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Cena:"));
        panel.add(priceField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Važi od (YYYY-MM-DD):"));
        panel.add(validFromField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Važi do (YYYY-MM-DD):"));
        panel.add(validToField);

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(e -> {
            try {
                PriceListItemType type = (PriceListItemType) typeCombo.getSelectedItem();
                String description = descriptionField.getText();
                double price = Double.parseDouble(priceField.getText());
                LocalDate validFrom = LocalDate.parse(validFromField.getText());
                LocalDate validTo = LocalDate.parse(validToField.getText());

                PriceListItem newItem;
                if (item == null) {
                    newItem = new PriceListItem(
                            priceListManager.getNextId(), type, description, price, validFrom, validTo
                    );
                    priceListManager.add(newItem);
                    JOptionPane.showMessageDialog(dialog, "Stavka je uspešno dodata!");
                } else {
                    newItem = new PriceListItem(
                            item.getId(), type, description, price, validFrom, validTo
                    );
                    priceListManager.update(newItem);
                    JOptionPane.showMessageDialog(dialog, "Stavka je uspešno ažurirana!");
                }

                dialog.dispose();
                showPriceListManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Greška pri unosu podataka: " + ex.getMessage(),
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showGenreManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Žanr");
        JButton editButton = new JButton("Uredi Žanr");
        JButton deleteButton = new JButton("Obriši Žanr");
        JButton backButton = new JButton("Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Naziv", "Opis"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Genre genre : genreManager.getAll()) {
            model.addRow(new Object[]{
                    genre.getId(), genre.getName(), genre.getDescription()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showGenreDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Genre genre = genreManager.getById(id);
                showGenreDialog(genre);
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite žanr za izmenu.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Da li ste sigurni da želite da obrišete žanr?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    genreManager.delete(id);
                    showGenreManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite žanr za brisanje.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Upravljanje Žanrovima");
    }

    private void showGenreDialog(Genre genre) {
        JDialog dialog = new JDialog(this, genre == null ? "Dodaj Žanr" : "Uredi Žanr", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(genre != null ? genre.getName() : "", 20);
        JTextField descriptionField = new JTextField(genre != null ? genre.getDescription() : "", 20);

        panel.add(new JLabel("Naziv:"));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Opis:"));
        panel.add(descriptionField);

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();

                Genre newGenre;
                if (genre == null) {
                    newGenre = new Genre(genreManager.getNextId(), name, description);
                    genreManager.add(newGenre);
                    JOptionPane.showMessageDialog(dialog, "Žanr je uspešno dodat!");
                } else {
                    newGenre = new Genre(genre.getId(), name, description);
                    genreManager.update(newGenre);
                    JOptionPane.showMessageDialog(dialog, "Žanr je uspešno ažuriran!");
                }

                dialog.dispose();
                showGenreManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Greška pri unosu podataka: " + ex.getMessage(),
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }


    private void showReports() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(createMenuButton("Prihodi i Rashodi", e -> {}));
        panel.add(createMenuButton("Statistika Bibliotekara", e -> {}));
        panel.add(createMenuButton("Statistika Knjiga", e -> {}));
        panel.add(createMenuButton("Prihodi po Kategorijama", e -> {}));

        showPanel(panel, "Izveštaji i Analitika");
    }

    private void showSettings() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Podešavanje vremena trajanja najma knjige (dani):");
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(7, 1, 30, 1));

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(spinner);
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JButton("Sačuvaj"));

        showPanel(panel, "Podešavanja Sistema");
    }

    private void showMemberManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj člana");
        JButton editButton = new JButton("Uredi člana");
        JButton deleteButton = new JButton("Obriši člana");
        JButton backButton = new JButton("Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Ime", "Prezime", "Email", "Kategorija", "Telefon"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Member mem : memberManager.getAll()) {
            model.addRow(new Object[]{
                    mem.getId(), mem.getFirstName(), mem.getLastName(),
                    mem.getUsername(), mem.getCategory(), mem.getPhone()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showMemberDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Member mem = memberManager.getById(id);
                showMemberDialog(mem);
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite člana za izmenu.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Da li ste sigurni da želite da obrišete člana?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    memberManager.delete(id);
                    showMemberManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite člana za brisanje.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Upravljanje Članovima");
    }

    private void showMemberDialog(Member member) {
        JDialog dialog = new JDialog(this, member == null ? "Dodaj Člana" : "Uredi Člana", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField firstNameField = new JTextField(member != null ? member.getFirstName() : "", 20);
        JTextField lastNameField = new JTextField(member != null ? member.getLastName() : "", 20);
        JComboBox<Gender> genderCombo = new JComboBox<>(Gender.values());
        JTextField birthDateField = new JTextField(member != null ? member.getBirthDate().toString() : "YYYY-MM-DD", 20);
        JTextField phoneField = new JTextField(member != null ? member.getPhone() : "", 20);
        JTextField addressField = new JTextField(member != null ? member.getAddress() : "", 20);
        JTextField emailField = new JTextField(member != null ? member.getUsername() : "", 20);
        JPasswordField passwordField = new JPasswordField(member != null ? member.getPassword() : "", 20);
        JComboBox<MembershipCategory> categoryCombo = new JComboBox<>(MembershipCategory.values());

        if (member != null) {
            genderCombo.setSelectedItem(member.getGender());
            categoryCombo.setSelectedItem(member.getCategory());
        }

        panel.add(new JLabel("Ime:"));
        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Prezime:"));
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Pol:"));
        panel.add(genderCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Datum rođenja (YYYY-MM-DD):"));
        panel.add(birthDateField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Telefon:"));
        panel.add(phoneField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Adresa:"));
        panel.add(addressField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Lozinka:"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Kategorija:"));
        panel.add(categoryCombo);

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(e -> {
            try {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                Gender gender = (Gender) genderCombo.getSelectedItem();
                LocalDate birthDate = LocalDate.parse(birthDateField.getText());
                String phone = phoneField.getText();
                String address = addressField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                MembershipCategory category = (MembershipCategory) categoryCombo.getSelectedItem();


                Member newMember;
                if (member == null) {
                    newMember = new Member(
                            memberManager.getNextId(), firstName, lastName, gender, birthDate, phone, address,
                            email, password, category, membershipManager.getNextId()
                    );
                    memberManager.add(newMember);
                    JOptionPane.showMessageDialog(dialog, "Član je uspešno dodat!");
                } else {
                    newMember = new Member(
                            member.getId(), firstName, lastName, gender, birthDate, phone, address,
                            email, password, category, member.getMembershipId()
                    );
                    memberManager.update(newMember);
                    JOptionPane.showMessageDialog(dialog, "Član je uspešno ažuriran!");
                }

                dialog.dispose();
                showMemberManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Greška pri unosu podataka: " + ex.getMessage(),
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    // ==================== LIBRARIAN METHODS ====================
    private void showBookIssuing() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("ID Rezervacije:"));
        JTextField reservationIdField = new JTextField(20);
        panel.add(reservationIdField);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("ID Primerka:"));
        JTextField bookIdField = new JTextField(20);
        panel.add(bookIdField);

        panel.add(Box.createVerticalStrut(20));
        JButton issueButton = new JButton("Izda Knjigu");
        panel.add(issueButton);

        issueButton.addActionListener(e -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                int bookId = Integer.parseInt(bookIdField.getText());

                Reservation res = reservationManager.getById(reservationId);
                Book book = bookManager.getById(bookId);

                if (res != null && book != null && res.getStatus() == ReservationStatus.CONFIRMED) {
                    book.setStatus(BookStatus.BORROWED);
                    res.setStatus(ReservationStatus.CONFIRMED);
                    bookManager.update(book);

                    JOptionPane.showMessageDialog(this, "Knjiga je uspešno izdata!");
                    reservationIdField.setText("");
                    bookIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Neispravni podaci",
                            "Greška", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Unesite ispravne brojeve",
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        showPanel(panel, "Izdavanje Knjiga");
    }

    private void showReservationManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Potvrdi Rezervaciju");
        JButton rejectButton = new JButton("Odbij Rezervaciju");
        JButton cancelButton = new JButton("Otkaži Rezervaciju");
        JButton deleteButton = new JButton("Obriši Rezervaciju");
        JButton backButton = new JButton("Nazad");

        buttonPanel.add(confirmButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Član", "Knjiga", "Datum Rezervacije", "Datum Preuzimanja", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Reservation res : reservationManager.getAll()) {
            Member mem = memberManager.getById(res.getMemberId());
            Book book = bookManager.getById(res.getBookId());
            model.addRow(new Object[]{
                    res.getId(),
                    mem != null ? mem.getFirstName() + " " + mem.getLastName() : "N/A",
                    book != null ? book.getTitle() : "N/A",
                    res.getReservationDate(),
                    res.getPickupDate(),
                    res.getStatus()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        confirmButton.addActionListener(e -> updateReservationStatus(table, ReservationStatus.CONFIRMED));
        rejectButton.addActionListener(e -> updateReservationStatus(table, ReservationStatus.REJECTED));
        cancelButton.addActionListener(e -> updateReservationStatus(table, ReservationStatus.CANCELED));

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Da li ste sigurni da želite da obrišete rezervaciju?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    reservationManager.delete(id);
                    showReservationManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite rezervaciju za brisanje.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Upravljanje Rezervacijama");
    }

    private void updateReservationStatus(JTable table, ReservationStatus status) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) table.getValueAt(selectedRow, 0);
            Reservation res = reservationManager.getById(id);
            if (res != null) {
                res.setStatus(status);
                reservationManager.update(res);
                showReservationManagement();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Izaberite rezervaciju za izmenu statusa.",
                    "Upozorenje", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showMemberRegistration() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Ime:"));
        JTextField firstNameField = new JTextField(20);
        panel.add(firstNameField);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Prezime:"));
        JTextField lastNameField = new JTextField(20);
        panel.add(lastNameField);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Email (korisničko ime):"));
        JTextField emailField = new JTextField(20);
        panel.add(emailField);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Pol:"));
        JComboBox<Gender> genderCombo = new JComboBox<>(Gender.values());
        panel.add(genderCombo);

        panel.add(Box.createVerticalStrut(20));
        JButton registerButton = new JButton("Registruj člana");
        panel.add(registerButton);

        registerButton.addActionListener(e -> {
            try {
                Member member = new Member(
                        memberManager.getNextId(),
                        firstNameField.getText(),
                        lastNameField.getText(),
                        (Gender) genderCombo.getSelectedItem(),
                        LocalDate.now(),
                        "",
                        emailField.getText(),
                        emailField.getText(),
                        null,
                        null,
                        2
                );
                memberManager.add(member);
                JOptionPane.showMessageDialog(this, "Član je uspešno registrovan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Greška pri registraciji",
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        showPanel(panel, "Dodavanje Novih Članova");
    }

    private void showAvailableBooks() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Naslov", "Autor", "Status", "Dostupno"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Book book : bookManager.getAll()) {
            model.addRow(new Object[]{
                    book.getId(), book.getTitle(), book.getAuthor(),
                    book.getStatus(), book.getAvailableCopies()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Nazad");
        panel.add(backButton, BorderLayout.SOUTH);
        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Dostupne Knjige");
    }

    private void showMembershipManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Dodaj Članarinu");
        JButton editButton = new JButton("Uredi Članarinu");
        JButton deleteButton = new JButton("Obriši Članarinu");
        JButton backButton = new JButton("Nazad");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Član", "Od", "Do", "Aktuelna", "Tip"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Membership mem : membershipManager.getAll()) {
            Member member = memberManager.getById(mem.getMemberId());
            model.addRow(new Object[]{
                    mem.getId(),
                    member != null ? member.getFirstName() + " " + member.getLastName() : "N/A",
                    mem.getStartDate(), mem.getEndDate(), mem.isActive(), mem.getType()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showMembershipDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Membership membership = membershipManager.getById(id);
                showMembershipDialog(membership);
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite članarinu za izmenu.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Da li ste sigurni da želite da obrišete članarinu?",
                        "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    membershipManager.delete(id);
                    showMembershipManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Izaberite članarinu za brisanje.",
                        "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> showMainMenu());

        showPanel(panel, "Upravljanje Članarinama");
    }

    private void showMembershipDialog(Membership membership) {
        JDialog dialog = new JDialog(this, membership == null ? "Dodaj Članarinu" : "Uredi Članarinu", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Member combobox
        JComboBox<Member> memberCombo = new JComboBox<>();
        for (Member member : memberManager.getAll()) {
            memberCombo.addItem(member);
        }

        JTextField startDateField = new JTextField(membership != null ? membership.getStartDate().toString() : "YYYY-MM-DD", 20);
        JTextField endDateField = new JTextField(membership != null ? membership.getEndDate().toString() : "YYYY-MM-DD", 20);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"MONTHLY", "YEARLY"});
        JCheckBox activeCheckbox = new JCheckBox("Aktivna", membership == null || membership.isActive());

        if (membership != null) {
            memberCombo.setSelectedItem(memberManager.getById(membership.getMemberId()));
            typeCombo.setSelectedItem(membership.getType());
        }

        panel.add(new JLabel("Član:"));
        panel.add(memberCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Početak (YYYY-MM-DD):"));
        panel.add(startDateField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Kraj (YYYY-MM-DD):"));
        panel.add(endDateField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Tip:"));
        panel.add(typeCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(activeCheckbox);

        JButton saveButton = new JButton("Sačuvaj");
        saveButton.addActionListener(e -> {
            try {
                Member selectedMember = (Member) memberCombo.getSelectedItem();
                LocalDate startDate = LocalDate.parse(startDateField.getText());
                LocalDate endDate = LocalDate.parse(endDateField.getText());
                String type = (String) typeCombo.getSelectedItem();
                boolean active = activeCheckbox.isSelected();

                Membership newMembership;
                if (membership == null) {
                    assert selectedMember != null;
                    newMembership = new Membership(
                            membershipManager.getNextId(), selectedMember.getId(), startDate, endDate, active, type
                    );
                    membershipManager.add(newMembership);
                    JOptionPane.showMessageDialog(dialog, "Članarina je uspešno dodata!");
                } else {
                    assert selectedMember != null;
                    newMembership = new Membership(
                            membership.getId(), selectedMember.getId(), startDate, endDate, active, type
                    );
                    membershipManager.update(newMembership);
                    JOptionPane.showMessageDialog(dialog, "Članarina je uspešno ažurirana!");
                }

                dialog.dispose();
                showMembershipManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Greška pri unosu podataka: " + ex.getMessage(),
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showDailyReport() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Dnevni Izveštaj"));
        panel.add(Box.createVerticalStrut(10));

        int reservationsToday = (int) reservationManager.getAll().stream()
                .filter(r -> r.getReservationDate().equals(LocalDate.now()))
                .count();

        panel.add(new JLabel("Rezervacija kreirano danas: " + reservationsToday));

        showPanel(panel, "Dnevni Izveštaj");
    }

    // ==================== MEMBER METHODS ====================
    private void showMemberAvailableBooks() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Naslov", "Autor", "Žanr", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Book book : bookManager.getAll()) {
            if (book.getStatus() == BookStatus.AVAILABLE) {
                Genre genre = genreManager.getById(book.getGenreId());
                model.addRow(new Object[]{
                        book.getId(), book.getTitle(), book.getAuthor(),
                        genre != null ? genre.getName() : "N/A", book.getStatus()
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Dostupne Knjige");
    }

    private void showMemberReservation() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("ID Knjige:"));
        JTextField bookIdField = new JTextField(20);
        panel.add(bookIdField);

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Datum Preuzimanja:"));
        JTextField pickupDateField = new JTextField("YYYY-MM-DD", 20);
        panel.add(pickupDateField);

        panel.add(Box.createVerticalStrut(20));
        JButton reserveButton = new JButton("Kreiraj Rezervaciju");
        panel.add(reserveButton);

        reserveButton.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText());
                LocalDate pickupDate = LocalDate.parse(pickupDateField.getText());

                Book book = bookManager.getById(bookId);
                if (book != null) {
                    Reservation res = new Reservation(
                            reservationManager.getNextId(),
                            currentUser.id,
                            bookId,
                            LocalDate.now(),
                            pickupDate,
                            pickupDate.plusDays(7),
                            ReservationStatus.PENDING,
                            0
                    );
                    reservationManager.add(res);
                    JOptionPane.showMessageDialog(this, "Rezervacija je uspešno kreirana!");
                    bookIdField.setText("");
                    pickupDateField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Knjiga ne postoji",
                            "Greška", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Greška pri kreiranju rezervacije",
                        "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        showPanel(panel, "Nova Rezervacija");
    }

    private void showMyReservations() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {"ID", "Knjiga", "Preuzimanje", "Vraćanje", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Reservation res : reservationManager.getReservationsByMember(currentUser.id)) {
            Book book = bookManager.getById(res.getBookId());
            model.addRow(new Object[]{
                    res.getId(), book.getTitle(), res.getPickupDate(),
                    res.getReturnDate(), res.getStatus()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Moje Rezervacije");
    }

    private void showMemberProfile() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Member member = memberManager.getById(currentUser.id);
        if (member != null) {
            panel.add(new JLabel("Ime: " + member.getFirstName()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Prezime: " + member.getLastName()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Email: " + member.getUsername()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Telefon: " + member.getPhone()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Kategorija: " + (member.getCategory() != null ? member.getCategory() : "N/A")));
        }

        showPanel(panel, "Moj Profil");
    }

    private void showRentalHistory() {
        showPanel(new JPanel(), "Istorija Iznajmljivanja");
    }

    private void showMembershipRenewal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Tip članarine:"));
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"MONTHLY", "YEARLY"});
        panel.add(typeCombo);

        panel.add(Box.createVerticalStrut(20));
        JButton renewButton = new JButton("Obnovi članarinu");
        panel.add(renewButton);

        renewButton.addActionListener(e -> {
            String type = (String) typeCombo.getSelectedItem();
            LocalDate startDate = LocalDate.now();
            assert type != null;
            LocalDate endDate = type.equals("MONTHLY") ? startDate.plusMonths(1) : startDate.plusYears(1);

            Membership membership = new Membership(
                    membershipManager.getNextId(),
                    currentUser.id,
                    startDate,
                    endDate,
                    true,
                    type
            );
            membershipManager.add(membership);
            JOptionPane.showMessageDialog(this, "Članarina je uspešno obnovljena!");
        });

        showPanel(panel, "Obnova Članarine");
    }

    private void showPanel(JPanel panel, String title) {
        getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 60, 120));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);
        headerPanel.setPreferredSize(new Dimension(1000, 40));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        JButton backButton = new JButton("Nazad");
        JPanel footerPanel = new JPanel();
        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> showMainMenu());

        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibrarySystemGUI frame = new LibrarySystemGUI();
            frame.setVisible(true);
        });
    }

    // Helper class za User
    private static class User {
        int id;
        String firstName;
        String lastName;
        UserType type;
        EmployeeRole role;

        User(int id, String firstName, String lastName, UserType type, EmployeeRole role) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.type = type;
            this.role = role;
        }
    }

    enum UserType {
        EMPLOYEE, MEMBER
    }
}