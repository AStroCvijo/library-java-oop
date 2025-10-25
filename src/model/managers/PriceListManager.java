package model.managers;

import model.entities.Employee;
import model.entities.PriceListItem;
import model.enums.PriceListItemType;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PriceListManager implements IManager<PriceListItem> {
    private List<PriceListItem> priceListItems;
    private String filename;

    public PriceListManager(String filename) {
        this.priceListItems = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }

    public int getNextId() {
        if (priceListItems.isEmpty()) {
            return 1;
        }
        return priceListItems.stream()
                .mapToInt(PriceListItem::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public void add(PriceListItem priceListItem) {
        priceListItems.add(priceListItem);
        saveToFile();
    }

    @Override
    public PriceListItem getById(int id) {
        return priceListItems.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public PriceListItem getItemByType(PriceListItemType type) {
        return priceListItems.stream()
                .filter(p -> p.getType() == type)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<PriceListItem> getAll() {
        return new ArrayList<>(priceListItems);
    }

    @Override
    public void update(PriceListItem priceListItem) {
        for (int i = 0; i < priceListItems.size(); i++) {
            if (priceListItems.get(i).getId() == priceListItem.getId()) {
                priceListItems.set(i, priceListItem);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        priceListItems.removeIf(p -> p.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        priceListItems.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Preskoči header
                }

                String[] data = line.split(",");
                if (data.length >= 6) {
                    int id = Integer.parseInt(data[0]);
                    PriceListItemType type = PriceListItemType.valueOf(data[1]);
                    String description = data[2];
                    double price = Double.parseDouble(data[3]);
                    LocalDate validFrom = LocalDate.parse(data[4]);
                    LocalDate validTo = LocalDate.parse(data[5]);

                    PriceListItem priceListItem = new PriceListItem(id, type, description,
                            price, validFrom, validTo);
                    priceListItems.add(priceListItem);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading price list items: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("id,type,description,price,validFrom,validTo");

            for (PriceListItem priceListItem : priceListItems) {
                writer.println(priceListItem.getId() + "," +
                        priceListItem.getType() + "," +
                        priceListItem.getDescription() + "," +
                        priceListItem.getPrice() + "," +
                        priceListItem.getValidFrom() + "," +
                        priceListItem.getValidTo());
            }
        } catch (IOException e) {
            System.out.println("Error saving price list items: " + e.getMessage());
        }
    }
}