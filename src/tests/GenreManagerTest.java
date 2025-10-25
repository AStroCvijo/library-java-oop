package tests;

import model.entities.Genre;
import model.managers.GenreManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreManagerTest {

    private GenreManager genreManager;
    private String testFilename = "test_genres.csv";

    @BeforeEach
    void setUp() {
        genreManager = new GenreManager(testFilename);
    }

    @AfterEach
    void tearDown() {
        new File(testFilename).delete();
    }

    @Test
    void addAndGetGenre() {
        Genre genre = new Genre(1, "Fiction", "Fictional books");
        genreManager.add(genre);
        Genre retrievedGenre = genreManager.getById(1);
        assertNotNull(retrievedGenre);
        assertEquals("Fiction", retrievedGenre.getName());
    }

    @Test
    void updateGenre() {
        Genre genre = new Genre(1, "Fiction", "Fictional books");
        genreManager.add(genre);
        genre.setName("Science Fiction");
        genreManager.update(genre);
        Genre retrievedGenre = genreManager.getById(1);
        assertEquals("Science Fiction", retrievedGenre.getName());
    }

    @Test
    void deleteGenre() {
        Genre genre = new Genre(1, "Fiction", "Fictional books");
        genreManager.add(genre);
        genreManager.delete(1);
        assertNull(genreManager.getById(1));
    }

    @Test
    void getAllGenres() {
        genreManager.add(new Genre(1, "Fiction", "Fictional books"));
        genreManager.add(new Genre(2, "Non-Fiction", "Non-fictional books"));
        List<Genre> genres = genreManager.getAll();
        assertEquals(2, genres.size());
    }
}
