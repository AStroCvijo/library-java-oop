package model.entities;

import model.enums.BookStatus;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private int genreId;
    private int copyNumber;
    private BookStatus status;

    public Book(int id, String title, String author, String isbn,
                int publicationYear, int genreId, int copyNumber,
                BookStatus status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.genreId = genreId;
        this.copyNumber = copyNumber;
        this.status = status;
    }

    // Getteri i setteri
    public int  getId() { return id; }
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

    public int getCopyNumber() { return copyNumber; }
    public void setCopyNumber(int copyNumber) { this.copyNumber = copyNumber; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
}