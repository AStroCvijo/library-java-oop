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
        setTitle("Bibliotečni Informacioni Sistem");
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
        centerPanel.add(createMenuButton("Upravljanje Cenovnikom", e -> showPriceListManagement()));
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
        buttonPanel.add(new JButton("Dodaj Zaposlenog"));
        buttonPanel.add(new JButton("Uredi Zaposlenog"));
        buttonPanel.add(new JButton("Obriši Zaposlenog"));
        buttonPanel.add(new JButton("Nazad"));
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Ime", "Prezime", "Pozicija", "Plata", "Stež"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Employee emp : employeeManager.getAll()) {
            model.addRow(new Object[]{
                    emp.getId(), emp.getFirstName(), emp.getLastName(),
                    emp.getRole(), emp.calculateSalary(), emp.getYearsOfExperience()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Upravljanje Zaposlenima");
    }

    private void showBookManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Dodaj Knjigu"));
        buttonPanel.add(new JButton("Uredi Knjigu"));
        buttonPanel.add(new JButton("Obriši Knjigu"));
        buttonPanel.add(new JButton("Nazad"));
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Naslov", "Autor", "Žanr", "Status", "Godište"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Book book : bookManager.getAll()) {
            Genre genre = genreManager.getById(book.getGenreId());
            model.addRow(new Object[]{
                    book.getId(), book.getTitle(), book.getAuthor(),
                    genre != null ? genre.getName() : "N/A", book.getStatus(), book.getPublicationYear()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Upravljanje Knjigama");
    }

    private void showPriceListManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new JButton("Dodaj Stavku"));
        buttonPanel.add(new JButton("Uredi Stavku"));
        buttonPanel.add(new JButton("Obriši Stavku"));
        buttonPanel.add(new JButton("Nazad"));
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Tip", "Opis", "Cena", "Od", "Do"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (PriceListItem item : priceListManager.getAll()) {
            model.addRow(new Object[]{
                    item.getId(), item.getType(), item.getDescription(),
                    item.getPrice(), item.getValidFrom(), item.getValidTo()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Upravljanje Cenovnikom");
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
        buttonPanel.add(new JButton("Dodaj člana"));
        buttonPanel.add(new JButton("Uredi člana"));
        buttonPanel.add(new JButton("Obriši člana"));
        buttonPanel.add(new JButton("Nazad"));
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Ime", "Prezime", "Email", "Kategorija", "Članarina"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Member mem : memberManager.getAll()) {
            model.addRow(new Object[]{
                    mem.getId(), mem.getFirstName(), mem.getLastName(),
                    mem.getUsername(), mem.getCategory(), "Aktivna"
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Upravljanje Članovima");
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
        buttonPanel.add(new JButton("Potvrdi Rezervaciju"));
        buttonPanel.add(new JButton("Odbij Rezervaciju"));
        buttonPanel.add(new JButton("Otkazi Rezervaciju"));
        buttonPanel.add(new JButton("Nazad"));
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Član", "Knjiga", "Datum Preuzimanja", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Reservation res : reservationManager.getAll()) {
            Member mem = memberManager.getById(res.getMemberId());
            Book book = bookManager.getById(res.getBookId());
            model.addRow(new Object[]{
                    res.getId(), mem.getFirstName() + " " + mem.getLastName(),
                    book.getTitle(), res.getPickupDate(), res.getStatus()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Upravljanje Rezervacijama");
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

        String[] columns = {"ID", "Član", "Od", "Do", "Aktuelna", "Tip"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Membership mem : membershipManager.getAll()) {
            Member member = memberManager.getById(mem.getMemberId());
            model.addRow(new Object[]{
                    mem.getId(), member.getFirstName() + " " + member.getLastName(),
                    mem.getStartDate(), mem.getEndDate(), mem.isActive(), mem.getType()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Pregled Članarina");
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