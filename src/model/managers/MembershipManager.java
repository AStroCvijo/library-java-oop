package model.managers;

import model.entities.Membership;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MembershipManager implements IManager<Membership> {
    private List<Membership> memberships;
    private String filename;

    public MembershipManager(String filename) {
        this.memberships = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }

    @Override
    public void add(Membership membership) {
        memberships.add(membership);
        saveToFile();
    }

    @Override
    public Membership getById(int id) {
        return memberships.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Membership> getAll() {
        return new ArrayList<>(memberships);
    }

    @Override
    public void update(Membership membership) {
        for (int i = 0; i < memberships.size(); i++) {
            if (memberships.get(i).getId() == membership.getId()) {
                memberships.set(i, membership);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        memberships.removeIf(m -> m.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        memberships.clear();
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
                    int memberId = Integer.parseInt(data[1]);
                    LocalDate startDate = LocalDate.parse(data[2]);
                    LocalDate endDate = LocalDate.parse(data[3]);
                    boolean isActive = Boolean.parseBoolean(data[4]);
                    String type = data[5];

                    Membership membership = new Membership(id, memberId, startDate, endDate, isActive, type);
                    memberships.add(membership);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading memberships: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("id,memberId,startDate,endDate,isActive,type");

            for (Membership membership : memberships) {
                writer.println(membership.getId() + "," +
                        membership.getMemberId() + "," +
                        membership.getStartDate() + "," +
                        membership.getEndDate() + "," +
                        membership.isActive() + "," +
                        membership.getType());
            }
        } catch (IOException e) {
            System.out.println("Error saving memberships: " + e.getMessage());
        }
    }

    public Membership getByMemberId(int memberId) {
        return memberships.stream()
                .filter(m -> m.getMemberId() == memberId)
                .findFirst()
                .orElse(null);
    }

    public List<Membership> getActiveMemberships() {
        List<Membership> activeMemberships = new ArrayList<>();
        for (Membership membership : memberships) {
            if (membership.isActive() && membership.getEndDate().isAfter(LocalDate.now())) {
                activeMemberships.add(membership);
            }
        }
        return activeMemberships;
    }

    public void deactivateMembership(int memberId) {
        Membership membership = getByMemberId(memberId);
        if (membership != null) {
            membership.setActive(false);
            update(membership);
        }
    }
}