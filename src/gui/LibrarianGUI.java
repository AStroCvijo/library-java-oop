package gui;

import model.entities.Book;
import model.entities.Member;
import model.entities.PriceListItem;
import model.entities.Reservation;
import model.enums.BookStatus;
import model.enums.Gender;
import model.enums.MembershipStatus;
import model.enums.ReservationStatus;
import model.managers.*;
import model.theme.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LibrarianGUI extends JFrame {
    private MemberManager memberManager;
    private BookManager bookManager;
    private ReservationManager reservationManager;
    private MembershipManager membershipManager;
    private PriceListManager priceListManager;
    private ReservationServiceManager reservationServiceManager;
    private LibrarySystemGUI mainGUI;

    public LibrarianGUI(LibrarySystemGUI mainGUI) {
        this.mainGUI = mainGUI;
        setTitle("Librarian Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        UITheme.applyTheme(this);

        initializeManagers();
        reservationManager.updateExpiredReservations();
        showLibrarianMenu();
    }

    private void initializeManagers() {
        memberManager = new MemberManager("data/members.csv");
        bookManager = new BookManager("data/books.csv");
        reservationManager = new ReservationManager("data/reservations.csv");
        membershipManager = new MembershipManager("data/memberships.csv");
        priceListManager = new PriceListManager("data/pricelist.csv");
        reservationServiceManager = new ReservationServiceManager("data/reservationServices.csv");
    }

    private void showLibrarianMenu() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Welcome, " + mainGUI.getCurrentUser().getFirstName() + " " +
                mainGUI.getCurrentUser().getLastName() + " (LIBRARIAN)"), BorderLayout.NORTH);

        // Center panel with options
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(UITheme.BACKGROUND_COLOR);

        centerPanel.add(createMenuButton("Issue Books", e -> showBookIssuing()));
        centerPanel.add(createMenuButton("Return Books", e -> showBookReturning()));
        centerPanel.add(createMenuButton("Manage Reservations", e -> showReservationManagement()));
        centerPanel.add(createMenuButton("Add New Members", e -> showMemberRegistration()));
        centerPanel.add(createMenuButton("View Available Books", e -> showAvailableBooks()));
        centerPanel.add(createMenuButton("View Memberships", e -> showMembershipManagement()));
        centerPanel.add(createMenuButton("Manage Renewals", e -> showRenewalManagement()));
        centerPanel.add(createMenuButton("Daily Report", e -> showDailyReport()));

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

    private void showBookReturning() {
        JDialog dialog = new JDialog(this, "Return Book", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JTextField bookIdField = new JTextField(10);
        JButton returnButton = new JButton("Return");

        dialog.add(new JLabel("Book ID:"));
        dialog.add(bookIdField);
        dialog.add(returnButton);

        returnButton.addActionListener(e -> {
            try {
                int bookId = Integer.parseInt(bookIdField.getText());
                Book book = bookManager.getById(bookId);
                Reservation reservation = reservationManager.getAll().stream()
                        .filter(r -> r.getBookId() == bookId && r.getStatus() == ReservationStatus.ISSUED)
                        .findFirst()
                        .orElse(null);

                if (book != null && reservation != null) {
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                    if (book.getAvailableCopies() > 0) {
                        book.setStatus(BookStatus.AVAILABLE);
                    }
                    bookManager.update(book);

                    reservation.setStatus(ReservationStatus.RETURNED);

                    // Penalty logic
                    if (LocalDate.now().isAfter(reservation.getReturnDate())) {
                        Member member = memberManager.getById(reservation.getMemberId());
                        member.setLateReturns(member.getLateReturns() + 1);
                        memberManager.update(member);

                        PriceListItem penaltyItem = priceListManager.getItemByType(model.enums.PriceListItemType.PENALTY);
                        if (penaltyItem != null) {
                            reservation.setTotalPrice(reservation.getTotalPrice() + penaltyItem.getPrice());
                        }
                        JOptionPane.showMessageDialog(dialog, "Book is returned late! Penalty applied.");
                    }

                    reservationManager.update(reservation);

                    JOptionPane.showMessageDialog(dialog, "Book returned successfully!");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Invalid book ID or no active rental found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid book ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private JButton createMenuButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        UITheme.styleBigButton(button);
        button.addActionListener(listener);
        return button;
    }

    // ==================== LIBRARIAN METHODS ====================
    private void showBookIssuing() {
        JDialog dialog = new JDialog(this, "Issue Book", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel verificationPanel = new JPanel();
        JTextField reservationIdField = new JTextField(10);
        JButton verifyButton = new JButton("Verify");
        verificationPanel.add(new JLabel("Reservation ID:"));
        verificationPanel.add(reservationIdField);
        verificationPanel.add(verifyButton);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Details"));
        JLabel reservationLabel = new JLabel("Reservation: ");
        JLabel memberLabel = new JLabel("Member: ");
        JLabel bookLabel = new JLabel("Book: ");
        detailsPanel.add(reservationLabel);
        detailsPanel.add(memberLabel);
        detailsPanel.add(bookLabel);

        detailsPanel.add(Box.createVerticalStrut(10));
        detailsPanel.add(new JLabel("Additional Services:"));

        List<JCheckBox> serviceCheckBoxes = new ArrayList<>();
        for (model.entities.PriceListItem item : priceListManager.getAll()) {
            if (item.getType() == model.enums.PriceListItemType.PRIORITY_PICKUP ||
                item.getType() == model.enums.PriceListItemType.PRIORITY_RETURN ||
                item.getType() == model.enums.PriceListItemType.EXTENDED_RETENTION) {
                JCheckBox checkBox = new JCheckBox(item.getDescription() + " (" + item.getPrice() + ")");
                checkBox.putClientProperty("item", item);
                serviceCheckBoxes.add(checkBox);
                detailsPanel.add(checkBox);
            }
        }

        JButton issueButton = new JButton("Issue Book");
        issueButton.setEnabled(false);

        verifyButton.addActionListener(e -> {
            try {
                int reservationId = Integer.parseInt(reservationIdField.getText());
                Reservation res = reservationManager.getById(reservationId);
                if (res != null && res.getStatus() == ReservationStatus.CONFIRMED) {
                    if (!res.getPickupDate().equals(LocalDate.now())) {
                        JOptionPane.showMessageDialog(dialog, "This book can only be issued on the pickup date: " + res.getPickupDate(), "Error", JOptionPane.ERROR_MESSAGE);
                        issueButton.setEnabled(false);
                        return;
                    }
                    Member mem = memberManager.getById(res.getMemberId());
                    Book book = bookManager.getById(res.getBookId());
                    reservationLabel.setText("Reservation: " + res.getId() + " (" + res.getStatus() + ")");
                    memberLabel.setText("Member: " + mem.getFirstName() + " " + mem.getLastName());
                    bookLabel.setText("Book: " + book.getTitle());
                    issueButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Invalid or not confirmed reservation ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    issueButton.setEnabled(false);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid reservation ID.", "Error", JOptionPane.ERROR_MESSAGE);
                issueButton.setEnabled(false);
            }
        });

        issueButton.addActionListener(e -> {
            int reservationId = Integer.parseInt(reservationIdField.getText());
            Reservation res = reservationManager.getById(reservationId);
            Book book = bookManager.getById(res.getBookId());

            book.setStatus(BookStatus.BORROWED);
            bookManager.update(book);

            double totalPrice = res.getTotalPrice();
            int extendedDays = 0;
            for (JCheckBox checkBox : serviceCheckBoxes) {
                if (checkBox.isSelected()) {
                    model.entities.PriceListItem item = (model.entities.PriceListItem) checkBox.getClientProperty("item");
                    if (item.getType() == model.enums.PriceListItemType.EXTENDED_RETENTION) {
                        String daysStr = JOptionPane.showInputDialog(this, "Enter number of extra days for retention:", "Extended Retention", JOptionPane.QUESTION_MESSAGE);
                        try {
                            extendedDays = Integer.parseInt(daysStr);
                            if (extendedDays <= 0) {
                                JOptionPane.showMessageDialog(this, "Number of days must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            totalPrice += item.getPrice() * extendedDays;
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(this, "Invalid number of days.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        totalPrice += item.getPrice();
                    }
                    reservationServiceManager.add(new model.entities.ReservationService(
                            reservationServiceManager.getNextId(),
                            res.getId(),
                            item.getId(),
                            (item.getType() == model.enums.PriceListItemType.EXTENDED_RETENTION) ? extendedDays : 1,
                            item.getPrice() * ((item.getType() == model.enums.PriceListItemType.EXTENDED_RETENTION) ? extendedDays : 1)
                    ));
                }
            }

            res.setStatus(ReservationStatus.ISSUED);
            res.setPickupDate(LocalDate.now());
            res.setReturnDate(res.getReturnDate().plusDays(extendedDays));
            res.setTotalPrice(totalPrice);
            res.setLibrarianId(mainGUI.getCurrentUser().getId());
            reservationManager.update(res);

            JOptionPane.showMessageDialog(dialog, "Book issued successfully!");
            dialog.dispose();
        });

        dialog.add(verificationPanel, BorderLayout.NORTH);
        dialog.add(detailsPanel, BorderLayout.CENTER);
        dialog.add(issueButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showReservationManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton confirmButton = new JButton("Confirm Reservation");
        UITheme.styleButton(confirmButton);
        JButton rejectButton = new JButton("Reject Reservation");
        UITheme.styleButton(rejectButton);
        JButton cancelButton = new JButton("Cancel Reservation");
        UITheme.styleButton(cancelButton);
        JButton deleteButton = new JButton("Delete Reservation");
        UITheme.styleButton(deleteButton);

        buttonPanel.add(confirmButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Member", "Book", "Reservation Date", "Pickup Date", "Return Date", "Status"};
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
                    res.getReturnDate(),
                    res.getStatus()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        confirmButton.addActionListener(e -> updateReservationStatus(table, ReservationStatus.CONFIRMED));
        rejectButton.addActionListener(e -> updateReservationStatus(table, ReservationStatus.REJECTED));
        cancelButton.addActionListener(e -> updateReservationStatus(table, ReservationStatus.CANCELED));

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                int option = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this reservation?",
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    reservationManager.delete(id);
                    showReservationManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a reservation to delete.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Reservation Management");
    }

    private void updateReservationStatus(JTable table, ReservationStatus newStatus) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) table.getValueAt(selectedRow, 0);
            Reservation res = reservationManager.getById(id);
            if (res != null) {
                ReservationStatus oldStatus = res.getStatus();

                if (oldStatus == ReservationStatus.ISSUED || oldStatus == ReservationStatus.CANCELED ||
                        oldStatus == ReservationStatus.REJECTED || oldStatus == ReservationStatus.RETURNED) {
                    JOptionPane.showMessageDialog(this, "This reservation's status cannot be changed.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                Book book = bookManager.getById(res.getBookId());

                // Logic for confirming a reservation
                if (newStatus == ReservationStatus.CONFIRMED && oldStatus == ReservationStatus.PENDING) {
                    if (book.getAvailableCopies() > 0) {
                        book.setAvailableCopies(book.getAvailableCopies() - 1);
                        bookManager.update(book);
                        res.setStatus(newStatus);
                        reservationManager.update(res);
                    } else {
                        JOptionPane.showMessageDialog(this, "No available copies of this book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Logic for rejecting or canceling a confirmed reservation
                else if ((newStatus == ReservationStatus.REJECTED || newStatus == ReservationStatus.CANCELED) && oldStatus == ReservationStatus.CONFIRMED) {
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                    bookManager.update(book);
                    res.setStatus(newStatus);
                    reservationManager.update(res);
                }
                // For other status changes
                else {
                    res.setStatus(newStatus);
                    reservationManager.update(res);
                }
                showReservationManagement();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a reservation to update its status.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showMemberRegistration() {
        JDialog dialog = new JDialog(this, "Add New Member", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JComboBox<Gender> genderCombo = new JComboBox<>(Gender.values());
        JTextField birthDateField = new JTextField("YYYY-MM-DD", 20);
        JTextField phoneField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<model.enums.MembershipCategory> categoryCombo = new JComboBox<>(model.enums.MembershipCategory.values());

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
                model.enums.MembershipCategory category = (model.enums.MembershipCategory) categoryCombo.getSelectedItem();

                int memberId = memberManager.getNextId();
                int membershipId = membershipManager.getNextId();

                Member newMember = new Member(
                        memberId, firstName, lastName, gender, birthDate, phone, address,
                        email, password, category, membershipId, 0
                );
                memberManager.add(newMember);

                model.entities.Membership newMembership = new model.entities.Membership(
                        membershipId, memberId, LocalDate.now(), LocalDate.now().plusYears(1), model.enums.MembershipStatus.ACTIVE, "ANNUAL"
                );
                membershipManager.add(newMembership);

                JOptionPane.showMessageDialog(dialog, "Member registered successfully!");
                dialog.dispose();
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

    private void showAvailableBooks() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        String[] columns = {"ID", "Title", "Author", "Status", "Available"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Book book : bookManager.getAll()) {
            model.addRow(new Object[]{
                    book.getId(), book.getTitle(), book.getAuthor(),
                    book.getStatus(), book.getAvailableCopies()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Available Books");
    }

    private void showMembershipManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        String[] columns = {"ID", "Member", "Start Date", "End Date", "Status", "Type"};
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

        showPanel(panel, "Membership Management");
    }

    private void showDailyReport() {
        JDialog dialog = new JDialog(this, "Daily Report", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Reservations Today
        String[] reservationsColumns = {"ID", "Member", "Book", "Status"};
        DefaultTableModel reservationsModel = new DefaultTableModel(reservationsColumns, 0);
        for (Reservation res : reservationManager.getAll()) {
            if (res.getReservationDate().equals(LocalDate.now())) {
                Member mem = memberManager.getById(res.getMemberId());
                Book book = bookManager.getById(res.getBookId());
                reservationsModel.addRow(new Object[]{
                        res.getId(),
                        mem != null ? mem.getFirstName() + " " + mem.getLastName() : "N/A",
                        book != null ? book.getTitle() : "N/A",
                        res.getStatus()
                });
            }
        }
        JTable reservationsTable = new JTable(reservationsModel);
        tabbedPane.addTab("Reservations Today", new JScrollPane(reservationsTable));

        // Books Issued Today
        String[] issuedColumns = {"ID", "Title", "Author", "Member"};
        DefaultTableModel issuedModel = new DefaultTableModel(issuedColumns, 0);
        for (Reservation res : reservationManager.getAll()) {
            if (res.getStatus() == ReservationStatus.ISSUED && res.getPickupDate().equals(LocalDate.now())) {
                Book book = bookManager.getById(res.getBookId());
                Member mem = memberManager.getById(res.getMemberId());
                issuedModel.addRow(new Object[]{
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        mem.getFirstName() + " " + mem.getLastName()
                });
            }
        }
        JTable issuedTable = new JTable(issuedModel);
        tabbedPane.addTab("Books Issued Today", new JScrollPane(issuedTable));

        dialog.add(tabbedPane, BorderLayout.CENTER);
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
        backButton.addActionListener(e -> showLibrarianMenu());
        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);


        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }

    private void showRenewalManagement() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton approveButton = new JButton("Approve");
        UITheme.styleButton(approveButton);
        JButton denyButton = new JButton("Deny");
        UITheme.styleButton(denyButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Member", "Start Date", "End Date", "Status", "Type"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (model.entities.Membership mem : membershipManager.getAll()) {
            if (mem.getStatus() == MembershipStatus.PENDING_RENEWAL) {
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
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        approveButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                model.entities.Membership mem = membershipManager.getById(id);
                if (mem != null) {
                    if ("YEARLY".equalsIgnoreCase(mem.getType())) {
                        mem.setEndDate(mem.getEndDate().plusYears(1));
                    } else {
                        mem.setEndDate(mem.getEndDate().plusMonths(1));
                    }
                    mem.setStatus(MembershipStatus.ACTIVE);
                    membershipManager.update(mem);
                    showRenewalManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a renewal to approve.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        denyButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                model.entities.Membership mem = membershipManager.getById(id);
                if (mem != null) {
                    mem.setStatus(MembershipStatus.INACTIVE);
                    membershipManager.update(mem);
                    showRenewalManagement();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a renewal to deny.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Renewal Management");
    }
}
