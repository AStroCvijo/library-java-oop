package model.managers;

import model.entities.Employee;
import model.entities.Genre;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenreManager implements IManager<Genre> {
    private List<Genre> genres;
    private String filename;

    public GenreManager(String filename) {
        this.genres = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }
    public int getNextId() {
        if (genres.isEmpty()) {
            return 1;
        }
        return genres.stream()
                .mapToInt(Genre::getId)
                .max()
                .orElse(0) + 1;
    }


    @Override
    public void add(Genre genre) {
        genres.add(genre);
        saveToFile();
    }

    @Override
    public Genre getById(int id) {
        return genres.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Genre> getAll() {
        return new ArrayList<>(genres);
    }

    @Override
    public void update(Genre genre) {
        for (int i = 0; i < genres.size(); i++) {
            if (genres.get(i).getId() == genre.getId()) {
                genres.set(i, genre);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        genres.removeIf(g -> g.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        genres.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Preskoči header
                }

                String[] data = line.split(",");
                if (data.length >= 3) {
                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    String description = data[2];

                    Genre genre = new Genre(id, name, description);
                    genres.add(genre);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading genres: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("id,name,description");

            for (Genre genre : genres) {
                writer.println(genre.getId() + "," +
                        genre.getName() + "," +
                        genre.getDescription());
            }
        } catch (IOException e) {
            System.out.println("Error saving genres: " + e.getMessage());
        }
    }
}