package gui;

import model.entities.*;
import model.enums.ReservationStatus;
import model.managers.*;
import model.theme.UITheme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberGUI extends JFrame {
    private MemberManager memberManager;
    private BookManager bookManager;
    private GenreManager genreManager;
    private ReservationManager reservationManager;
    private MembershipManager membershipManager;
    private PriceListManager priceListManager;
    private ReservationServiceManager reservationServiceManager;
    private SettingsManager settingsManager;
    private LibrarySystemGUI mainGUI;

    public MemberGUI(LibrarySystemGUI mainGUI) {
        this.mainGUI = mainGUI;
        setTitle("Member Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        UITheme.applyTheme(this);

        initializeManagers();
        showMemberMenu();
    }

    private void initializeManagers() {
        memberManager = new MemberManager("data/members.csv");
        bookManager = new BookManager("data/books.csv");
        genreManager = new GenreManager("data/genres.csv");
        reservationManager = new ReservationManager("data/reservations.csv");
        membershipManager = new MembershipManager("data/memberships.csv");
        priceListManager = new PriceListManager("data/pricelist.csv");
        reservationServiceManager = new ReservationServiceManager("data/reservationServices.csv");
        settingsManager = new SettingsManager();
    }

    private void showMemberMenu() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND_COLOR);

        // Header
        mainPanel.add(UITheme.createHeaderPanel("Welcome, " + mainGUI.getCurrentUser().getFirstName() + " " +
                mainGUI.getCurrentUser().getLastName() + " (MEMBER)"), BorderLayout.NORTH);

        // Center panel with options
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(UITheme.BACKGROUND_COLOR);

        centerPanel.add(createMenuButton("View Available Books", e -> showMemberAvailableBooks()));
        centerPanel.add(createMenuButton("My Reservations", e -> showMyReservations()));
        centerPanel.add(createMenuButton("My Profile", e -> showMemberProfile()));
        centerPanel.add(createMenuButton("Rental History", e -> showRentalHistory()));
        centerPanel.add(createMenuButton("Renew Membership", e -> showMembershipRenewal()));

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

    // ==================== MEMBER METHODS ====================
    private void showMemberAvailableBooks() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        // North panel for filters and buttons
        JPanel northPanel = new JPanel(new GridLayout(3, 4, 5, 5));
        northPanel.setBackground(UITheme.BACKGROUND_COLOR);
        northPanel.setBorder(BorderFactory.createTitledBorder("Filter Books"));

        JTextField titleFilter = new JTextField(15);
        titleFilter.setFont(UITheme.MAIN_FONT);
        JTextField authorFilter = new JTextField(15);
        authorFilter.setFont(UITheme.MAIN_FONT);
        JComboBox<Genre> genreFilter = new JComboBox<>();
        genreFilter.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Genre) {
                    setText(((Genre) value).getName());
                } else {
                    setText("All Genres");
                }
                return this;
            }
        });
        genreFilter.setFont(UITheme.MAIN_FONT);
        genreFilter.addItem(null); // for "All Genres"
        for (Genre genre : genreManager.getAll()) {
            genreFilter.addItem(genre);
        }
        JTextField fromDateFilter = new JTextField("YYYY-MM-DD", 10);
        fromDateFilter.setFont(UITheme.MAIN_FONT);
        JTextField toDateFilter = new JTextField("YYYY-MM-DD", 10);
        toDateFilter.setFont(UITheme.MAIN_FONT);
        JButton filterButton = new JButton("Filter");
        UITheme.styleButton(filterButton);
        JButton clearButton = new JButton("Clear Filters");
        UITheme.styleButton(clearButton);

        northPanel.add(new JLabel("Title:"));
        northPanel.add(titleFilter);
        northPanel.add(new JLabel("Author:"));
        northPanel.add(authorFilter);
        northPanel.add(new JLabel("Genre:"));
        northPanel.add(genreFilter);
        northPanel.add(new JLabel("From:"));
        northPanel.add(fromDateFilter);
        northPanel.add(new JLabel("To:"));
        northPanel.add(toDateFilter);
        northPanel.add(filterButton);
        northPanel.add(clearButton);
        panel.add(northPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Title", "Author", "Genre", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        Runnable updateTable = () -> {
            model.setRowCount(0);
            String title = titleFilter.getText().toLowerCase();
            String author = authorFilter.getText().toLowerCase();
            Genre genre = (Genre) genreFilter.getSelectedItem();
            LocalDate fromDate = null;
            LocalDate toDate = null;
            try {
                if (!fromDateFilter.getText().equals("YYYY-MM-DD")) {
                    fromDate = LocalDate.parse(fromDateFilter.getText());
                }
                if (!toDateFilter.getText().equals("YYYY-MM-DD")) {
                    toDate = LocalDate.parse(toDateFilter.getText());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Book book : bookManager.getAvailableBooks(fromDate, toDate)) {
                Genre bookGenre = genreManager.getById(book.getGenreId());
                if ((title.isEmpty() || book.getTitle().toLowerCase().contains(title)) &&
                    (author.isEmpty() || book.getAuthor().toLowerCase().contains(author)) &&
                    (genre == null || (bookGenre != null && bookGenre.getId() == genre.getId()))
                ) {
                    model.addRow(new Object[]{
                            book.getId(), book.getTitle(), book.getAuthor(),
                            bookGenre != null ? bookGenre.getName() : "N/A", book.getStatus()
                    });
                }
            }
        };

        filterButton.addActionListener(e -> updateTable.run());
        clearButton.addActionListener(e -> {
            titleFilter.setText("");
            authorFilter.setText("");
            genreFilter.setSelectedItem(null);
            fromDateFilter.setText("YYYY-MM-DD");
            toDateFilter.setText("YYYY-MM-DD");
            updateTable.run();
        });
        updateTable.run(); // Initial population

        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton reserveButton = new JButton("Create Reservation");
        UITheme.styleButton(reserveButton);
        buttonPanel.add(reserveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        reserveButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) table.getValueAt(selectedRow, 0);
                Book book = bookManager.getById(bookId);
                showCreateReservationDialog(book);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to reserve.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "Available Books");
    }

    private void showCreateReservationDialog(Book book) {
        Member member = memberManager.getById(mainGUI.getCurrentUser().getId());
        if (member.getLastCancellationDate() != null && member.getLastCancellationDate().isAfter(LocalDate.now().minusDays(1))) {
            JOptionPane.showMessageDialog(this, "You cannot make a new reservation for 24 hours after cancelling one.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Membership membership = membershipManager.getByMemberId(mainGUI.getCurrentUser().getId());
        if (membership == null || !membership.isActive() || membership.getEndDate().isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "You must have an active membership to make a reservation.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Create Reservation for " + book.getTitle(), true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField pickupDateField = new JTextField("YYYY-MM-DD", 15);
        formPanel.add(new JLabel("Pickup Date:"));
        formPanel.add(pickupDateField);

        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(new JLabel("Additional Services:"));

        List<JCheckBox> serviceCheckBoxes = new ArrayList<>();
        for (model.entities.PriceListItem item : priceListManager.getAll()) {
            if (item.getType() == model.enums.PriceListItemType.PRIORITY_PICKUP ||
                item.getType() == model.enums.PriceListItemType.EXTENDED_RETENTION) {
                JCheckBox checkBox = new JCheckBox(item.getDescription() + " (" + item.getPrice() + ")");
                checkBox.putClientProperty("item", item);
                serviceCheckBoxes.add(checkBox);
                formPanel.add(checkBox);
            }
        }

        dialog.add(formPanel, BorderLayout.CENTER);

        JButton reserveButton = new JButton("Reserve");
        UITheme.styleButton(reserveButton);
        dialog.add(reserveButton, BorderLayout.SOUTH);

        reserveButton.addActionListener(e -> {
            try {
                LocalDate pickupDate = LocalDate.parse(pickupDateField.getText());
                if (pickupDate.isBefore(LocalDate.now().plusDays(1))) {
                    JOptionPane.showMessageDialog(dialog, "Pickup date must be tomorrow or later.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (book.getAvailableCopies() > 0) {
                    double totalPrice = 0;
                    int extendedDays = 0;
                    List<model.entities.PriceListItem> selectedServices = new ArrayList<>();

                    for (JCheckBox checkBox : serviceCheckBoxes) {
                        if (checkBox.isSelected()) {
                            model.entities.PriceListItem item = (model.entities.PriceListItem) checkBox.getClientProperty("item");
                            if (item.getType() == model.enums.PriceListItemType.EXTENDED_RETENTION) {
                                String daysStr = JOptionPane.showInputDialog(dialog, "Enter number of extra days for retention:", "Extended Retention", JOptionPane.QUESTION_MESSAGE);
                                try {
                                    extendedDays = Integer.parseInt(daysStr);
                                    if (extendedDays <= 0) {
                                        JOptionPane.showMessageDialog(dialog, "Number of days must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                    totalPrice += item.getPrice() * extendedDays;
                                } catch (NumberFormatException ex) {
                                    JOptionPane.showMessageDialog(dialog, "Invalid number of days.", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                            } else {
                                totalPrice += item.getPrice();
                            }
                            selectedServices.add(item);
                        }
                    }

                    int reservationId = reservationManager.getNextId();
                    Reservation res = new Reservation(
                            reservationId,
                            mainGUI.getCurrentUser().getId(),
                            book.getId(),
                            LocalDate.now(),
                            pickupDate,
                            pickupDate.plusDays(settingsManager.getRentalDuration() + extendedDays), // Use setting and extended days
                            ReservationStatus.PENDING,
                            totalPrice
                    );
                    reservationManager.add(res);

                    for (model.entities.PriceListItem service : selectedServices) {
                        int quantity = 1;
                        if (service.getType() == model.enums.PriceListItemType.EXTENDED_RETENTION) {
                            quantity = extendedDays;
                        }
                        reservationServiceManager.add(new model.entities.ReservationService(
                                reservationServiceManager.getNextId(),
                                reservationId,
                                service.getId(),
                                quantity,
                                service.getPrice() * quantity
                        ));
                    }

                    JOptionPane.showMessageDialog(dialog, "Reservation created successfully!");
                    dialog.dispose();
                    showMemberAvailableBooks(); // Refresh the list
                } else {
                    JOptionPane.showMessageDialog(dialog, "Book is not available.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error creating reservation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void showMyReservations() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(UITheme.BACKGROUND_COLOR);
        JButton cancelButton = new JButton("Cancel Reservation");
        UITheme.styleButton(cancelButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Book", "Pickup", "Return", "Status", "Total Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Reservation res : reservationManager.getReservationsByMember(mainGUI.getCurrentUser().getId())) {
            Book book = bookManager.getById(res.getBookId());
            String bookTitle = (book != null) ? book.getTitle() : "Book not found";
            model.addRow(new Object[]{
                    res.getId(), bookTitle, res.getPickupDate(),
                    res.getReturnDate(), res.getStatus(), res.getTotalPrice()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        cancelButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) table.getValueAt(selectedRow, 0);
                Reservation res = reservationManager.getById(id);
                if (res != null && res.getStatus() == ReservationStatus.PENDING) {
                    res.setStatus(ReservationStatus.CANCELED);
                    reservationManager.update(res);

                    Book book = bookManager.getById(res.getBookId());
                    book.setAvailableCopies(book.getAvailableCopies() + 1);
                    bookManager.update(book);

                    Member member = memberManager.getById(mainGUI.getCurrentUser().getId());
                    member.setLastCancellationDate(LocalDate.now());
                    memberManager.update(member);

                    showMyReservations();
                } else {
                    JOptionPane.showMessageDialog(this, "You can only cancel pending reservations.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a reservation to cancel.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        showPanel(panel, "My Reservations");
    }

    private void showMemberProfile() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        Member member = memberManager.getById(mainGUI.getCurrentUser().getId());
        if (member != null) {
            panel.add(new JLabel("First Name: " + member.getFirstName()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Last Name: " + member.getLastName()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Email: " + member.getUsername()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Phone: " + member.getPhone()));
            panel.add(Box.createVerticalStrut(5));
            panel.add(new JLabel("Category: " + (member.getCategory() != null ? member.getCategory() : "N/A")));
        }

        showPanel(panel, "My Profile");
    }

    private void showRentalHistory() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        String[] columns = {"ID", "Book", "Pickup", "Return", "Status", "Total Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Reservation res : reservationManager.getReservationsByMember(mainGUI.getCurrentUser().getId())) {
            if (res.getStatus() != ReservationStatus.PENDING && res.getStatus() != ReservationStatus.CANCELED) {
                Book book = bookManager.getById(res.getBookId());
                String bookTitle = (book != null) ? book.getTitle() : "Book not found";
                model.addRow(new Object[]{
                        res.getId(), bookTitle, res.getPickupDate(),
                        res.getReturnDate(), res.getStatus(), res.getTotalPrice()
                });
            }
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        UITheme.styleTable(table, scrollPane);
        panel.add(scrollPane, BorderLayout.CENTER);

        showPanel(panel, "Rental History");
    }

    private void showMembershipRenewal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(UITheme.BACKGROUND_COLOR);

        Membership currentMembership = membershipManager.getByMemberId(mainGUI.getCurrentUser().getId());
        if (currentMembership != null) {
            JLabel label = new JLabel("Current Membership End Date: " + currentMembership.getEndDate());
            label.setFont(UITheme.BOLD_FONT);
            panel.add(label);
        } else {
            JLabel label = new JLabel("You do not have an active membership.");
            label.setFont(UITheme.BOLD_FONT);
            panel.add(label);
        }

        panel.add(Box.createVerticalStrut(10));
        JLabel typeLabel = new JLabel("Membership Type:");
        typeLabel.setFont(UITheme.MAIN_FONT);
        panel.add(typeLabel);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"MONTHLY", "YEARLY"});
        typeCombo.setFont(UITheme.MAIN_FONT);
        panel.add(typeCombo);

        panel.add(Box.createVerticalStrut(20));
        JButton renewButton = new JButton("Renew Membership");
        UITheme.styleButton(renewButton);
        panel.add(renewButton);

        renewButton.addActionListener(e -> {
            Membership membership = membershipManager.getByMemberId(mainGUI.getCurrentUser().getId());
            if (membership != null) {
                if (memberManager.getById(mainGUI.getCurrentUser().getId()).getLateReturns() > 5) {
                    JOptionPane.showMessageDialog(this, "You have too many late returns to renew your membership.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String selectedType = (String) typeCombo.getSelectedItem();
                membership.setType(selectedType);
                membership.setStatus(model.enums.MembershipStatus.PENDING_RENEWAL);
                membershipManager.update(membership);
                JOptionPane.showMessageDialog(this, "Membership renewal request sent!");
                showMemberMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Error: Could not find your membership.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        showPanel(panel, "Renew Membership");
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
        backButton.addActionListener(e -> showMemberMenu());
        footerPanel.add(backButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);


        getContentPane().add(mainPanel);
        revalidate();
        repaint();
    }
}
