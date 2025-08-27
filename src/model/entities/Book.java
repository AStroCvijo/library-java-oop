package model.entities;

import model.enums.BookStatus;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private int genreId;
    private int totalCopies;
    private int availableCopies;
    private BookStatus status;

    // Constructor
    public Book(int id, String title, String author, String isbn,
                int publicationYear, int genreId, int totalCopies,
                int availableCopies, BookStatus status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.genreId = genreId;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public int getGenreId() { return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    // Helper methods
    public boolean isAvailable() {
        return availableCopies > 0 && status == BookStatus.AVAILABLE;
    }

    public void incrementCopies(int amount) {
        this.totalCopies += amount;
        this.availableCopies += amount;
    }

    public void decrementAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public void incrementAvailableCopies() {
        availableCopies++;
        if (availableCopies > 0 && status != BookStatus.AVAILABLE) {
            status = BookStatus.AVAILABLE;
        }
    }
}