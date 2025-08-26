package model.managers;

import model.entities.Member;
import model.enums.Gender;
import model.enums.MembershipCategory;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberManager implements IManager<Member> {
    private List<Member> members;
    private String filename;

    public MemberManager(String filename) {
        this.members = new ArrayList<>();
        this.filename = filename;
        loadFromFile();
    }

    @Override
    public void add(Member member) {
        members.add(member);
        saveToFile();
    }

    @Override
    public Member getById(int id) {
        return members.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Member> getAll() {
        return new ArrayList<>(members);
    }

    @Override
    public void update(Member member) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == member.getId()) {
                members.set(i, member);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public void delete(int id) {
        members.removeIf(m -> m.getId() == id);
        saveToFile();
    }

    @Override
    public void loadFromFile() {
        members.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Preskoči header
                }

                String[] data = line.split(",");
                if (data.length >= 10) {
                    int id = Integer.parseInt(data[0]);
                    String firstName = data[1];
                    String lastName = data[2];
                    Gender gender = Gender.valueOf(data[3]);
                    LocalDate birthDate = LocalDate.parse(data[4]);
                    String phone = data[5];
                    String address = data[6];
                    String username = data[7];
                    String password = data[8];
                    MembershipCategory category = data[9].equals("null") ? null : MembershipCategory.valueOf(data[9]);

                    Member member = new Member(id, firstName, lastName, gender,
                            birthDate, phone, address, username,
                            password, category);
                    members.add(member);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }
    }

    @Override
    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("id,firstName,lastName,gender,birthDate,phone,address,username,password,category");

            for (Member member : members) {
                writer.println(member.getId() + "," +
                        member.getFirstName() + "," +
                        member.getLastName() + "," +
                        member.getGender() + "," +
                        member.getBirthDate() + "," +
                        member.getPhone() + "," +
                        member.getAddress() + "," +
                        member.getUsername() + "," +
                        member.getPassword() + "," +
                        (member.getCategory() != null ? member.getCategory().name() : "null"));
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }
}