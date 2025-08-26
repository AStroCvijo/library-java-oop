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
        books.add(book);
        saveToFile();
    }

    @Override
    public Book getById(int id) {
        return books.stream()
                .filter(b -> b.getId() == id).
                findFirst().
                orElse(null);
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
                if (values.length >= 8) {
                    int id = Integer.parseInt(values[0]);
                    String title = values[1];
                    String author = values[2];
                    String isbn = values[3];
                    int publicationYear = Integer.parseInt(values[4]);
                    int  genreId = Integer.parseInt(values[5]);
                    int copyNumber = Integer.parseInt(values[6]);
                    BookStatus status = BookStatus.valueOf(values[7]);

                    Book book = new Book(id, title, author, isbn, publicationYear,
                            genreId, copyNumber, status);
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
            writer.println("id,title,author,isbn,publicationYear,genreId,copyNumber,status");

            for (Book book : books) {
                writer.println(book.getId() + "," +
                        book.getTitle() + "," +
                        book.getAuthor() + "," +
                        book.getIsbn() + "," +
                        book.getPublicationYear() + "," +
                        book.getGenreId() + "," +
                        book.getCopyNumber() + "," +
                        book.getStatus());
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public List<Book> getAvailableBooks(LocalDate startPeriod, LocalDate endPeriod, ReservationManager reservationManager) {
        List<Book> availableBooks = new ArrayList<>();

        for (Book book : books) {
            if (book.getStatus() == BookStatus.AVAILABLE) {
                boolean isReserved = false;

                for (Reservation reservation : reservationManager.getAll()) {
                    if (reservation.getBookId() == book.getId() &&
                            (reservation.getStatus() == ReservationStatus.CONFIRMED ||
                                    reservation.getStatus() == ReservationStatus.PENDING)) {

                        if (!(reservation.getReturnDate().isBefore(startPeriod) ||
                                reservation.getPickupDate().isAfter(endPeriod))) {
                            isReserved = true;
                            break;
                        }
                    }
                }

                if (!isReserved) {
                    availableBooks.add(book);
                }
            }
        }

        return availableBooks;
    }
}
