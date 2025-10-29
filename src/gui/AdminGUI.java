package gui;

import model.entities.Employee;
import model.entities.Book;
import model.entities.Genre;
import model.entities.PriceListItem;
import model.entities.Member;
import model.enums.*;
import model.managers.*;
import model.theme.UITheme;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class AdminGUI extends JFrame {
    private EmployeeManager employeeManager;
    private MemberManager memberManager;
    private BookManager bookManager;
    private GenreManager genreManager;
    private ReservationManager reservationManager;
    private PriceListManager priceListManager;
    private MembershipManager membershipManager;
    private SettingsManager settingsManager;
    private LibrarySystemGUI mainGUI;

    public AdminGUI(LibrarySystemGUI mainGUI) {
        this.mainGUI = mainGUI;
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        UITheme.applyTheme(this);

        initializeManagers();
        reservationManager.updateExpiredReservations();
        showAdminMenu();
    }

    private void initializeManagers() {
        employeeManager = new EmployeeManager("data/employees.csv");
        memberManager = new MemberManager("data/members.csv");
        bookManager = new BookManager("data/books.csv");
        genreManager = new GenreManager("data/genres.csv");
        reservationManager = new ReservationManager("data/reservations.csv");
        priceListManager = new PriceListManager("data/pricelist.csv");
        membershipManager = new MembershipManager("data/memberships.csv");
        settingsManager = new SettingsManager();
    }

    private void showAdminMenu() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Welcome, " + mainGUI.getCurrentUser().getFirstName() + " " +
                mainGUI.getCurrentUser().getLastName() + " (ADMIN)"), BorderLayout.NORTH);

        // Center panel with options
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 3, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(UITheme.BACKGROUND_COLOR);

        centerPanel.add(createMenuButton("Manage Employees", e -> showEmployeeManagement()));
        centerPanel.add(createMenuButton("Manage Members", e -> showMemberManagement()));
        centerPanel.add(createMenuButton("Manage Books", e -> showBookManagement()));
        centerPanel.add(createMenuButton("Manage Genres", e -> showGenreManagement()));
        centerPanel.add(createMenuButton("Manage Reservations", e -> showReservationManagement()));
        centerPanel.add(createMenuButton("Manage Price List", e -> showPriceListManagement()));
        centerPanel.add(createMenuButton("Manage Memberships", e -> showMembershipManagement()));
        centerPanel.add(createMenuButton("Reports and Analytics", e -> showReports()));
        centerPanel.add(createMenuButton("System Settings", e -> showSettings()));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Logout button in the footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton logoutButton = new JButton("Logout");
        UITheme.styleButton(logoutButton);
        logoutButton.addActionListener(e -> {
            mainGUI.setCurrentUser(null);
            mainGUI.showLoginScreen();
            dispose();
        });
        footerPanel.add(logoutButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);


        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        UITheme.styleBigButton(button);
        button.addActionListener(listener);
        return button;
    }

    // ==================== ADMIN METHODS ====================
    private void showEmployeeManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton addButton = new JButton("Add Employee");
        UITheme.styleButton(addButton);
        JButton editButton = new JButton("Edit Employee");
        UITheme.styleButton(editButton);
        JButton deleteButton = new JButton("Delete Employee");
        UITheme.styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "First Name", "Last Name", "Role", "Salary", "Experience"};
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
        UITheme.styleTable(table, scrollPane);
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
                JOptionPane.showMessageDialog(this, "Select an employee to edit.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Delete button action
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this employee?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    employeeManager.delete(id);
                    showEmployeeManagement(); // Refresh
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an employee to delete.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Employee Management");
    }

    private void showEmployeeDialog(Employee employee) {
        JDialog dialog = new JDialog(this, employee == null ? "Add Employee" : "Edit Employee", true);
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

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        panel.add(birthDateField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Education:"));
        panel.add(educationCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Years of Experience:"));
        panel.add(yearsOfExperienceSpinner);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Base Salary:"));
        panel.add(salaryField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Role:"));
        panel.add(roleCombo);

        JButton saveButton = new JButton("Save");
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
                    JOptionPane.showMessageDialog(dialog, "Employee added successfully!");
                } else {
                    newEmployee = new Employee(
                            employee.getId(), firstName, lastName, gender, birthDate, phone, address,
                            username, password, education, yearsOfExperience, baseSalary, role
                    );
                    employeeManager.update(newEmployee);
                    JOptionPane.showMessageDialog(dialog, "Employee updated successfully!");
                }

                dialog.dispose();
                showEmployeeManagement(); // Refresh view
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
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
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton addButton = new JButton("Add Book");
        UITheme.styleButton(addButton);
        JButton editButton = new JButton("Edit Book");
        UITheme.styleButton(editButton);
        JButton deleteButton = new JButton("Delete Book");
        UITheme.styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Author", "Genre", "Status", "Year", "Available"};
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
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showBookDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Book book = bookManager.getById(id);
                showBookDialog(book);
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to edit.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this book?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    bookManager.delete(id);
                    showBookManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a book to delete.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Book Management");
    }

    private void showBookDialog(Book book) {
        JDialog dialog = new JDialog(this, book == null ? "Add Book" : "Edit Book", true);
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

        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("ISBN:"));
        panel.add(isbnField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Publication Year:"));
        panel.add(yearField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Genre:"));
        panel.add(genreCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Status:"));
        panel.add(statusCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Number of Copies:"));
        panel.add(copiesSpinner);

        JButton saveButton = new JButton("Save");
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
                    JOptionPane.showMessageDialog(dialog, "Book added successfully!");
                } else {

                    int newTotalCopies = (int) copiesSpinner.getValue();
                    int oldTotalCopies = book.getTotalCopies();
                    int diff = newTotalCopies - oldTotalCopies;
                    int newAvailableCopies = book.getAvailableCopies() + diff;

                    newBook = new Book(
                            book.getId(), title, author, isbn, publicationYear,
                            selectedGenre != null ? selectedGenre.getId() : 0, newTotalCopies, newAvailableCopies, status
                    );
                    bookManager.update(newBook);
                    JOptionPane.showMessageDialog(dialog, "Book updated successfully!");
                }

                dialog.dispose();
                showBookManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
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
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton addButton = new JButton("Add Item");
        UITheme.styleButton(addButton);
        JButton editButton = new JButton("Edit Item");
        UITheme.styleButton(editButton);
        JButton deleteButton = new JButton("Delete Item");
        UITheme.styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Type", "Description", "Price", "From", "To"};
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
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showPriceListItemDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                PriceListItem item = priceListManager.getById(id);
                showPriceListItemDialog(item);
            } else {
                JOptionPane.showMessageDialog(this, "Select an item to edit.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this item?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    priceListManager.delete(id);
                    showPriceListManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an item to delete.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Price List Management");
    }

    private void showPriceListItemDialog(PriceListItem item) {
        JDialog dialog = new JDialog(this, item == null ? "Add Item" : "Edit Item", true);
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

        panel.add(new JLabel("Item Type:"));
        panel.add(typeCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Valid From (YYYY-MM-DD):"));
        panel.add(validFromField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Valid To (YYYY-MM-DD):"));
        panel.add(validToField);

        JButton saveButton = new JButton("Save");
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
                    JOptionPane.showMessageDialog(dialog, "Item added successfully!");
                } else {
                    newItem = new PriceListItem(
                            item.getId(), type, description, price, validFrom, validTo
                    );
                    priceListManager.update(newItem);
                    JOptionPane.showMessageDialog(dialog, "Item updated successfully!");
                }

                dialog.dispose();
                showPriceListManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
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
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton addButton = new JButton("Add Genre");
        UITheme.styleButton(addButton);
        JButton editButton = new JButton("Edit Genre");
        UITheme.styleButton(editButton);
        JButton deleteButton = new JButton("Delete Genre");
        UITheme.styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Description"};
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
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showGenreDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Genre genre = genreManager.getById(id);
                showGenreDialog(genre);
            } else {
                JOptionPane.showMessageDialog(this, "Select a genre to edit.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this genre?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    genreManager.delete(id);
                    showGenreManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a genre to delete.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Genre Management");
    }

    private void showGenreDialog(Genre genre) {
        JDialog dialog = new JDialog(this, genre == null ? "Add Genre" : "Edit Genre", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(genre != null ? genre.getName() : "", 20);
        JTextField descriptionField = new JTextField(genre != null ? genre.getDescription() : "", 20);

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String description = descriptionField.getText();

                Genre newGenre;
                if (genre == null) {
                    newGenre = new Genre(genreManager.getNextId(), name, description);
                    genreManager.add(newGenre);
                    JOptionPane.showMessageDialog(dialog, "Genre added successfully!");
                } else {
                    newGenre = new Genre(genre.getId(), name, description);
                    genreManager.update(newGenre);
                    JOptionPane.showMessageDialog(dialog, "Genre updated successfully!");
                }

                dialog.dispose();
                showGenreManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }


    private void showReports() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        panel.add(createMenuButton("Income and Expenses", e -> showIncomeAndExpensesReport()));
        panel.add(createMenuButton("Librarian Statistics", e -> showLibrarianStatisticsReport()));
        panel.add(createMenuButton("Reservation Statistics", e -> showReservationStatisticsReport()));
        panel.add(createMenuButton("Book Statistics", e -> showBookStatisticsReport()));
        panel.add(createMenuButton("Total Income Report", e -> showTotalIncomeReport()));
        panel.add(createMenuButton("Income Chart", e -> showIncomeChart()));
        panel.add(createMenuButton("Librarian Workload Chart", e -> showLibrarianWorkloadChart()));

        showPanel(panel, "Reports and Analytics");
    }

    private void showTotalIncomeReport() {
        double totalIncome = 0;
        for (model.entities.Reservation res : reservationManager.getAll()) {
            totalIncome += res.getTotalPrice();
        }
        for (model.entities.Membership mem : membershipManager.getAll()) {
            if (mem.isActive()) {
                // Assuming a yearly fee from the price list
                PriceListItem item = priceListManager.getItemByType(model.enums.PriceListItemType.YEARLY_SUBSCRIPTION);
                if (item != null) {
                    totalIncome += item.getPrice();
                }
            }
        }

        JOptionPane.showMessageDialog(this,
                "Total Income: " + totalIncome,
                "Total Income Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showBookStatisticsReport() {
        JDialog dialog = new JDialog(this, "Book Statistics", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        String[] columns = {"Book", "Genre", "Rentals", "Reservations"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Book book : bookManager.getAll()) {
            Genre genre = genreManager.getById(book.getGenreId());
            int rentals = 0;
            int reservations = 0;
            for (model.entities.Reservation res : reservationManager.getAll()) {
                if (res.getBookId() == book.getId()) {
                    reservations++;
                    if (res.getStatus() == ReservationStatus.ISSUED || res.getStatus() == ReservationStatus.RETURNED) {
                        rentals++;
                    }
                }
            }
            model.addRow(new Object[]{
                    book.getTitle(),
                    genre != null ? genre.getName() : "N/A",
                    rentals,
                    reservations
            });
        }

        JTable table = new JTable(model);
        dialog.add(new JScrollPane(table));
        dialog.setVisible(true);
    }

    private void showReservationStatisticsReport() {
        int confirmed = 0;
        int rejected = 0;
        int canceled = 0;

        for (model.entities.Reservation res : reservationManager.getAll()) {
            switch (res.getStatus()) {
                case CONFIRMED:
                    confirmed++;
                    break;
                case REJECTED:
                    rejected++;
                    break;
                case CANCELED:
                    canceled++;
                    break;
            }
        }

        JOptionPane.showMessageDialog(this,
                "Confirmed Reservations: " + confirmed + "\n" +
                "Rejected Reservations: " + rejected + "\n" +
                "Canceled Reservations: " + canceled,
                "Reservation Statistics",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showLibrarianStatisticsReport() {
        JDialog dialog = new JDialog(this, "Librarian Statistics", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);

        String[] columns = {"Librarian", "Books Issued"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        Map<Integer, Integer> booksIssuedByLibrarian = new java.util.HashMap<>();

        for (model.entities.Reservation res : reservationManager.getAll()) {
            if ((res.getStatus() == ReservationStatus.ISSUED || res.getStatus() == ReservationStatus.RETURNED) && res.getLibrarianId() != 0) {
                booksIssuedByLibrarian.merge(res.getLibrarianId(), 1, Integer::sum);
            }
        }

        for (Map.Entry<Integer, Integer> entry : booksIssuedByLibrarian.entrySet()) {
            Employee librarian = employeeManager.getById(entry.getKey());
            if (librarian != null) {
                model.addRow(new Object[]{librarian.getFirstName() + " " + librarian.getLastName(), entry.getValue()});
            }
        }

        JTable table = new JTable(model);
        dialog.add(new JScrollPane(table));
        dialog.setVisible(true);
    }

    private void showIncomeAndExpensesReport() {
        double totalIncome = 0;
        for (model.entities.Reservation res : reservationManager.getAll()) {
            totalIncome += res.getTotalPrice();
        }
        for (model.entities.Membership mem : membershipManager.getAll()) {
            if (mem.isActive()) {
                // Assuming a yearly fee from the price list
                PriceListItem item = priceListManager.getItemByType(model.enums.PriceListItemType.YEARLY_SUBSCRIPTION);
                if (item != null) {
                    totalIncome += item.getPrice();
                }
            }
        }

        double totalExpenses = 0;
        for (Employee emp : employeeManager.getAll()) {
            totalExpenses += emp.calculateSalary();
        }

        JOptionPane.showMessageDialog(this,
                "Total Income: " + totalIncome + "\n" +
                "Total Expenses: " + totalExpenses + "\n" +
                "Net Profit: " + (totalIncome - totalExpenses),
                "Income and Expenses Report",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showReportNotImplemented() {
        JOptionPane.showMessageDialog(this, "This report is not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSettings() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JLabel label = new JLabel("Set book rental duration (days):");
        label.setFont(UITheme.BOLD_FONT);
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(settingsManager.getRentalDuration(), 1, 30, 1));
        spinner.setFont(UITheme.MAIN_FONT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(10));
        panel.add(spinner);
        panel.add(Box.createVerticalStrut(20));
        JButton saveButton = new JButton("Save");
        UITheme.styleButton(saveButton);
        panel.add(saveButton);

        saveButton.addActionListener(e -> {
            int duration = (int) spinner.getValue();
            settingsManager.setSetting(SettingsManager.RENTAL_DURATION_KEY, String.valueOf(duration));
            settingsManager.saveSettings();
            JOptionPane.showMessageDialog(this, "Settings saved. Rental duration is now " + duration + " days.");
        });

        showPanel(panel, "System Settings");
    }

    private void showIncomeChart() {
        JDialog dialog = new JDialog(this, "Income Chart", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);

        CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Monthly Income by Member Category").xAxisTitle("Month").yAxisTitle("Income").build();
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setAxisTicksVisible(true);

        Map<Month, Map<MembershipCategory, Double>> monthlyIncome = new EnumMap<>(Month.class);

        for (model.entities.Reservation res : reservationManager.getAll()) {
            if (res.getReservationDate().isAfter(LocalDate.now().minusMonths(12))) {
                Member member = memberManager.getById(res.getMemberId());
                if (member != null) {
                    Month month = res.getReservationDate().getMonth();
                    monthlyIncome.computeIfAbsent(month, k -> new EnumMap<>(MembershipCategory.class));
                    
                    MembershipCategory category = member.getCategory() != null ? member.getCategory() : MembershipCategory.UNCATEGORIZED;
                    monthlyIncome.get(month).merge(category, res.getTotalPrice(), Double::sum);
                }
            }
        }

        List<Month> months = new ArrayList<>();
        List<Double> studentIncome = new ArrayList<>();
        List<Double> pupilIncome = new ArrayList<>();
        List<Double> retireeIncome = new ArrayList<>();
        List<Double> uncategorizedIncome = new ArrayList<>();

        for (Month month : Month.values()) {
            if (monthlyIncome.containsKey(month)) {
                months.add(month);
                studentIncome.add(monthlyIncome.get(month).getOrDefault(MembershipCategory.STUDENT, 0.0));
                pupilIncome.add(monthlyIncome.get(month).getOrDefault(MembershipCategory.PUPIL, 0.0));
                retireeIncome.add(monthlyIncome.get(month).getOrDefault(MembershipCategory.RETIREE, 0.0));
                uncategorizedIncome.add(monthlyIncome.get(month).getOrDefault(MembershipCategory.UNCATEGORIZED, 0.0));
            }
        }

        List<String> monthNames = new ArrayList<>();
        for (Month month : months) {
            monthNames.add(month.toString());
        }

        if (!months.isEmpty()) {
            chart.addSeries("Student", monthNames, studentIncome);
            chart.addSeries("Pupil", monthNames, pupilIncome);
            chart.addSeries("Retiree", monthNames, retireeIncome);
            chart.addSeries("Uncategorized", monthNames, uncategorizedIncome);
        }

        JPanel chartPanel = new XChartPanel<>(chart);
        dialog.add(chartPanel);
        dialog.setVisible(true);
    }

    private void showLibrarianWorkloadChart() {
        JDialog dialog = new JDialog(this, "Librarian Workload", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(1, 2));

        // Librarian Workload Pie Chart
        PieChart workloadChart = new PieChartBuilder().width(400).height(600).title("Librarian Workload (Last 30 Days)").build();
        workloadChart.getStyler().setLegendVisible(true);
        workloadChart.getStyler().setStartAngleInDegrees(90);

        Map<String, Integer> librarianWorkload = new java.util.HashMap<>();
        for (model.entities.Reservation res : reservationManager.getAll()) {
            if ((res.getStatus() == ReservationStatus.ISSUED || res.getStatus() == ReservationStatus.RETURNED) &&
                    res.getPickupDate() != null && res.getPickupDate().isAfter(LocalDate.now().minusDays(30))) {
                if (res.getLibrarianId() != 0) {
                    Employee librarian = employeeManager.getById(res.getLibrarianId());
                    if (librarian != null) {
                        String librarianName = librarian.getFirstName() + " " + librarian.getLastName();
                        librarianWorkload.merge(librarianName, 1, Integer::sum);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> entry : librarianWorkload.entrySet()) {
            workloadChart.addSeries(entry.getKey(), entry.getValue());
        }

        // Reservation Status Pie Chart
        PieChart statusChart = new PieChartBuilder().width(400).height(600).title("Reservation Status (Last 30 Days)").build();
        statusChart.getStyler().setLegendVisible(true);
        statusChart.getStyler().setStartAngleInDegrees(90);

        Map<ReservationStatus, Integer> statusCounts = new EnumMap<>(ReservationStatus.class);
        for (model.entities.Reservation res : reservationManager.getAll()) {
            if (res.getReservationDate().isAfter(LocalDate.now().minusDays(30))) {
                statusCounts.merge(res.getStatus(), 1, Integer::sum);
            }
        }

        for (Map.Entry<ReservationStatus, Integer> entry : statusCounts.entrySet()) {
            statusChart.addSeries(entry.getKey().toString(), entry.getValue());
        }

        dialog.add(new XChartPanel<>(workloadChart));
        dialog.add(new XChartPanel<>(statusChart));
        dialog.setVisible(true);
    }


    private void showMemberManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton addButton = new JButton("Add Member");
        UITheme.styleButton(addButton);
        JButton editButton = new JButton("Edit Member");
        UITheme.styleButton(editButton);
        JButton deleteButton = new JButton("Delete Member");
        UITheme.styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "First Name", "Last Name", "Email", "Category", "Phone"};
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
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showMemberDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Member mem = memberManager.getById(id);
                showMemberDialog(mem);
            } else {
                JOptionPane.showMessageDialog(this, "Select a member to edit.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this member?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    memberManager.delete(id);
                    showMemberManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a member to delete.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Member Management");
    }

    private void showMemberDialog(Member member) {
        JDialog dialog = new JDialog(this, member == null ? "Add Member" : "Edit Member", true);
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

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        panel.add(birthDateField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Address:"));
        panel.add(addressField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(Box.createVerticalStrut(5));
        panel.add(new JLabel("Category:"));
        panel.add(categoryCombo);

        JButton saveButton = new JButton("Save");
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
                            email, password, category, membershipManager.getNextId(), 0
                    );
                    memberManager.add(newMember);
                    JOptionPane.showMessageDialog(dialog, "Member added successfully!");
                } else {
                    newMember = new Member(
                            member.getId(), firstName, lastName, gender, birthDate, phone, address,
                            email, password, category, member.getMembershipId(), member.getLateReturns()
                    );
                    memberManager.update(newMember);
                    JOptionPane.showMessageDialog(dialog, "Member updated successfully!");
                }

                dialog.dispose();
                showMemberManagement();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error saving data: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void showReservationManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        String[] columns = {"ID", "Member", "Book", "Librarian", "Reservation Date", "Pickup Date", "Return Date", "Status", "Total Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (model.entities.Reservation res : reservationManager.getAll()) {
            Member mem = memberManager.getById(res.getMemberId());
            Book book = bookManager.getById(res.getBookId());
            Employee librarian = employeeManager.getById(res.getLibrarianId());
            model.addRow(new Object[]{
                    res.getId(),
                    mem != null ? mem.getFirstName() + " " + mem.getLastName() : "N/A",
                    book != null ? book.getTitle() : "N/A",
                    librarian != null ? librarian.getFirstName() + " " + librarian.getLastName() : "N/A",
                    res.getReservationDate(),
                    res.getPickupDate(),
                    res.getReturnDate(),
                    res.getStatus(),
                    res.getTotalPrice()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Reservation Management");
    }

    private void showMembershipManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton addButton = new JButton("Add Membership");
        UITheme.styleButton(addButton);
        JButton editButton = new JButton("Edit Membership");
        UITheme.styleButton(editButton);
        JButton deleteButton = new JButton("Delete Membership");
        UITheme.styleButton(deleteButton);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Member", "Start Date", "End Date", "Active", "Type"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (model.entities.Membership mem : membershipManager.getAll()) {
            Member member = memberManager.getById(mem.getMemberId());
            model.addRow(new Object[]{
                    mem.getId(),
                    member != null ? member.getFirstName() + " " + member.getLastName() : "N/A",
                    mem.getStartDate(),
                    mem.getEndDate(),
                    mem.getStatus(),
                    mem.getType()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> showMembershipDialog(null));

        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                model.entities.Membership mem = membershipManager.getById(id);
                showMembershipDialog(mem);
            } else {
                JOptionPane.showMessageDialog(this, "Select a membership to edit.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this membership?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    membershipManager.delete(id);
                    showMembershipManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a membership to delete.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Membership Management");
    }

        private void showMembershipDialog(model.entities.Membership membership) {
            JDialog dialog = new JDialog(this, membership == null ? "Add Membership" : "Edit Membership", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);
    
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
            JComboBox<Member> memberCombo = new JComboBox<>();
            for (Member mem : memberManager.getAll()) {
                memberCombo.addItem(mem);
            }
            JTextField startDateField = new JTextField(membership != null ? membership.getStartDate().toString() : "YYYY-MM-DD", 20);
            JTextField endDateField = new JTextField(membership != null ? membership.getEndDate().toString() : "YYYY-MM-DD", 20);
            JComboBox<MembershipStatus> statusCombo = new JComboBox<>(MembershipStatus.values());
            JTextField typeField = new JTextField(membership != null ? membership.getType() : "", 20);
    
            if (membership != null) {
                for (int i = 0; i < memberCombo.getItemCount(); i++) {
                    if (memberCombo.getItemAt(i).getId() == membership.getMemberId()) {
                        memberCombo.setSelectedIndex(i);
                        break;
                    }
                }
                statusCombo.setSelectedItem(membership.getStatus());
            }
    
            panel.add(new JLabel("Member:"));
            panel.add(memberCombo);
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Start Date (YYYY-MM-DD):"));
            panel.add(startDateField);
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("End Date (YYYY-MM-DD):"));
            panel.add(endDateField);
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Status:"));
            panel.add(statusCombo);
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Type:"));
            panel.add(typeField);
    
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                try {
                    Member selectedMember = (Member) memberCombo.getSelectedItem();
                    LocalDate startDate = LocalDate.parse(startDateField.getText());
                    LocalDate endDate = LocalDate.parse(endDateField.getText());
                    MembershipStatus status = (MembershipStatus) statusCombo.getSelectedItem();
                    String type = typeField.getText();
    
                    model.entities.Membership newMembership;
                    if (membership == null) {
                        newMembership = new model.entities.Membership(
                                membershipManager.getNextId(), selectedMember.getId(), startDate, endDate, status, type
                        );
                        membershipManager.add(newMembership);
                        JOptionPane.showMessageDialog(dialog, "Membership added successfully!");
                    } else {
                        newMembership = new model.entities.Membership(
                                membership.getId(), selectedMember.getId(), startDate, endDate, status, type
                        );
                        membershipManager.update(newMembership);
    
                        JOptionPane.showMessageDialog(dialog, "Membership updated successfully!");
                    }
    
                    dialog.dispose();
                    showMembershipManagement();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Error saving data: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
    
            panel.add(Box.createVerticalStrut(20));
            panel.add(saveButton);
            dialog.add(panel, BorderLayout.CENTER);
            dialog.setVisible(true);
        }
    private void showPanel(JPanel panel, String title) {
        getContentPane().removeAll();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);

        // Header
        mainPanel.add(UITheme.createHeaderPanel(title), BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);

        // Footer with Back button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton backButton = new JButton("Back to Menu");
        UITheme.styleButton(backButton);
        backButton.addActionListener(e -> showAdminMenu());
        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);


        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }
}
