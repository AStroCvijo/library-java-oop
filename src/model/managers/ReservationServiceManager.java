package model.managers;

import model.entities.ReservationService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationServiceManager implements IManager<ReservationService> {
    private List<ReservationService> reservationServices;
    private String filename;

    public ReservationServiceManager(String filename) {
        this.reservationServices = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }

    public int getNextId() {
        if (reservationServices.isEmpty()) {
            return 1;
        }
        return reservationServices.stream()
                .mapToInt(ReservationService::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public void add(ReservationService reservationService) {
        reservationServices.add(reservationService);
        saveToFile();
    }

    @Override
    public ReservationService getById(int id) {
        return reservationServices.stream()
                .filter(rs -> rs.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<ReservationService> getByReservationId(int reservationId) {
        return reservationServices.stream()
                .filter(rs -> rs.getReservationId() == reservationId)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationService> getAll() {
        return new ArrayList<>(reservationServices);
    }

    @Override
    public void update(ReservationService reservationService) {
        for (int i = 0; i < reservationServices.size(); i++) {
            if (reservationServices.get(i).getId() == reservationService.getId()) {
                reservationServices.set(i, reservationService);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        reservationServices.removeIf(rs -> rs.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        reservationServices.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] data = line.split(",");
                if (data.length >= 5) {
                    int id = Integer.parseInt(data[0]);
                    int reservationId = Integer.parseInt(data[1]);
                    int additionalServiceId = Integer.parseInt(data[2]);
                    int quantity = Integer.parseInt(data[3]);
                    double price = Double.parseDouble(data[4]);

                    ReservationService rs = new ReservationService(id, reservationId, additionalServiceId, quantity, price);
                    reservationServices.add(rs);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reservation services: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("id,reservationId,additionalServiceId,quantity,price");

            for (ReservationService rs : reservationServices) {
                writer.println(rs.getId() + "," +
                        rs.getReservationId() + "," +
                        rs.getAdditionalServiceId() + "," +
                        rs.getQuantity() + "," +
                        rs.getPrice());
            }
        } catch (IOException e) {
            System.out.println("Error saving reservation services: " + e.getMessage());
        }
    }
}
