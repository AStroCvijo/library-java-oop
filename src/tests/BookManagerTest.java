package tests;

import model.entities.Book;
import model.enums.BookStatus;
import model.managers.BookManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookManagerTest {

    private BookManager bookManager;
    private String testFilename = "test_books.csv";

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(testFilename);
        testFile.createNewFile();
        try (PrintWriter writer = new PrintWriter(new FileWriter(testFilename))) {
            writer.println("id,title,author,isbn,publicationYear,genreId,totalCopies,availableCopies,status");
        }
        bookManager = new BookManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetBook() {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", 2022, 1, 5, 5, BookStatus.AVAILABLE);
        bookManager.add(book);
        Book retrievedBook = bookManager.getById(1);
        assertNotNull(retrievedBook);
        assertEquals("Test Book", retrievedBook.getTitle());
    }

    @Test
    void updateBook() {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", 2022, 1, 5, 5, BookStatus.AVAILABLE);
        bookManager.add(book);
        book.setTitle("Updated Title");
        bookManager.update(book);
        Book retrievedBook = bookManager.getById(1);
        assertEquals("Updated Title", retrievedBook.getTitle());
    }

    @Test
    void deleteBook() {
        Book book = new Book(1, "Test Book", "Test Author", "1234567890", 2022, 1, 5, 5, BookStatus.AVAILABLE);
        bookManager.add(book);
        bookManager.delete(1);
        assertNull(bookManager.getById(1));
    }

    @Test
    void getAllBooks() {
        bookManager.add(new Book(1, "Book 1", "Author 1", "111", 2020, 1, 2, 2, BookStatus.AVAILABLE));
        bookManager.add(new Book(2, "Book 2", "Author 2", "222", 2021, 2, 3, 3, BookStatus.AVAILABLE));
        List<Book> books = bookManager.getAll();
        assertEquals(2, books.size());
    }

    @Test
    void loadAndSaveFile() throws IOException {
        bookManager.add(new Book(1, "File Book", "File Author", "333", 2022, 1, 1, 1, BookStatus.AVAILABLE));
        // The saveToFile is called internally by add, so we create a new manager to test loading
        BookManager newManager = new BookManager(testFilename);
        Book retrievedBook = newManager.getById(1);
        assertNotNull(retrievedBook);
        assertEquals("File Book", retrievedBook.getTitle());
    }
}
