package model.managers;

import model.entities.Reservation;
import model.enums.ReservationStatus;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager implements IManager<Reservation> {
    private List<Reservation> reservations;
    private String filename;

    public ReservationManager(String filename) {
        this.reservations = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }

    public int getNextId() {
        if (reservations.isEmpty()) {
            return 1;
        }
        return reservations.stream()
                .mapToInt(Reservation::getId)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public void add(Reservation reservation) {
        reservations.add(reservation);
        saveToFile();
    }

    @Override
    public Reservation getById(int id) {
        return reservations.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Reservation> getAll() {
        return new ArrayList<>(reservations);
    }

    @Override
    public void update(Reservation reservation) {
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getId() == reservation.getId()) {
                reservations.set(i, reservation);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        reservations.removeIf(r -> r.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        reservations.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Preskoči header
                }

                String[] data = line.split(",");
                if (data.length >= 8) {
                    int id = Integer.parseInt(data[0]);
                    int memberId = Integer.parseInt(data[1]);
                    int bookId = Integer.parseInt(data[2]);
                    LocalDate reservationDate = LocalDate.parse(data[3]);
                    LocalDate pickupDate = LocalDate.parse(data[4]);
                    LocalDate returnDate = LocalDate.parse(data[5]);
                    ReservationStatus status = ReservationStatus.valueOf(data[6]);
                    double totalPrice = Double.parseDouble(data[7]);

                    Reservation reservation = new Reservation(id, memberId, bookId,
                            reservationDate, pickupDate, returnDate,
                            status, totalPrice);
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading reservations: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("id,memberId,bookId,reservationDate,pickupDate,returnDate,status,totalPrice");

            for (Reservation reservation : reservations) {
                writer.println(reservation.getId() + "," +
                        reservation.getMemberId() + "," +
                        reservation.getBookId() + "," +
                        reservation.getReservationDate() + "," +
                        reservation.getPickupDate() + "," +
                        reservation.getReturnDate() + "," +
                        reservation.getStatus() + "," +
                        reservation.getTotalPrice());
            }
        } catch (IOException e) {
            System.out.println("Error saving reservations: " + e.getMessage());
        }
    }

    public List<Reservation> getReservationsByMember(int memberId) {
        List<Reservation> memberReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getMemberId() == memberId) {
                memberReservations.add(reservation);
            }
        }
        return memberReservations;
    }

    public void updateExpiredReservations() {
        LocalDate today = LocalDate.now();
        MemberManager memberManager = new MemberManager("data/members.csv");
        for (Reservation res : reservations) {
            if (res.getStatus() == ReservationStatus.PENDING && res.getPickupDate().isBefore(today)) {
                res.setStatus(ReservationStatus.REJECTED);
                update(res);
            } else if (res.getStatus() == ReservationStatus.CONFIRMED && res.getPickupDate().isBefore(today)) {
                res.setStatus(ReservationStatus.CANCELED);
                
                model.entities.Member member = memberManager.getById(res.getMemberId());
                if (member != null) {
                    member.setLastCancellationDate(LocalDate.now());
                    memberManager.update(member);
                }
                update(res);
            }
        }
    }
}