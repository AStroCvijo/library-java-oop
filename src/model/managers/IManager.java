package model.managers;

import java.util.List;

public interface IManager<T> {
    void add(T item);
    T getById(int id);
    List<T> getAll();
    void update(T item);
    void delete(int id);
    void loadFromFile();
    void saveToFile();
}