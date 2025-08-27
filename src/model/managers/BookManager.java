package model.managers;

import model.entities.Book;
import model.entities.Reservation;
import model.enums.BookStatus;
import model.enums.ReservationStatus;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookManager implements IManager<Book> {
    private List<Book> books;
    private String filename;

    public BookManager(String filename) {
        this.books = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }

    @Override
    public void add(Book book) {
        Book existingBook = getBookByIsbn(book.getIsbn());

        if (existingBook != null) {
            existingBook.incrementCopies(book.getTotalCopies());
            update(existingBook);
        } else {
            books.add(book);
        }
        saveToFile();
    }

    @Override
    public Book getById(int id) {
        return books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Book getBookByIsbn(String isbn) {
        return books.stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Book> getAll() {
        return new ArrayList<>(books);
    }

    @Override
    public void update(Book book) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId() == book.getId()) {
                books.set(i, book);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        books.removeIf(b -> b.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        books.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] values = line.split(",");
                if (values.length >= 9) {
                    int id = Integer.parseInt(values[0]);
                    String title = values[1];
                    String author = values[2];
                    String isbn = values[3];
                    int publicationYear = Integer.parseInt(values[4]);
                    int genreId = Integer.parseInt(values[5]);
                    int totalCopies = Integer.parseInt(values[6]);
                    int availableCopies = Integer.parseInt(values[7]);
                    BookStatus status = BookStatus.valueOf(values[8]);

                    Book book = new Book(id, title, author, isbn, publicationYear,
                            genreId, totalCopies, availableCopies, status);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            writer.println("id,title,author,isbn,publicationYear,genreId,totalCopies,availableCopies,status");

            for (Book book : books) {
                writer.println(book.getId() + "," +
                        book.getTitle() + "," +
                        book.getAuthor() + "," +
                        book.getIsbn() + "," +
                        book.getPublicationYear() + "," +
                        book.getGenreId() + "," +
                        book.getTotalCopies() + "," +
                        book.getAvailableCopies() + "," +
                        book.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public List<Book> getAvailableBooks(LocalDate startPeriod, LocalDate endPeriod, ReservationManager reservationManager) {
        List<Book> availableBooks = new ArrayList<>();

        for (Book book : books) {
            if (book.isAvailable()) {
                int reservedCopies = countReservedCopies(book, startPeriod, endPeriod, reservationManager);
                int actuallyAvailableCopies = book.getAvailableCopies() - reservedCopies;

                if (actuallyAvailableCopies > 0) {
                    Book availableBook = new Book(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getIsbn(),
                            book.getPublicationYear(),
                            book.getGenreId(),
                            book.getTotalCopies(),
                            actuallyAvailableCopies,
                            book.getStatus()
                    );
                    availableBooks.add(availableBook);
                }
            }
        }

        return availableBooks;
    }

    private int countReservedCopies(Book book, LocalDate startPeriod, LocalDate endPeriod,
                                    ReservationManager reservationManager) {
        int reservedCount = 0;

        for (Reservation reservation : reservationManager.getAll()) {
            if (reservation.getBookId() == book.getId() &&
                    (reservation.getStatus() == ReservationStatus.CONFIRMED ||
                            reservation.getStatus() == ReservationStatus.PENDING)) {

                if (reservationOverlaps(reservation, startPeriod, endPeriod)) {
                    reservedCount++;
                }

                if (reservedCount >= book.getAvailableCopies()) {
                    break;
                }
            }
        }

        return reservedCount;
    }

    private boolean reservationOverlaps(Reservation reservation, LocalDate startPeriod, LocalDate endPeriod) {
        LocalDate pickup = reservation.getPickupDate();
        LocalDate returnDate = reservation.getReturnDate();

        return !(returnDate.isBefore(startPeriod) || pickup.isAfter(endPeriod));
    }

    public void addCopies(String isbn, int numberOfCopies) {
        Book book = getBookByIsbn(isbn);
        if (book != null) {
            book.incrementCopies(numberOfCopies);
            update(book);
        } else {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found");
        }
    }

    public void removeCopies(String isbn, int numberOfCopies) {
        Book book = getBookByIsbn(isbn);
        if (book != null) {
            if (numberOfCopies > book.getTotalCopies() - (book.getTotalCopies() - book.getAvailableCopies())) {
                throw new IllegalArgumentException("Cannot remove more copies than are available");
            }
            book.setTotalCopies(book.getTotalCopies() - numberOfCopies);
            book.setAvailableCopies(book.getAvailableCopies() - numberOfCopies);
            update(book);
        } else {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found");
        }
    }
}